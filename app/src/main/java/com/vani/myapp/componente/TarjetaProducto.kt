package com.vani.myapp.componente


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.vani.myapp.data.Producto
import kotlinx.coroutines.delay

/**
 * Se crea una tarjeta visual (Card) para mostrar de forma atractiva la información de un producto.
 * * Este componente incluye una animación de entrada que hace que el contenido se
 * despliegue suavemente una vez que la tarjeta aparece en pantalla, cumpliendo
 * con los requisitos de animaciones dinámicas.
 * * @param producto El objeto con los datos del servicio (nombre, precio, fotos...).
 * @param onClick Función que se ejecuta al pulsar la tarjeta para navegar al detalle.
 */


//diseño de cada card
@Composable
fun TarjetaProducto(producto: Producto, onClick: (Int) -> Unit) {
    //animación de entrada
    var visible by remember { mutableStateOf(false) }

    //luego de que cargue la tarjeta
    LaunchedEffect(Unit) {
        delay(300) // Espera un poquito antes de empezar
        visible = true
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onClick(producto.id)
            }, //para que sea clickeable y vaya a detalle
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(20.dp) //esquinas redondeadas

    ) {
        //AnimatedVisibility para que el contenido aparezca suavemente
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(800)) + expandVertically()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                //titulo y precio
                Text(text = producto.nombre, style = MaterialTheme.typography.headlineSmall)
                Text(text = producto.precio, color = MaterialTheme.colorScheme.secondary)

                Spacer(modifier = Modifier.height(10.dp)) //espacio

                // 2. Carrusel de FOTOS
                if (producto.carruselFotos.isNotEmpty()) {
                    Text("Galería de Fotos:", style = MaterialTheme.typography.labelLarge)
                    CarruselModular(producto.carruselFotos)
                }

                // 3. Carrusel de VIDEOS
                if (producto.carruselVideos.isNotEmpty()) {
                    Text("Muestras de Video:", style = MaterialTheme.typography.labelLarge)
                    CarruselModular(producto.carruselVideos)
                }

                // 4. Carrusel de CLIPS
                if (producto.carruselClips.isNotEmpty()) {
                    Text("Clips para Redes:", style = MaterialTheme.typography.labelLarge)
                    CarruselModular(producto.carruselClips)
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

/**
 * Componente de interfaz reutilizable que muestra una fila horizontal de imágenes deslizables.
 * * @param imagenes Lista de identificadores de recursos visuales a mostrar en el carrusel.
 */
@Composable
fun CarruselModular(imagenes: List<Int>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        items(imagenes) { imagen ->
            Image(
                painter = painterResource(id = imagen),
                contentDescription = null,
                modifier = Modifier.size(120.dp).clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop //recorta la foto oara que siempre quede centrada y bien
            )
        }
    }
}