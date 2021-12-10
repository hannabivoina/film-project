package com.example.myapplication.common

import androidx.fragment.app.Fragment

fun Fragment.regContract() : RegistrationContract? = requireActivity() as? RegistrationContract

interface RegistrationContract {
    fun toMainActivity()
    fun toSignInCard()
    fun toSignUpCard()
    fun fromSignInToSignUp()
    fun fromSignUpToSignIn()
}