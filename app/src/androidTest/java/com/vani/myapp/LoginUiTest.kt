package com.vani.myapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.vani.myapp.ui.Pantallas.Login
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.vani.myapp.viewmodel.LoginViewModel
import org.junit.Rule
import org.junit.Test
//prueba IU
class LoginUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun cuandoEmailEsInvalido_ElCampoMuestraEstadoDeError() {
        //iniciamos nuestra pantalla de Login
        composeTestRule.setContent {
            //el nombre de la función @Composable del Login
            Login(
                viewModel = LoginViewModel(),
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
    //test de logueo

    @Test
    fun cuandoLoginEsExitoso_LlamaAOnLoginOk() {
        var loginExitoso = false

        composeTestRule.setContent {
            Login(
                viewModel = LoginViewModel(),
                auth = Firebase.auth,
                //si el login funciona, cambiaremos esta variable a true
                onLoginOk = { loginExitoso = true },
                navegaARegistro = { }
            )
        }

        //un email real (a@a.com)
        composeTestRule.onNodeWithTag("campo_email")
            .performTextInput("a@a.com")

        //la contraseña
        composeTestRule.onNodeWithTag("campo_pass")
            .performTextInput("123456")

        //botón de entrar
        //el texto igual al del botón
        composeTestRule.onNodeWithText("Entrar").performClick()

        //se espera a que la variable sea true
        composeTestRule.waitUntil(5000) { loginExitoso }

        assert(loginExitoso)
    }
}