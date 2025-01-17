package com.mindorks.bootcamp.instagram.utils.common

import com.mindorks.bootcamp.instagram.R
import java.util.regex.Pattern

object Validator {
    private val EMAIL_ADDRESS = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
    )

    private const val MIN_PASSWORD_LENGTH = 6

    fun validateLoginFields(email: String?, password: String?): List<Validation> =
        ArrayList<Validation>().apply {
            when {
                email.isNullOrBlank() ->
                    add(Validation(Validation.Field.EMAIL,
                        Resource.error(R.string.login_error_emailEmpty)))
                !EMAIL_ADDRESS.matcher(email).matches() ->
                    add(Validation(Validation.Field.EMAIL,
                        Resource.error(R.string.login_error_invalidEmail)))
                else ->
                    add(Validation(Validation.Field.EMAIL,
                        Resource.success()))
            }

            when {
                password.isNullOrBlank() ->
                    add(Validation(Validation.Field.PASSWORD,
                        Resource.error(R.string.login_error_passwordEmpty)))
                password.length < MIN_PASSWORD_LENGTH ->
                    add(Validation(Validation.Field.PASSWORD,
                        Resource.error(R.string.login_error_invalidPassword)))
                else ->
                    add(Validation(Validation.Field.PASSWORD,
                        Resource.success()))
            }
        }

    fun validateSignUpFields(name: String?, email: String?, password: String?): List<Validation> =
        ArrayList<Validation>().apply {
            when {
                name.isNullOrBlank() ->
                    add(Validation(Validation.Field.NAME,
                        Resource.error(R.string.signup_error_nameEmpty)))
                else ->
                    add(Validation(Validation.Field.NAME,
                        Resource.success()))
            }

            when {
                email.isNullOrBlank() ->
                    add(Validation(Validation.Field.EMAIL,
                        Resource.error(R.string.signup_error_emailEmpty)))
                !EMAIL_ADDRESS.matcher(email).matches() ->
                    add(Validation(Validation.Field.EMAIL,
                        Resource.error(R.string.signup_error_invalidEmail)))
                else ->
                    add(Validation(Validation.Field.EMAIL,
                        Resource.success()))
            }

            when {
                password.isNullOrBlank() ->
                    add(Validation(Validation.Field.PASSWORD,
                        Resource.error(R.string.signup_error_passwordEmpty)))
                password.length < MIN_PASSWORD_LENGTH ->
                    add(Validation(Validation.Field.PASSWORD,
                        Resource.error(R.string.signup_error_invalidPassword)))
                else ->
                    add(Validation(Validation.Field.PASSWORD,
                        Resource.success()))
            }
        }


    fun validateUpdateProfileField(name: String?, tagline: String?): List<Validation> =
        ArrayList<Validation>().apply {
            when {
                name.isNullOrBlank() ->
                    add(Validation(Validation.Field.NAME, Resource.error()))
                else ->
                    add(Validation(Validation.Field.NAME, Resource.success()))
            }

            when {
                tagline.isNullOrBlank() ->
                    add(Validation(Validation.Field.TAGLINE, Resource.error()))
                else ->
                    add(Validation(Validation.Field.TAGLINE, Resource.success()))
            }
        }
}

data class Validation(val field: Field, val resource: Resource<Int>) {

    enum class Field {
        EMAIL,
        PASSWORD,
        NAME,
        TAGLINE
    }
}