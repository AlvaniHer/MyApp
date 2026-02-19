package com.vani.myapp

import junit.framework.TestCase.assertEquals
import org.junit.Test

class LoginTest {

    @Test
    fun validarEmail_cuandoNoTieneArroba_retornaTrue() {
        //Dado que tenemos el ViewModel y un email sin @)
        val viewModel = TiendaViewModel()
        val emailInvalido = "usuario-gmail.com" //es una trampa para probar

        //cuando llamamos a la función de validar email (que vea si tiene @)
        viewModel.validaremail(emailInvalido)

        //entonces el error debe ser TRUE
        val resultado = viewModel.emailError.value
        assertEquals(true, resultado)
    }

    @Test
    fun validarEmail_cuandoEsCorrecto_retornaFalse() {
        //Un email que sí está bien
        val viewModel = TiendaViewModel()
        val emailValido = "admin@test.com"

        //validamos
        viewModel.validaremail(emailValido)

        //el error debe ser FALSE, porque no hay error
        val resultado = viewModel.emailError.value
        assertEquals(false, resultado)
    }
}

