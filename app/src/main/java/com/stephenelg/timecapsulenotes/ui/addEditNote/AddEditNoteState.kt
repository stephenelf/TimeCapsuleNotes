package com.stephenelg.timecapsulenotes.ui.addEditNote

import com.stephenelg.timecapsulenotes.data.model.PokemonHabitat
import java.util.Date

data class AddEditNoteState(val title: String="", val content: String="", val date : Date = Date(), val habitats: List<PokemonHabitat> = emptyList(),
                            val habitat:String ="", val userMessage: Int? = null,
                            val isLoading: Boolean = false, val isSaved: Boolean = false)
