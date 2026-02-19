package com.vani.myapp.ui.Pantallas

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ListResult
import android.net.Uri
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items // Asegúrate de que este NO esté en rojo
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.RoundedCornerShape
import coil.compose.AsyncImage
import com.vani.myapp.ui.componente.ItemVideo

@Composable
fun PantallaPortfolio(tipo: String) {
    var listaUrls by remember { mutableStateOf<List<String>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }

    LaunchedEffect(tipo) {
        cargando = true
        // Usamos la instancia directa para evitar el error 'Unresolved reference storage'
        val storage: FirebaseStorage = FirebaseStorage.getInstance()
        val referencia: StorageReference = storage.reference.child("portfolio/$tipo")

        referencia.listAll().addOnSuccessListener { resultado: ListResult ->
            val fotosDeFirebase: List<StorageReference> = resultado.items
            val urlsTemporales = mutableListOf<String>()

            if (fotosDeFirebase.isEmpty()) {
                cargando = false
            } else {
                fotosDeFirebase.forEach { item: StorageReference ->
                    item.downloadUrl.addOnSuccessListener { uri: Uri ->
                        urlsTemporales.add(uri.toString())
                        // IMPORTANTE: Usamos fotosDeFirebase.size (sin paréntesis)
                        if (urlsTemporales.size == fotosDeFirebase.size) {
                            listaUrls = urlsTemporales.toList()
                            cargando = false
                        }
                    }.addOnFailureListener {
                        cargando = false
                    }
                }
            }
        }.addOnFailureListener {
            cargando = false
        }
    }

    if (cargando) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Este 'items' es de androidx.compose.foundation.lazy.grid.items
            items(listaUrls) { url ->
                Card(
                    modifier = Modifier.padding(8.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    //esto es par que se vea el video o la foto
                    val esVideo = url.contains(".mp4") || url.contains(".mov") || url.contains("video", ignoreCase = true)

                    if (esVideo) {
                        ItemVideo(url = url)
                    } else {
                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}