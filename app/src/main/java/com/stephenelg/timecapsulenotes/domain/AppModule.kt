package com.stephenelg.timecapsulenotes.domain

import android.util.Log
import com.stephenelg.timecapsulenotes.data.TimeCapsuleNoteDao
import com.stephenelg.timecapsulenotes.data.api.PokemonApi
import com.stephenelg.timecapsulenotes.domain.repository.NotesRepository
import com.stephenelg.timecapsulenotes.data.repository.NotesRepositoryImpl
import com.stephenelg.timecapsulenotes.domain.repository.PokemonRepository
import com.stephenelg.timecapsulenotes.data.repository.PokemonRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabaseRepository(timeCapsuleNoteDao: TimeCapsuleNoteDao): NotesRepository {
        Log.d("AppModule", "DatabaseRepository created")
        return NotesRepositoryImpl(timeCapsuleNoteDao)
    }

    @Provides
    @Singleton
    fun providePokemonRepository(pokemonApi: PokemonApi): PokemonRepository {
        return PokemonRepositoryImpl(pokemonApi)

    }
}