package com.example.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapp.Pantallas.Home
import com.example.myapp.navegacion.GestionNavegacion
import com.example.myapp.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme() {
                GestionNavegacion()
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMiApp() {
    MyAppTheme {
        Home(navegaADetalle = { id -> },
            navegaACarrito = { })
    }

}