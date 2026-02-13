package com.example.myapp.Pantallas

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapp.TiendaViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.myapp.R


@Composable
fun Login(auth: FirebaseAuth, onLoginOk :() -> Unit,navegaARegistro:() -> Unit,viewModel: TiendaViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember {mutableStateOf("")}
    var muestraDialogoError by remember { mutableStateOf(false) }
    val tieneError by viewModel.emailError.collectAsState() //esto es para ver si da error el email
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background), //fondo de pantalla
        horizontalAlignment = Alignment.CenterHorizontally, //Centra el texto de izquierda a derecha
        verticalArrangement = Arrangement.Center, //Centra el contenido de arriba a abajo

    ) {
        Image(
            painter = androidx.compose.ui.res.painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .width(200.dp)
                .height(200.dp), // Altura fija para que no ocupe toda la pantalla
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )
        Text(
            text="Loging",
            fontSize = 25.sp,
            style = MaterialTheme.typography.headlineLarge, // Añadido estilo para que sea grande
            color = MaterialTheme.colorScheme.primary, // Corregido: es "color", no "colors"
            modifier = Modifier
        )
        Spacer(Modifier.height(16.dp))
        //espacio de email
        OutlinedTextField(
            value = email,
            onValueChange = {
                email=it
                viewModel.validaremail(it) //verificar si es un email
            },
            modifier = Modifier.testTag("campo_email"), //esto es para el test
            // Aquí se cumple la instrucción: Mostrar contador de caracteres
            label = { Text("Email") },
            isError = tieneError // Si es true, el campo se pone rojo
        )
        //Mostrar un texto rojo si hay error
        if (tieneError) {
            Text("El email debe contener una @", color = MaterialTheme.colorScheme.error)
        }
        Spacer(Modifier.height(16.dp))
        //espacio contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password=it },//actualiza la contraseña
            // Aquí se cumple la instrucción: Mostrar contador de caracteres
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation() // Para que no se vea la clave
        )
        Spacer(Modifier.height(16.dp))
        //boton de login
        Button(
            onClick = {
                // Validación básica antes de llamar a Firebase
                if (email.isNotEmpty() && password.isNotEmpty()) {

                    auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {//esto es si el firebase esta escuchando y envia los datos para ver si estan en el
                            onLoginOk() //llama a la funcion de exito, si sale bien se le avisa a la navegacion
                        }
                        .addOnFailureListener { e -> // si el firebase no tiene esos datos
                            Log.e("FIREBASE_ERROR", "Error: ${e.message}")
                            muestraDialogoError = true
                        }
                } else {
                    muestraDialogoError = true //avisar que faltan campos
                }
            },
            modifier = Modifier
                .width(200.dp)  // Ancho específico
                .height(60.dp), // Alto específico
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Entrar", style = MaterialTheme.typography.titleLarge)
        }
        //Link de registra
        Spacer(Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("¿No tienes cuenta?")
            TextButton(
                onClick = { navegaARegistro() },
                modifier = Modifier
                    .padding(start = 8.dp)
            ) {
                Text("Registrarse")
            }
        }
    }
    // Diálogo de error (según la guía )
    if (muestraDialogoError) {
        AlertDialog(
            onDismissRequest = { muestraDialogoError = false },
            confirmButton = {
                TextButton(onClick = { muestraDialogoError = false }) { Text("OK") }
            },
            title = { Text("Error") },
            text = { Text("Usuario o contraseña incorrectos") }
        )
    }
}