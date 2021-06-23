package com.lotteryadviser.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillingManager @Inject constructor(
    private val context: Context,
    private val coroutineScope: CoroutineScope
) {

    companion object {
        private const val TAG = "BillingManager"
        const val SUB_FOUR_WEEKS = "la_4_weeks"
        const val SUB_YEAR = "la_1_year"

        private const val MAX_COUNT_RETRY_CONNECT = 3
    }

    private var countRetryConnect = 0
    private var isEndConnection: Boolean = false

    private var state: State = State()
    private val stateBilling: MutableStateFlow<State> = MutableStateFlow(state)
    private val _stateBilling = stateBilling.asStateFlow()

    private var _stateOperation: MutableStateFlow<ResponseCode?> = MutableStateFlow(null)
    private var updateStateOperation: Boolean = false
    val stateOperation = _stateOperation.asStateFlow()

    init {
        log("$this")
    }

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        log("PurchasesUpdatedListener")
        log("debug message: ${billingResult.debugMessage}")
        logResponseCode(billingResult.responseCode)
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && !purchases.isNullOrEmpty()) {
            if (!purchases.isNullOrEmpty()) {
                for (item in purchases) {
                    log(item.originalJson)
                }
                payComplete(purchases)
                val purchaseItem = purchases.first()
                val token = purchaseItem.purchaseToken
                val purchaseID = purchaseItem.sku
                val packageName = purchaseItem.packageName
                coroutineScope.launch {
                    state =
                        State(Status.READY, true, state.skuDetails, token, purchaseID, packageName)
                    stateBilling.emit(state)
                }
            } else {
                coroutineScope.launch {
                    state =
                        State(Status.READY, false,  state.skuDetails)
                    stateBilling.emit(state)
                }
            }
        }
        if (updateStateOperation) {
            _stateOperation.value = convertResponseCode(billingResult.responseCode)
            _stateOperation.value = null
            updateStateOperation = false
        }
    }


    private var billingClient: BillingClient = createBillingClient()

    private fun createBillingClient(): BillingClient {
        return BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
    }

    fun getState(): StateFlow<State> {
        if (state.status == Status.ERROR || isEndConnection) {
            startConnection()
        }
        return _stateBilling
    }

    fun startConnection() {
        log("start")
        if (isEndConnection) {
            isEndConnection = false
            billingClient = createBillingClient()
            log("recreate billingClient")
        }
        log("isReady: ${billingClient.isReady}")
        if (!billingClient.isReady) {
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    log("onBillingSetupFinished")
                    logResponseCode(billingResult.responseCode)
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        coroutineScope.launch {
                            launch { querySkuDetails() }.join()
                            launch { restorePurchase() }.join()
                            state = State(
                                Status.READY,
                                state.hasSubscription,
                                state.skuDetails,
                                state.token,
                                state.purchaseID,
                                state.packageName
                            )
                            stateBilling.emit(state)
                        }
                    } else {
                        stateError()
                    }
                }

                override fun onBillingServiceDisconnected() {
                    log("onBillingServiceDisconnected")
                    if (countRetryConnect < MAX_COUNT_RETRY_CONNECT) {
                        log("try reconnect")
                        isEndConnection = true
                        startConnection()
                        countRetryConnect++
                    } else {
                        stateError()
                        countRetryConnect = 0
                    }

                }
            })
        }
    }

    private fun stateError() {
        coroutineScope.launch {
            state = State(Status.ERROR, state.hasSubscription, state.skuDetails)
            stateBilling.emit(state)
        }
    }

    private suspend fun querySkuDetails() {
        val skuList = mutableListOf(SUB_FOUR_WEEKS, SUB_YEAR)
        val param = SkuDetailsParams.newBuilder()
            .setSkusList(skuList)
            .setType(BillingClient.SkuType.SUBS)
            .build()

        val skuResponse = withContext(Dispatchers.IO) {
            billingClient.querySkuDetails(param)
        }
        val billingResult = skuResponse.billingResult
        log("onSkuDetailsResponse")
        logResponseCode(billingResult.responseCode)
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            val skuDetailsList = skuResponse.skuDetailsList
            if (!skuDetailsList.isNullOrEmpty()) {
                val skuDetailsMap: MutableMap<String, SkuDetails> = mutableMapOf()
                skuDetailsMap[SUB_FOUR_WEEKS] =
                    skuDetailsList.find { it.sku == SUB_FOUR_WEEKS }!!
                skuDetailsMap[SUB_YEAR] = skuDetailsList.find { it.sku == SUB_YEAR }!!
                state = State(Status.LOADING, false, skuDetailsMap)
                stateBilling.emit(state)
                for (skuItem in skuDetailsList) {
                    log(skuItem.originalJson)
                }
            }
        }

    }

    private suspend fun restorePurchase() {
        log("restorePurchase")
        val purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS)
        val purchases = purchasesResult.purchasesList
        val purchaseItem = purchases?.maxByOrNull { it.purchaseTime }
        if (!purchases.isNullOrEmpty()) {
            payComplete(purchases)
            for (item in purchasesResult.purchasesList!!) {
                log("$item")
            }
        }
        if (purchaseItem != null) {
            val token = purchaseItem.purchaseToken
            val purchaseID = purchaseItem.sku
            val packageName = purchaseItem.packageName
            purchaseItem.purchaseState

            state = State(Status.LOADING, true, state.skuDetails, token, purchaseID, packageName)
            stateBilling.emit(state)
        } else {
            State(Status.LOADING, false, state.skuDetails)
            stateBilling.emit(state)
        }

    }

    fun endConnection() {
        log("endConnection")
        isEndConnection = true
        billingClient.endConnection()
    }

    fun launchBillingFlow(skuDetails: SkuDetails, activity: Activity) {
        log("launchBillingFlow")
        val flowParam = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails)
            .build()
        log("Start")
        updateStateOperation = true
        val billingResult = billingClient.launchBillingFlow(activity, flowParam)
        logResponseCode(billingResult.responseCode)
    }

    private fun payComplete(purchases: List<Purchase>?) {
        log("payComplete")
        if (!purchases.isNullOrEmpty()) {
            coroutineScope.launch {
                for (purchase in purchases) {
                    if (!purchase.isAcknowledged) {
                        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                            .setPurchaseToken(purchase.purchaseToken)
                        val ackPurchaseResult = withContext(Dispatchers.IO) {
                            billingClient.acknowledgePurchase(acknowledgePurchaseParams.build())
                        }
                        logResponseCode(ackPurchaseResult.responseCode)
                    }
                }
            }
        }
    }

    data class State(
        val status: Status = Status.LOADING,
        val hasSubscription: Boolean = false,
        val skuDetails: Map<String, SkuDetails> = emptyMap(),
        val token: String = "",
        val purchaseID: String = "",
        val packageName: String = ""
    ) {
        fun getSkuDetailsFourWeek(): SkuDetails? {
            return skuDetails[SUB_FOUR_WEEKS]
        }

        fun getSkuDetailsYear(): SkuDetails? {
            return skuDetails[SUB_YEAR]
        }
    }

    enum class Status {
        LOADING, READY, ERROR
    }

    private fun logResponseCode(responseCode: Int) {
        log("response code: ${convertResponseCode(responseCode)}")
    }

    private fun log(message: String) {
        Log.d(TAG, message)
    }

    private fun convertResponseCode(responseCode: Int): ResponseCode {
        return when (responseCode) {
            -3 -> ResponseCode.SERVICE_TIMEOUT
            -2 -> ResponseCode.FEATURE_NOT_SUPPORTED
            -1 -> ResponseCode.SERVICE_DISCONNECTED
            0 -> ResponseCode.OK
            1 -> ResponseCode.USER_CANCELED
            2 -> ResponseCode.SERVICE_UNAVAILABLE
            3 -> ResponseCode.BILLING_UNAVAILABLE
            4 -> ResponseCode.ITEM_UNAVAILABLE
            5 -> ResponseCode.DEVELOPER_ERROR
            6 -> ResponseCode.ERROR
            7 -> ResponseCode.ITEM_ALREADY_OWNED
            8 -> ResponseCode.ITEM_NOT_OWNED
            else -> ResponseCode.UNKNOWN
        }
    }

    enum class ResponseCode {
        SERVICE_TIMEOUT,
        FEATURE_NOT_SUPPORTED,
        SERVICE_DISCONNECTED,
        OK,
        USER_CANCELED,
        SERVICE_UNAVAILABLE,
        BILLING_UNAVAILABLE,
        ITEM_UNAVAILABLE,
        DEVELOPER_ERROR,
        ERROR,
        ITEM_ALREADY_OWNED,
        ITEM_NOT_OWNED,
        UNKNOWN,
    }

}