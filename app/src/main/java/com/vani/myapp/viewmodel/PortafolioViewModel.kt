package com.vani.myapp.viewmodel

import androidx.lifecycle.ViewModel
import com.vani.myapp.R
import com.vani.myapp.modelo.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class PortafolioViewModel: ViewModel(){
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
    val productos: StateFlow<List<Producto>> = _productos
}