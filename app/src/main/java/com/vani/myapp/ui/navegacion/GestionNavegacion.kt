package com.vani.myapp.ui.navegacion

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.vani.myapp.ui.Pantallas.Home
import com.vani.myapp.ui.Pantallas.Detalles
import com.vani.myapp.ui.Pantallas.Confirmacion
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vani.myapp.ui.Pantallas.Registro
import com.vani.myapp.ui.Pantallas.Carrito
import com.vani.myapp.ui.Pantallas.Login
import com.vani.myapp.ui.Pantallas.PantallaPortfolio
import com.vani.myapp.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.vani.myapp.viewmodel.CarritoViewModel
import com.vani.myapp.viewmodel.LoginViewModel
import com.vani.myapp.viewmodel.PortafolioViewModel


/**
 * Definición de los destinos principales para la barra de navegación adaptable.
 * * @property label Texto que se mostrará debajo del icono.
 * @property icon Icono visual representativo de la sección.
 * @property route La ruta lógica a la que apunta este destino.
 */
enum class AppDestinations(
    val label: String,
    val icon: Any, //para mis vectores propios
    val route: Routes
) {
    TIENDA("Tienda", Icons.Default.Home, Routes.Home),
    FOTOS("Portafolio Fotos", Icons.Default.Image, Routes.PortafolioFotos), // Añadido Routes.
    VIDEOS("Portafolio Videos", R.drawable.video, Routes.PortafolioVideos),
    CLIPS("Portafolio Clips", R.drawable.clips, Routes.PortafolioClips)
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
    //view model
    val loginVM: LoginViewModel = viewModel()
    val portafolioVM: PortafolioViewModel = viewModel()
    val carritoVM: CarritoViewModel = viewModel()
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
            is Routes.Carrito,
            is Routes.PortafolioFotos,
            is Routes.PortafolioVideos,
            is Routes.PortafolioClips ->true //en estas si se muestra
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
                                // Detectamos qué tipo de icono es para dibujarlo bien
                                val icono = destino.icon
                                Box(contentAlignment = Alignment.Center){
                                    if (icono is ImageVector) {
                                        Icon(
                                            imageVector = icono,
                                            contentDescription = destino.label,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    } else if (icono is Int) {
                                        Icon(
                                            painter = painterResource(id = icono),
                                            contentDescription = destino.label,
                                            modifier = Modifier.size(24.dp) // Iconos un pelín más pequeños para que se vea "fina"
                                        )
                                    }
                                }
                            },
                            alwaysShowLabel = true,
                            label = { Text(destino.label,textAlign = TextAlign.Center) },
                            selected = rutaActual == destino.route, //con esto se comprueba la ruta para señalarla
                            onClick = {
                                if (rutaActual != destino.route) {
                                    // Creamos una copia de la pila actual
                                    val nuevaPila = navegacionPila.toMutableList()

                                    if (destino.route is Routes.Home) {
                                        //Si pulsamos Home, limpiamos todo y dejamos solo el Home
                                        nuevaPila.clear()
                                        nuevaPila.add(Routes.Home)
                                    } else {
                                        // Si estamos en un portafolio y pulsamos OTRO portafolio, reemplazamos el último
                                        if (nuevaPila.lastOrNull() !is Routes.Home) {
                                            nuevaPila.removeLastOrNull()
                                        }
                                        // Añadimos el nuevo destino (Fotos, Vídeos o Clips)
                                        nuevaPila.add(destino.route)
                                    }

                                    // Aplicamos los cambios a la pila real
                                    navegacionPila.clear()
                                    navegacionPila.addAll(nuevaPila)
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
                                viewModel = loginVM, //se pasa el viewmodel
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
                                viewModel = loginVM,
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
                                miViewModel=portafolioVM,
                                navegaADetalle = { id ->
                                    navegacionPila.add(Routes.Detalles(id))
                                },
                                navegaACarrito={navegacionPila.add(Routes.Carrito)}
                                )

                        }
                        //pantalla detalles
                        is Routes.Detalles -> NavEntry(key) {
                            Detalles(
                                portafolioVM=portafolioVM,
                                carritoVM=carritoVM,
                                id = key.id,
                                navegaAtras = { navegacionPila.removeLastOrNull() } //volver a home para seguir viendo los catalogos
                            )
                        }
                        is Routes.Carrito -> NavEntry(key){
                            Carrito(
                                viewModel = carritoVM,
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
                                viewModel = carritoVM,
                                navegaAHome={
                                    // Después de confirmar, vaciamos el carrito
                                    carritoVM.limpiarCarrito()
                                    navegacionPila.add(Routes.Home)

                                }

                            )
                        }
                        //portafolio fotos
                        is Routes.PortafolioFotos-> NavEntry(key){
                            PantallaPortfolio( tipo="fotos")
                        }
                        //portafolio video
                        is Routes.PortafolioVideos -> NavEntry(key){
                            PantallaPortfolio(tipo="videos")
                        }
                        //portafolio clips
                        is Routes.PortafolioClips -> NavEntry(key){
                            PantallaPortfolio(tipo="clips")
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

@Composable
fun PantallaPortfolio(tipo: String) {
    TODO("Not yet implemented")
}

