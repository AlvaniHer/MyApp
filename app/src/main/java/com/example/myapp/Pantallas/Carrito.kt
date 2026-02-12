package com.example.myapp.Pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapp.TiendaViewModel
import com.example.myapp.R

@Composable
fun Carrito(
    viewModel: TiendaViewModel,
    onVolver: () -> Unit,
    onConfirmar: () ->Unit,
    navegaADetalle:(Int) -> Unit,
    navegaAHome:() -> Unit
) {
    // Observamos la lista del carrito del ViewModel
    val listaCarrito by viewModel.carrito.collectAsState()

        Column(Modifier.padding()) {
            if (listaCarrito.isEmpty()) {
                // Si no hay nada, mostramos un mensaje
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    // La Column organiza los elementos uno DEBAJO de otro
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("El carrito está vacío")

                        Spacer(modifier = Modifier.height(16.dp)) // Espacio entre texto y botón
                        Button(
                            onClick = { navegaAHome() },
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                        ) {
                            Text(
                                "Volver a la tienda",
                                color = MaterialTheme.colorScheme.secondary
                                )
                        }
                    }
                }

            } else {
                //lista de productos seleccionados
                LazyColumn(Modifier.weight(1f)) {
                    items(listaCarrito) { producto ->
                        // Elegimos el carrusel correcto según el producto
                        val idImagen = when {
                            producto.carruselFotos.isNotEmpty() -> producto.carruselFotos[0]
                            producto.carruselVideos.isNotEmpty() -> producto.carruselVideos[0]
                            else -> producto.carruselClips[0]
                        }
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically

                            ) {
                                Image(
                                    painter = painterResource(id = idImagen),
                                    contentDescription = null,
                                    modifier = Modifier.size(60.dp)
                                            .clip(RoundedCornerShape(8.dp)), // las esquinas redondeadas
                                )
                                Column(
                                    Modifier
                                        .padding(start = 16.dp)
                                        .weight(1f))
                                {
                                    Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                                    Text("${producto.precio}€", color = MaterialTheme.colorScheme.secondary)
                                }
                                //iconos
                                Row {
                                    //lupa
                                    IconButton(onClick = { navegaADetalle(producto.id) }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.lupa), //mi vector personalizado
                                            contentDescription = "Ver detalle",
                                            tint = MaterialTheme.colorScheme.background
                                        )
                                    }
                                    //papelera
                                    IconButton(onClick = { viewModel.eliminarDelCarrito(producto.id) }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.papelera),
                                            contentDescription = "Eliminar",
                                            tint = MaterialTheme.colorScheme.background
                                        )
                                    }

                                }
                            }
                        }
                    }
                }
                //botones de confirmacion
                Button(
                    onClick = onConfirmar,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.secondary //letras sobre el boton
                    )
                ) {
                    Text("Comprar ahora")
                }


                //boton para finalizar o volver
                Button(
                    onClick = onVolver,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text(
                        "Volver a la tienda",
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
}


