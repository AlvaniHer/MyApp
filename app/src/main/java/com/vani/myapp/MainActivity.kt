package com.vani.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vani.myapp.ui.Pantallas.Home
import com.vani.myapp.ui.navegacion.GestionNavegacion
import com.vani.myapp.ui.theme.MyAppTheme
import com.vani.myapp.viewmodel.PortafolioViewModel

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
    val carritoVM: PortafolioViewModel = viewModel()
    MyAppTheme {
        Home(
            miViewModel = carritoVM,
            navegaADetalle = { id -> },
            navegaACarrito = { })
    }

}