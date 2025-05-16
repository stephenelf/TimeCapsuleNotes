package com.stephenelg.timecapsulenotes.data.api

import com.stephenelg.timecapsulenotes.data.model.Pokemon
import com.stephenelg.timecapsulenotes.data.model.PokemonColor
import com.stephenelg.timecapsulenotes.data.model.PokemonHabitat
import com.stephenelg.timecapsulenotes.data.ResourceList
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApi {

    @GET("pokemon/{dexNumOrName}/")
    suspend fun getPokemonByDexNumOrName(@Path("dexNumOrName") dexNumOrName: String): Pokemon

    @GET("pokemon-habitat/{dexNumOrName}/")
    suspend fun getPokemonHabitatByDexNumOrName(@Path("dexNumOrName") dexNumOrName: String): PokemonHabitat

    @GET("pokemon-habitat/")
    suspend fun getPokemonHabitats(): ResourceList<PokemonHabitat>

    @GET("pokemon-color/")
    suspend fun getPokemonColors(): ResourceList<PokemonColor>

}
