package com.example.myapp.navegacion

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.myapp.Pantallas.Home
import com.example.myapp.Pantallas.Detalles
import com.example.myapp.Pantallas.Confirmacion
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapp.TiendaViewModel
import com.example.myapp.Pantallas.Registro
import com.example.myapp.Pantallas.Carrito
import com.example.myapp.Pantallas.Login
import com.example.myapp.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


/**
 * Definición de los destinos principales para la barra de navegación adaptable.
 * * @property label Texto que se mostrará debajo del icono.
 * @property icon Icono visual representativo de la sección.
 * @property route La ruta lógica a la que apunta este destino.
 */
enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
    val route: Routes?
) {
    TIENDA("Tienda", Icons.Default.Home, Routes.Home),
    FOTOS("Fotos", Icons.Default.Image, null), //estos no van a ningun lado
    CAMARA("Cámara", Icons.Default.PhotoCamera, null),
    CONFIGURACION("Ajustes", Icons.Default.Settings, null)
    // Aqui es donde se añaden mas destinos si tuviera mas
}
/**
 * Gestiona el sistema de navegación principal de la aplicación VaniShoots.
 * * Esta función es responsable de:
 * 1.Mantener la "pila" de navegación (el historial de pantallas visitadas).
 * 2.Mostrar una barra superior (TopAppBar) que cambia de título automáticamente.
 * 3.Controlar el botón de retroceso para que el usuario pueda volver atrás.
 * 4.Conectar cada ruta (Home, Detalles, Confirmación) con su pantalla correspondiente.
 * 5.Implementar NavigationSuiteScaffold para una navegación adaptable  a móvil o tablet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionNavegacion() {
    val auth= Firebase.auth //instancia de firebase
    val vm: TiendaViewModel=viewModel() //view model
    //pila `para la navegacion, inicia en el home
    val navegacionPila = rememberNavBackStack(Routes.Login)
    val rutaActual =navegacionPila.lastOrNull() //obtenemos la ruta actual para saber el titulo
    //se necesita saber qué destino de la barra está seleccionado
    val currentDestination = if (rutaActual is Routes.Home) AppDestinations.TIENDA else null // Si la ruta actual es Home, seleccionamos el ítem de Tienda
   //variable para mostrar el carrito
    val mostrarcarrito = when (rutaActual){
            is Routes.Login,
            is Routes.Registro,
            is Routes.Carrito,
            is Routes.Confirmacion -> false // en todas estas no
            else ->true //en las demas si
    }
    //variable para mostrar la top bar
    val mostrarbarras =when (rutaActual){
            is Routes.Home,
            is Routes.Detalles,
            is Routes.Carrito ->true //en estas si se muestra
            else -> false //las demas no
    }
    /**
     * Configuración de colores para los elementos individuales de la barra de navegación.
     * Se extrae a una variable para evitar errores de contexto composable en la definición de items.
     */
    //variable para los colores de la barra (ya que navigationsuitedefauls es no composable)
    val coloresBarra = NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            indicatorColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),//burbuja detras del icono
            selectedIconColor = MaterialTheme.colorScheme.primary, //color del icono
            unselectedIconColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f), //color de los iconos que no esten pulsados(aqui solo tengo uno)
            selectedTextColor = MaterialTheme.colorScheme.primary,//color del texto debajo
        ),

    )
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { //la barra de arriba
            if (mostrarbarras){
                TopAppBar(
                    title = {
                        // título dinámico
                        val titulo = when (rutaActual) {
                            is Routes.Login -> "Login"
                            is Routes.Registro -> "Registro"
                            is Routes.Home -> "VaniShoots"
                            is Routes.Detalles -> "Detalles del Servicio"
                            is Routes.Carrito -> "Lista de Compra"
                            is Routes.Confirmacion -> "Finalizar Compra"
                            else -> "VaniShoots"
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if(rutaActual is Routes.Home){
                                Image(
                                    painter = painterResource(id = R.drawable.logo), // Reemplaza por tu logo
                                    contentDescription = "Logo App",
                                    modifier = Modifier
                                        .size(70.dp) // Ajusta el tamaño que quieras
                                        .padding(end = 8.dp) // Espacio entre logo y texto
                                )
                            }

                            Text(titulo)
                        }
                    },
                    navigationIcon = {
                        //muestra flecha atrás si NO estamos en Home
                        if (rutaActual !is Routes.Home && rutaActual !is Routes.Login) {
                            IconButton(onClick = { navegacionPila.removeLastOrNull() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Regresar"
                                )
                            }
                        }
                    },
                    actions = {
                        //boton del carrito
                        if (mostrarcarrito){
                            IconButton(onClick ={navegacionPila.add(Routes.Carrito)} ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.carrito),
                                    contentDescription = "Ir al carrito",
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        }
                    },
                    // Aplicamos los colores del Theme a la barra
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }

    ) { innerPadding ->
        val paddingFinal = if (mostrarbarras) innerPadding else PaddingValues(0.dp) //para las paginas que no llevan barra por lo menos tengan un minimo de espacio y no se monte en los bordes
        NavigationSuiteScaffold( //barra de abajo para que se adapte si se gira el movil
            modifier = Modifier.padding(paddingFinal,),

            // Para que la barra sea de otro color
            navigationSuiteColors = NavigationSuiteDefaults.colors(
                navigationBarContainerColor = MaterialTheme.colorScheme.background,
            ),
            navigationSuiteItems = {
                if (mostrarbarras){
                    // Solo mostramos los destinos principales (como el Home)
                    AppDestinations.entries.forEach { destino ->
                        item(
                            icon = {
                                Icon(
                                    destino.icon,
                                    contentDescription = destino.label,
                                    modifier = Modifier.size(24.dp) // Iconos un pelín más pequeños para que se vea "fina"
                                )
                            },
                            label = { Text(destino.label) },
                            selected = destino == currentDestination,
                            onClick = {
                                // Si el usuario pulsa "Tienda" y no está en Home, lo llevamos allí
                                if (rutaActual !is Routes.Home) {
                                    // Limpiamos la pila hasta volver al Home
                                    while (navegacionPila.size > 1) {
                                        navegacionPila.removeLastOrNull()
                                    }
                                }
                            },
                            colors = coloresBarra
                        )
                    }
                }
            }
        ) {
            NavDisplay(
                backStack = navegacionPila,
                onBack = { navegacionPila.removeLastOrNull() }, //cuando se va hacia atras
                entryProvider = { key ->
                    when (key) {
                        //Pantalla Login
                        is Routes.Login -> NavEntry(key) {
                            Login(
                                auth = auth, //se pasa la firebase
                                viewModel = vm, //se pasa el viewmodel
                                onLoginOk = {
                                    navegacionPila.add(Routes.Home) //si se hace bien el login va a home
                                },
                                navegaARegistro = {
                                    navegacionPila.add(Routes.Registro)
                                }
                            )
                        }
                        //Pantalla registro
                        is Routes.Registro-> NavEntry(key) {

                            Registro(
                                auth=auth,
                                viewModel = vm,
                                registroOK ={
                                    navegacionPila.add(Routes.Home)
                                },
                                onNavegaAtras = {
                                    navegacionPila.removeLastOrNull()
                                }
                            )
                        }
                        //pantalla home
                        is Routes.Home -> NavEntry(key) {
                            Home(
                                navegaADetalle = { id ->
                                    navegacionPila.add(Routes.Detalles(id))
                                },
                                navegaACarrito={navegacionPila.add(Routes.Carrito)}
                                )

                        }
                        //pantalla detalles
                        is Routes.Detalles -> NavEntry(key) {
                            Detalles(
                                id = key.id,
                                navegaAtras = { navegacionPila.removeLastOrNull() } //volver a home para seguir viendo los catalogos
                            )
                        }
                        is Routes.Carrito -> NavEntry(key){
                            Carrito(
                                viewModel = vm,
                                onConfirmar ={navegacionPila.add(Routes.Confirmacion)}, //Para confirmar la compra
                                onVolver = { navegacionPila.removeLastOrNull() },
                                navegaADetalle = {idRecibido ->
                                   navegacionPila.add(Routes.Detalles(id=idRecibido))
                                },
                                navegaAHome = {
                                    navegacionPila.removeLastOrNull()
                                }
                            )
                        }
                        //pantalla confirmar compra
                        is Routes.Confirmacion -> NavEntry(key) {
                            Confirmacion(
                                navegaAHome={navegacionPila.add(Routes.Home)}

                            )
                        }

                        else -> NavEntry(Routes.Error) {
                            Text("Error")
                        }
                    }

                }
            )
        }

    }

}

