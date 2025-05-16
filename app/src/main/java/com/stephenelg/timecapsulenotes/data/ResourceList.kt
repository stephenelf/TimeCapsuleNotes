package com.stephenelg.timecapsulenotes.data

data class ResourceList<T>(val count: Int, val next: String?, val previous: String?, val results: List<T>)
