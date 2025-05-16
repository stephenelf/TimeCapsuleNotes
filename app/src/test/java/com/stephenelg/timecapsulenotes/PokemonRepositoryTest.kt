package com.stephenelg.timecapsulenotes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.stephenelg.timecapsulenotes.data.api.PokemonApi
import com.stephenelg.timecapsulenotes.domain.repository.PokemonRepository
import com.stephenelg.timecapsulenotes.data.repository.PokemonRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


@OptIn(ExperimentalCoroutinesApi::class)
class PokemonRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var pokemonService: PokemonApi
    private lateinit var pokemonRepository: PokemonRepository

    @Before
    fun setUp() {
        val interceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.HEADERS
        }
        val okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(interceptor)
            .build()
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        pokemonService = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build().create(PokemonApi::class.java)
        pokemonRepository = PokemonRepositoryImpl(pokemonService)

    }

    @Test
    fun getPokemonFromRepository() = runTest {

        System.out.println("Testing repository")
        pokemonRepository.getPokemons("44").collect { pokemons ->
            System.out.println("Name: ${pokemons.name}")
            Assert.assertNotNull(pokemons.name)
        }
    }

    @Test
    fun getPokemonColorsFromRepository() = runTest {

        pokemonRepository.getPokemonColors().collect { colors ->
            System.out.println("Colors: ${colors.size}")
            colors.forEach { System.out.println("Name: ${it.name}") }
            // Assert.assertNotNull(colors.name)
        }
    }

    @Test
    fun getPokemonsHabitatsFromRepository() = runTest {
        pokemonRepository.getPokemonHabitats().collect { habitats ->
            System.out.println("Habitats: ${habitats.size}")
            habitats.forEach { System.out.println("Name: ${it.name}") }
            // Assert.assertNotNull(colors.name)
        }
    }
}