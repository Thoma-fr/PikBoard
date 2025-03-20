package com.example.pikboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.pikboard.ui.theme.PikBoardTheme
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PikBoardTheme {
                Myapp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val expanded = remember { mutableStateOf(false) }
    val extraPadding= if (expanded.value) 48.dp else 0.dp
    Surface(color = MaterialTheme.colorScheme.primary, modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
        Row(modifier = Modifier.padding(24.dp)) {

            Column( modifier = Modifier.weight(1f).padding(bottom = extraPadding)) {

                Text(
                    text = "Hello",
                )
                Text(
                    text = "$name!",
                )
            }
            ElevatedButton (onClick = {  expanded.value= !expanded.value})
            {
                Text(if(expanded.value)"skibidi" else "skbididou")
            }
        }
    }
}
@Composable
fun OnboardingScreen(onContinueClicked: () -> Unit,modifier: Modifier=Modifier)
{
    var shouldSdhowOnBoarding by remember { mutableStateOf(true) }
    Column(modifier =modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text("welcome to PikBoard")
        Button(modifier = Modifier.padding(vertical = 24.dp), onClick = {shouldSdhowOnBoarding=false})
        {
            Text("Continue")
        }
    }
}
@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnBoardingPreview()
{
    OnboardingScreen(onContinueClicked = {})
}
@Composable
fun Myapp(modifier: Modifier=Modifier,names:List<String> = listOf("user1","user2with a long")){
    var shouldSdhowOnBoarding by remember { mutableStateOf(true) }
    Surface(modifier) {
        if(shouldSdhowOnBoarding){
            OnboardingScreen( onContinueClicked = { shouldSdhowOnBoarding = false })
        }
        else
        {
            Greetings()
        }
    }
Greetings()
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PikBoardTheme {
        Myapp(Modifier.fillMaxSize())
    }
}
@Composable
private fun Greetings(modifier: Modifier=Modifier,names:List<String> = List(100){ "$it"})
{
    Surface(modifier=modifier, color = MaterialTheme.colorScheme.background)
    {
        LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
            items(items = names) { name ->
                Greeting(name = name)
            }
        }

    }
}