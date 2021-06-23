package com.lotteryadviser.domain.usesase

import com.lotteryadviser.billing.BillingManager
import com.lotteryadviser.data.Preferences
import com.lotteryadviser.domain.model.Error
import com.lotteryadviser.domain.model.Result
import com.lotteryadviser.domain.repository.countries.CountriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject


class LaunchCase @Inject constructor(
    private val countriesRepository: CountriesRepository,
    private val preferences: Preferences,
    private val billingManger: BillingManager
) {

    fun updateData(): Flow<Result<Boolean>> {
        return flow {
            val result = try {
                if (needUpdate()) {
                    countriesRepository.update()
                    preferences.dateLastCountriesUpdate = Instant.now()
                }
                val firstLaunch = preferences.isFirstLaunch
                if (preferences.isFirstLaunch) {
                    preferences.isFirstLaunch = false
                }
                Result.Success(firstLaunch)
            } catch (e: Exception) {
                if (!preferences.isFirstLaunch) {
                    Result.Success(false)
                } else {
                    Result.Failure(false, Error.NetworkError)
                }
            }
            emit(result)
        }
    }

    private fun needUpdate(): Boolean {
        val lastDateCountriesUpdate = preferences.dateLastCountriesUpdate
        return if (lastDateCountriesUpdate == null) {
          true
        } else {
            val newTimeUpdate = lastDateCountriesUpdate.plus(1, ChronoUnit.DAYS)
            newTimeUpdate.isBefore(lastDateCountriesUpdate)
        }
    }


    fun connectBillingManager() {
        billingManger.startConnection()
    }

    fun endConnectionBillingManger() {
        billingManger.endConnection()
    }

    fun hasSubscription(): Flow<Boolean> {
        return flow {
            billingManger.getState().collect { state ->
                if (state.status == BillingManager.Status.READY) {
                    emit(state.hasSubscription)
                }
            }
        }

    }
}