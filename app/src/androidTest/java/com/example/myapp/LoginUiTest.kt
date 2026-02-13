package com.example.myapp

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.example.myapp.Pantallas.Login
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import org.junit.Rule
import org.junit.Test

class LoginUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun cuandoEmailEsInvalido_ElCampoMuestraEstadoDeError() {
        //iniciamos nuestra pantalla de Login
        composeTestRule.setContent {
            //el nombre de la función @Composable del Login
            Login(
                viewModel = TiendaViewModel(),
                auth = Firebase.auth, // conductor
                onLoginOk = { },      // destino, vacio en el test
                navegaARegistro = { } //mapa vacio en el test
            )
        }

        //el usuario escribe un email sin arroba
        composeTestRule.onNodeWithTag("campo_email")
            .performTextInput("usuario-sin-arroba")

        // se verifica que el campo detecto el error
        composeTestRule.onNodeWithText("El email debe contener una @") //texto que sale en mi if (error)
            .assertIsDisplayed()
        // O simplemente verificar que el texto sigue ahí pero con el estado de error activo
    }
}