package com.vani.myapp.data

/**
 * Datos que representa un servicio o paquete de la tienda VaniShoots.
 *
 * Esta clase se encarga de agrupar toda la información que necesita un producto
 * para mostrarse en la aplicación, desde sus textos básicos hasta las listas
 * de imágenes para los carruseles visuales.
 *
 * @property id Número único que identifica al producto
 * @property nombre El título del servicio
 * @property precio El costo del servicio guardado como texto para facilitar su visualización.
 * @property descripcion Texto detallado que explica qué incluye el paquete contratado.
 * @property carruselFotos Lista de recursos  de imágenes para la galería principal.
 * @property carruselClips Lista de recursos de imágenes para la sección de clips rápidos.
 * @property carruselVideos Lista de recursos que sirven como miniaturas para los vídeos largos.
 */
data class Producto(
    val id: Int,
    val nombre: String,
    val precio: String,
    val descripcion: String,
    // Lista de imágenes para que el carrusel funcione
    val carruselFotos: List<Int> = emptyList(),  // Lista fotos
    val carruselClips: List<Int> = emptyList(),  // lista clips
    val carruselVideos: List<Int> = emptyList()  // lista videos (miniaturas)
)