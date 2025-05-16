package com.stephenelg.timecapsulenotes.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Pokemon(val id: Int, val name: String)
