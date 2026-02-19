package com.vani.myapp.Pantallas

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vani.myapp.TiendaViewModel
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.draw.scale

/**
 * Pantalla de éxito que se muestra al finalizar el proceso de compra.
 * * Su objetivo es dar una confirmación visual positiva al usuario y ofrecer
 * un botón claro para regresar al catálogo principal de VaniShoots.
 * * @param onVolver Acción que se dispara para regresar al inicio. Gracias a la
 * lógica en GestionNavegacion, esta función se encarga de limpiar el historial
 * para que no se pueda volver atrás a la pantalla de pago.
 */
@Composable
fun Confirmacion(navegaAHome: () -> Unit, vm: TiendaViewModel= viewModel()){ //esta funcion de parametro  hace que vuelva al home luego
    //se ejecuta el metodo de limpiar la lista
    LaunchedEffect(Unit) {
        vm.limpiarCarrito()
    }
    //animamos que sube y baja(aumenta y disminuye)
    val infiniteTransition = rememberInfiniteTransition(label = "escala_infinita")
    val escalaBoton by infiniteTransition.animateFloat(
        initialValue = 1f,   // Tamaño normal (100%)
        targetValue = 1.05f, // Crece un 5%
        animationSpec = infiniteRepeatable(
            // La animación dura 1 segundo en crecer y volver a encoger
            animation = tween(1000, easing = FastOutSlowInEasing),
            // Repetir en modo espejo (ida y vuelta suave)
            repeatMode = RepeatMode.Reverse
        ),
        label = "escala"
    )
    //diseño
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¡Compra realizada!",
            style = MaterialTheme.typography.headlineLarge, // Añadido estilo para que sea grande
            color = MaterialTheme.colorScheme.secondary, // Corregido: es "color", no "colors"
            modifier = Modifier
                .padding(20.dp)
                .scale(escalaBoton) // Aplicamos la animación
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = navegaAHome,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onPrimary //letras sobre el boton
            )
        ) {
            Text("Volver a la Tienda")
        }
    }
}