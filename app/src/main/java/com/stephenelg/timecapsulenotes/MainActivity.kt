package com.stephenelg.timecapsulenotes


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.stephenelg.timecapsulenotes.ui.NotesNavGraph
import com.stephenelg.timecapsulenotes.ui.notesList.NotesListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: NotesListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesNavGraph()
        }
    }
}




@Composable
fun Title(){
    val infinteTransition = rememberInfiniteTransition()
    val rotationAnimation = infinteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1000, easing = LinearEasing))
    )
    val rainbowColorBrush = Brush.radialGradient(listOf(Color.Red, Color.Blue, Color.Yellow),
        tileMode = TileMode.Repeated)

    Card(modifier = Modifier.fillMaxWidth()
        .padding(8.dp)
        .drawBehind {
            rotate(rotationAnimation.value) {

                drawRect(rainbowColorBrush, style = Stroke(16.dp.toPx()))
            }
        }
    )
            {
                Text(
                    text = "Time Capsule Notes",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(8.dp).align(CenterHorizontally)
                    )
                //)
            }
}


