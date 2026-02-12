package com.example.myapp.Pantallas

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapp.R
import com.example.myapp.TiendaViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun Registro(
    auth: FirebaseAuth,
    viewModel: TiendaViewModel,
    registroOK: () -> Unit,      // Para ir al Home al terminar
    onNavegaAtras: () -> Unit    // Por si quiere volver al Login
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confPassword by remember { mutableStateOf("") } // Campo extra para confirmar
    val context =
        androidx.compose.ui.platform.LocalContext.current//solo para ver el contexto o errores
    val emailError by viewModel.emailError.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = androidx.compose.ui.res.painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .width(100.dp)
                .height(100.dp), // Altura fija para que no ocupe toda la pantalla
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )
        Text(
            text="Crear Cuenta",
            fontSize = 25.sp,
            style = MaterialTheme.typography.headlineLarge, // Añadido estilo para que sea grande
            color = MaterialTheme.colorScheme.primary, // Corregido: es "color", no "colors"
            modifier = Modifier
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                viewModel.validaremail(it)
            },
            label = { Text("Email") },
            isError = emailError,        // Si el profesor dice que está mal, la caja se pone roja
            supportingText = {           // Ponemos un mensajito debajo si hay error
                if (emailError) {
                    Text("El email debe contener un @", color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(8.dp))

        // Campo de confirmación (Seguridad extra)
        OutlinedTextField(
            value = confPassword,
            onValueChange = { confPassword = it },
            label = { Text("Confirmar Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.registrarUsuario(
                    auth = auth,
                    email = email,
                    password = password,
                    confPassword = confPassword,
                    onSuccess = {
                        Log.d("REGISTRO", "¡Éxito!")
                        registroOK() // Función de navegación
                    },
                    onFailure = { error ->
                        Log.e("REGISTRO", "Fallo: $error")
                        // Aquí podrías mostrar un Toast o un mensaje en pantalla
                        android.widget.Toast.makeText(
                            context,
                            "Error: $error",
                            android.widget.Toast.LENGTH_LONG
                        ).show()
                    }
                )
            },
            modifier = Modifier.width(200.dp)
        ) {
            Text("Registrarse")
        }

        Button(
            onClick = onNavegaAtras,
            modifier = Modifier.width(200.dp)
        ) {
            Text("Cancelar")
        }
    }
}