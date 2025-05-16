package com.stephenelg.timecapsulenotes

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class TestAppRunner: AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application? {
        return super.newApplication(
            cl,
            TimeCapsuleNotesApplication::class.java.name,
            context)
    }
}