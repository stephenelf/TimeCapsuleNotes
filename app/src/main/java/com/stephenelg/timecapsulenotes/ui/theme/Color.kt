@file:Suppress("IMPLICIT_CAST_TO_ANY")

package com.stephenelg.timecapsulenotes.ui.theme

import android.os.Build
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val dynamicColorEnabled= Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
val colorScheme = if (dynamicColorEnabled){
    //if dark
  //  val dynamicDarkColorScheme = dynamicDarkColorScheme(LocalContext.current)
    //dynamicDarkColorScheme
} else {
    //if light

}