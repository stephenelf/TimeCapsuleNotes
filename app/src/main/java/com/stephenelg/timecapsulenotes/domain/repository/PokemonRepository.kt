package com.stephenelg.timecapsulenotes.domain.repository

import com.stephenelg.timecapsulenotes.data.model.Pokemon
import com.stephenelg.timecapsulenotes.data.model.PokemonColor
import com.stephenelg.timecapsulenotes.data.model.PokemonHabitat
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    suspend fun getPokemons(pokeDexOrName: String): Flow<Pokemon>

    fun getPokemonHabitats(): Flow<List<PokemonHabitat>>

    suspend fun getPokemonColors(): Flow<List<PokemonColor>>

}