package com.vani.myapp.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel: ViewModel(){
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
    //resgistro
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
}