package com.stephenelg.timecapsulenotes.domain

sealed class UIResources<out T> {
    //Represents the successful retrieval of data.
    // It contains the actual data returned by the repository.
    data class Success<out T>(val data: T) : UIResources<T>()

    //Represents an error during the operation.
    // It contains an error message to be shown to the user
    data class Error(val message: String) : UIResources<Nothing>()

    //Represents the loading state while the data is being fetched
    object Loading : UIResources<Nothing>()
}