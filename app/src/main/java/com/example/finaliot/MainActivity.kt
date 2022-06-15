package com.example.finaliot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finaliot.ui.theme.FinaloTTheme


class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            FinaloTTheme(){
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("ON")
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String) {

    var name by remember { mutableStateOf("name")}

    val mainViewModel: IoTViewModel = viewModel()

    val notifications: String? by mainViewModel.estado.observeAsState()

    var cambio: String



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(text = notifications!!.toString(),textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Button(
            modifier = Modifier.padding(top = 10.dp),
            onClick = {

                mainViewModel.nuevoEstado(notifications.toString())

                //mainViewModel.nuevoEstado(notifications!!.toString(),pusherIoT)

            }
            /*modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)*/
        ) {

            Text("ENVIAR")

        }

    }

}







/*
@Preview(name="Tremenda chusta", showSystemUi = true, showBackground = true, device = Devices.DEFAULT)
@Composable
fun DefaultPreview() {
    PruebaIoTTheme {
        Greeting("Android")
    }
}*/