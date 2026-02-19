package com.vani.myapp.navegacion

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Define las rutas o destinos posibles dentro de la aplicación VaniShoots.
 * * Se utiliza una [sealed class] (clase sellada) para que el sistema de navegación
 * sepa exactamente cuáles son las únicas pantallas que existen, evitando errores
 * al intentar ir a un lugar que no está en el mapa.
 */
@Serializable
sealed class Routes: NavKey{

    @Serializable
    data object Login:Routes()

    @Serializable
    data object Registro: Routes()

    @Serializable
    data object Home: Routes()

    /** * Representa la pantalla de información detallada.
     * @param id El identificador del producto que queremos ver. Se pasa como
     * parámetro para que la pantalla sepa qué fotos y textos cargar.
     */
    @Serializable
    data class Detalles(val id: Int): Routes()

    @Serializable
    data object PortafolioFotos:Routes()

    @Serializable
    data object PortafolioVideos:Routes()
    @Serializable
    data object  PortafolioClips: Routes()
    @Serializable
    data object Carrito: Routes()
    @Serializable
    data object Confirmacion: Routes()

    /** * Ruta de seguridad que se muestra si algo falla o si el usuario intenta
     * acceder a una dirección que no existe.
     */
    @Serializable
    data object Error: Routes()
}