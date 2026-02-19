package com.vani.myapp.Pantallas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vani.myapp.componente.TarjetaProducto
import com.vani.myapp.TiendaViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


/**
 * Pantalla principal de la aplicación (Catálogo).
 * * Esta función se encarga de:
 * 1. Observar la lista de productos que ofrece el (TiendaViewModel).
 * 2. Ejecutar una animación de entrada suave para toda la lista al abrir la app.
 * 3. Mostrar un título de bienvenida y una lista deslizable (LazyColumn) de servicios.
 * * @param miViewModel El proveedor de datos de la tienda.
 * @param navegaADetalle Acción que se ejecuta al pulsar un producto, enviando su ID.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(miViewModel: TiendaViewModel = viewModel(),navegaADetalle: (Int) -> Unit, navegaACarrito:() -> Unit) {
    // lista de productos que viene del ViewModel
    val listaDeProductos by miViewModel.productos.collectAsState()
    var visible by remember { mutableStateOf(false) } //estado para controlar la animacion

    // Al iniciar, se activa la visibilidad
    LaunchedEffect(Unit) { visible = true }
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(1000)) // Aparece suavemente en 1 segundo
        ) {
            LazyColumn( //LazyColumn para que la pantalla tenga scroll vertical
                modifier = Modifier
                    .fillMaxSize()
                    .padding()
            ) {
                //lista de tarjetas (productos)
                // Por cada producto en el ViewModel, creamos una TarjetaProducto
                items(listaDeProductos) { producto ->
                    TarjetaProducto(
                        producto = producto,
                        onClick = { idRecibido ->  //para que vaya a detalle
                            navegaADetalle(idRecibido)
                        }
                    )
                }

                // Espacio final para que el último producto no quede tapado por la barra
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
}


