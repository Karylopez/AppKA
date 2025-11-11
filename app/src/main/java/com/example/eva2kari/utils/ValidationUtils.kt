package com.example.eva2kari.utils

object ValidationUtils {

    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"
        return email.matches(emailRegex.toRegex())
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    fun isValidUsername(username: String): Boolean {
        return username.length >= 3
    }

    fun isNotEmpty(text: String): Boolean {
        return text.isNotBlank()
    }

    fun validateLoginFields(username: String, password: String): ValidationResult {
        return when {
            username.isBlank() -> ValidationResult.Error("El nombre de usuario es requerido")
            password.isBlank() -> ValidationResult.Error("La contraseña es requerida")
            !isValidUsername(username) -> ValidationResult.Error("El nombre de usuario debe tener al menos 3 caracteres")
            !isValidPassword(password) -> ValidationResult.Error("La contraseña debe tener al menos 6 caracteres")
            else -> ValidationResult.Success
        }
    }

    sealed class ValidationResult {
        object Success : ValidationResult()
        data class Error(val message: String) : ValidationResult()
    }
}