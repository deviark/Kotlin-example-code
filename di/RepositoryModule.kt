package com.lotteryadviser.di

import com.lotteryadviser.data.database.combination.CombinationDao
import com.lotteryadviser.data.database.country.CountryDao
import com.lotteryadviser.data.database.lottery.LotteryDao
import com.lotteryadviser.data.database.recommendnumbers.RecommendNumbersDao
import com.lotteryadviser.data.network.RestApi
import com.lotteryadviser.data.repositories.combinations.CombinationsRepositoryImp
import com.lotteryadviser.data.repositories.countries.CountriesRepositoryImp
import com.lotteryadviser.data.repositories.gamesresults.GamesResultsRepositoryImp
import com.lotteryadviser.data.repositories.gamesystems.GameSystemsRepositoryImp
import com.lotteryadviser.data.repositories.lotteries.LotteriesRepositoryImp
import com.lotteryadviser.data.repositories.recomendnumbres.RecommendGroupNumbersRepositoryImp
import com.lotteryadviser.data.repositories.recommendcombinations.RecommendCombinationsRepositoryImp
import com.lotteryadviser.domain.repository.combinations.CombinationsRepository
import com.lotteryadviser.domain.repository.countries.CountriesRepository
import com.lotteryadviser.domain.repository.gamesresults.GamesResultsRepository
import com.lotteryadviser.domain.repository.gamesystems.GameSystemsRepository
import com.lotteryadviser.domain.repository.lotteries.LotteriesRepository
import com.lotteryadviser.domain.repository.recommendcombinations.RecommendCombinationsRepository
import com.lotteryadviser.domain.repository.recommendnumbers.RecommendGroupNumbersRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideCountriesRepository(
        restApi: RestApi,
        countriesDao: CountryDao,
    ): CountriesRepository = CountriesRepositoryImp(restApi, countriesDao)

    @Provides
    fun provideLotteriesRepository(
        restApi: RestApi,
        lotteriesDao: LotteryDao
    ): LotteriesRepository = LotteriesRepositoryImp(restApi, lotteriesDao)

    @Provides
    fun provideRecommendGroupNumbersRepository(
        restApi: RestApi,
        recommendNumbersDao: RecommendNumbersDao
    ): RecommendGroupNumbersRepository = RecommendGroupNumbersRepositoryImp(restApi, recommendNumbersDao)

    @Provides
    fun provideGameSystemsRepository(
        restApi: RestApi,
    ): GameSystemsRepository = GameSystemsRepositoryImp(restApi)

    @Provides
    fun provideRecommendNumbersRepository(
        restApi: RestApi
    ): RecommendCombinationsRepository = RecommendCombinationsRepositoryImp(restApi)

    @Provides
    fun provideGamesResultsRepository(
        restApi: RestApi
    ): GamesResultsRepository = GamesResultsRepositoryImp(restApi)

    @Provides
    fun provideCombinationsRepository(
        combinationDao: CombinationDao
    ): CombinationsRepository = CombinationsRepositoryImp(combinationDao)
}