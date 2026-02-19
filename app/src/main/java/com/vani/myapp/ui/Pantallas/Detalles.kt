package com.vani.myapp.ui.Pantallas

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vani.myapp.ui.componente.TarjetaProducto
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import com.vani.myapp.viewmodel.CarritoViewModel
import com.vani.myapp.viewmodel.PortafolioViewModel

/**
 * Pantalla que muestra la información completa de un servicio específico.
 * * Realiza las siguientes tareas:
 * 1. Busca el producto en el (TiendaViewModel) usando el ID recibido.
 * 2. Implementa una animación de "latido" infinita en el botón de compra para
 * atraer la atención del usuario
 * 3. Reutiliza el componente (TarjetaProducto) para mantener la coherencia visual.
 * * @param id Identificador único del producto a mostrar.
 * @param navegaAtras Función para regresar a la pantalla de inicio.
 * @param miViewModel Fuente de datos que contiene la lista de servicios.
 */
@Composable
fun Detalles(
    id: Int,
    navegaAtras: () -> Unit,
    portafolioVM: PortafolioViewModel,
    carritoVM: CarritoViewModel
) {
    var mostrardialogodeñadir by remember { mutableStateOf(false) } //el dialogo de compra
    //el producto en la lista del ViewModel usando el id
    val listaProductos by portafolioVM.productos.collectAsState()
    val producto = listaProductos.find { it.id == id }
    //transición infinita
    val transition = rememberInfiniteTransition(label = "pulso_boton")
    //animamos que sube y baja(aumenta y disminuye)
    val escalaBoton by transition.animateFloat(
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

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { paddingValues ->
        if (producto != null) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                // la tarjeta del producto (Ya incluye los carruseles )
                TarjetaProducto(producto = producto, onClick = { }) //

                //demas datos
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                   //nombre y precio
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = producto.nombre,
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            text = producto.precio,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    //descripcion centrada
                    Text(
                        text = producto.descripcion,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center // Centrado
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    //boton comprar y atras
                    Button(
                        onClick =  {carritoVM.añadirAlCarrito(producto) // Guarda el producto en el StateFlow
                                mostrardialogodeñadir=true},
                        modifier = Modifier
                            .fillMaxWidth()
                            .scale(escalaBoton), //aqui se aplica la escala de animacion
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("Comprar ahora")
                    }
                    // diálogo de añadir al carrito
                    if (mostrardialogodeñadir) {
                        AlertDialog(
                            onDismissRequest = { mostrardialogodeñadir = false },
                            confirmButton = {
                                TextButton(onClick = { mostrardialogodeñadir = false }) {
                                    Text("Aceptar")
                                }
                            },
                            title = { Text("¡Éxito!") },
                            text = { Text("Producto añadido correctamente al carrito.") }
                        )
                    }

                    TextButton(
                        onClick = navegaAtras,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Atrás",
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }

    }
}
