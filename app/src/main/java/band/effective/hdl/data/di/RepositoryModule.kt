package band.effective.hdl.data.di

import band.effective.hdl.data.LeaderIdRepositoryImpl
import band.effective.hdl.data.PreferencesRepositoryImpl
import band.effective.hdl.domain.LeaderIdRepository
import band.effective.hdl.domain.PreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindLeaderIdRepository(leaderIdRepositoryImpl: LeaderIdRepositoryImpl) : LeaderIdRepository

    @Binds
    fun bindPrefRepository(preferencesRepositoryImpl: PreferencesRepositoryImpl) : PreferencesRepository
}