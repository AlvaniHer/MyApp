package com.vani.myapp

import androidx.lifecycle.ViewModel
import com.vani.myapp.data.Producto
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Clase que actúa como el "cerebro" o administrador de datos de la tienda VaniShoots.
 * * Su función principal es preparar y proteger la lista de servicios (fotos, vídeos, clips)
 * para que las pantallas puedan leerlos sin riesgo de que los datos se pierdan o se dañen
 * al girar el móvil o cambiar de sección.
 */
class TiendaViewModel : ViewModel() {
   /**
    * Lista interna y privada de los productos.
    * Se usa [MutableStateFlow] para que la lista pueda actualizarse si fuera necesario.
    * El guion bajo (_) indica que es una variable "secreta" que solo este archivo puede modificar.
    */
   // El estado puede ser: "INICIO", "CARGANDO", "EXITO" o un mensaje de ERROR
   private val _estadoLogin = MutableStateFlow("INICIO")
    val estadoLogin = _estadoLogin.asStateFlow()
    // Función para loguear (con Firebase)
    fun loginUsuario(auth: FirebaseAuth, email: String, pass: String) {
        _estadoLogin.value = "CARGANDO"
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _estadoLogin.value = "EXITO"
                } else {
                    _estadoLogin.value = task.exception?.message ?: "Error"
                }
            }
    }
   private val _emailError = MutableStateFlow(false)
    val emailError = _emailError.asStateFlow()
    fun validaremail(email: String){
        //si el email no tiene @ no es un corre
        _emailError.value = !email.contains("@")

    }
    fun registrarUsuario(
        auth: FirebaseAuth,
        email: String,
        password: String,
        confPassword: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        // Validamos primero: ¿Son iguales las contraseñas?
        if (password != confPassword) {
            onFailure("Las contraseñas no coinciden")
            return
        }
        // Validamos: ¿Es muy corta?
        if (password.length < 6) {
            onFailure("La contraseña debe tener al menos 6 caracteres")
            return
        }
        //si esta bien
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception?.message ?: "Error desconocido")
                }
            }
    }
    // Lista de productos (Lógica de negocio)
    private val _productos = MutableStateFlow(
        listOf(
            //fotos
            Producto(
                id = 0,
                nombre = "Sesión de Fotos",
                precio = "20€",
                carruselFotos = listOf(
                    R.drawable.f1,
                    R.drawable.f2,
                    R.drawable.f3,
                    R.drawable.f4
                ), //lista fotos
                descripcion = "Sesion fotografica profesional, en alta resolucion, ideal para eventos o retratos personales. Pack:20 fotos(editadas)."
            ),
            // Videos
            Producto(
                id = 1,
                nombre = "Muestra de Videos",
                precio = "50€",
                carruselVideos = listOf(
                    R.drawable.v1,
                    R.drawable.v2,
                    R.drawable.v3
                ), // Lista videos
                descripcion = "Toma y edicion de video cinematofrafico con correcion de color y montaje de audio profesional."
            ),
            //Clips
            Producto(
                id = 2,
                nombre = "Clips para Redes",
                precio = "20€",
                carruselClips = listOf(
                    R.drawable.c1,
                    R.drawable.c2,
                    R.drawable.c3,
                    R.drawable.c4,
                    R.drawable.c5
                ), // Lista clips
                descripcion = "Clips cortos optimizados para redes sociales (Reels, TikTok) con transiciones dinamicas. (30s max)."
            ),
        )
    )
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

    /**
     * Canal público de datos que observan las pantallas
     * Es un [StateFlow] de solo lectura, lo que garantiza que la interfaz solo
     * pueda "ver" los productos pero no borrarlos ni cambiarlos directamente.
     */
    val productos: StateFlow<List<Producto>> = _productos
}