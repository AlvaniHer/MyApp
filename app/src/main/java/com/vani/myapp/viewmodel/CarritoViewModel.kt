package com.vani.myapp.viewmodel

import androidx.lifecycle.ViewModel
import com.vani.myapp.modelo.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CarritoViewModel: ViewModel(){
    private val _carrito = MutableStateFlow<List<Producto>>(emptyList())
    val carrito = _carrito.asStateFlow()
    // Función para el botón Comprar
    fun añadirAlCarrito(producto: Producto) {
        _carrito.update { listaActual: List<Producto> ->
            listaActual + producto
        }
    }
    fun eliminarDelCarrito(productoId: Int) {
        _carrito.value = _carrito.value.filter { it.id != productoId }
    }
    fun limpiarCarrito() {
        _carrito.value = emptyList() //vaciar la StateFlow
    }
}