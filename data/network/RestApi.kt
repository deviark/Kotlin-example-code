package com.lotteryadviser.data.network

import com.lotteryadviser.data.network.request.LotteryIdsRequest
import com.lotteryadviser.data.network.response.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.time.Instant

interface RestApi {

    @GET("/api/v1/countries")
    suspend fun getCountries(): CountriesResponse

    @GET("/api/v1/lotteries")
    suspend fun getLotteries(
        @Query("country_code") countryCode: String
    ): LotteriesResponse

    @POST("/api/v1/lotteries")
    suspend fun getFavoriteLotteries(
        @Body request: LotteryIdsRequest
    ): LotteriesResponse

    @GET("/api/v1/recommend-numbers")
    suspend fun getRecommendGroupNumbers(
        @Query("lottery_id") lotteryId: Long,
        @Query("play_at") playAt: Instant,
        @Query("token") token: String,
        @Query("subscriptionId") purchaseID: String,
        @Query("packageName") packageName: String
    ): RecommendGroupNumbersResponse


    @GET("/api/v1/systems")
    suspend fun getGameSystems(
        @Query("lottery_id") lotteryID: Long,
        @Query("count_numbers") countNumbers: Int,
        @Query("token") token: String,
        @Query("subscriptionId") purchaseID: String,
        @Query("packageName") packageName: String
    ): SystemsResponse


    @GET("/api/v1/combinations")
    suspend fun getRecommendCombinations(
        @Query("system_id") systemId: Long,
        @Query("play_at") playAt: Instant,
        @Query("token") token: String,
        @Query("subscriptionId") purchaseID: String,
        @Query("packageName") packageName: String
    ): CombinationsResponse

    @GET("/api/v1/game-results")
    suspend fun getGamesResults(
        @Query("lottery_id") lotteryID: Long,
        @Query("page") page: Int,
        @Query("played_at") playedAt: String?,
    ): GameResultsPageResponse
}