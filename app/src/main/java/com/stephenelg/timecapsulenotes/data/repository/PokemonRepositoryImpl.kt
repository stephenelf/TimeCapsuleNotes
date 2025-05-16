package com.stephenelg.timecapsulenotes.data.repository

import com.stephenelg.timecapsulenotes.data.model.Pokemon
import com.stephenelg.timecapsulenotes.data.model.PokemonColor
import com.stephenelg.timecapsulenotes.data.model.PokemonHabitat
import com.stephenelg.timecapsulenotes.data.api.PokemonApi
import com.stephenelg.timecapsulenotes.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(private val api: PokemonApi) : PokemonRepository {
    override suspend fun getPokemons(pokeDexOrName: String): Flow<Pokemon> {
       return flow{
           emit(api.getPokemonByDexNumOrName(pokeDexOrName))
       }
    }

    override fun getPokemonHabitats(): Flow<List<PokemonHabitat>> {
        return flow{
            emit(api.getPokemonHabitats().results)
        }
    }

    override suspend fun getPokemonColors(): Flow<List<PokemonColor>> {
        return flow{
            emit(api.getPokemonColors().results)
        }
    }


}