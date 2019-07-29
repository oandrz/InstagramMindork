package com.mindorks.bootcamp.instagram.utils.common

import com.mindorks.bootcamp.instagram.R
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.hasSize
import org.junit.Test

class ValidatorTest {

    @Test
    fun givenValidEmailAndValidPassword_whenValidate_shouldReturnSuccess() {
        val email = "testing@gmail.com"
        val password = "password"
        val validation = Validator.validateLoginFields(email, password)
        assertThat(validation, hasSize(2))
        assertThat(
            validation, contains(
                Validation(Validation.Field.EMAIL, Resource.success()),
                Validation(Validation.Field.PASSWORD, Resource.success())
            )
        )
    }

    @Test
    fun givenInvalidEmailAndValidPassword_whenValidate_shouldReturnEmailError() {
        val email = "test.com"
        val password = "password"
        val validations = Validator.validateLoginFields(email, password)
        assertThat(
            validations, contains(
                Validation(Validation.Field.EMAIL, Resource.error(R.string.login_error_invalidEmail)),
                Validation(Validation.Field.PASSWORD, Resource.success())
            )
        )
    }

    @Test
    fun givenValidEmailAndInvalidPassword_whenValidate_shouldReturnPasswordError() {
        val email = "test@gmail.com"
        val password = "p"
        val validations = Validator.validateLoginFields(email, password)
        assertThat(
            validations, contains(
                Validation(Validation.Field.EMAIL, Resource.success()),
                Validation(Validation.Field.PASSWORD, Resource.error(R.string.login_error_invalidPassword))
            )
        )
    }

    @Test
    fun givenInvalidEmailAndInvalidPassword_whenValidate_shouldReturnEmailAndPasswordError() {
        val email = "tes"
        val password = "pa"
        val validations = Validator.validateLoginFields(email, password)
        assertThat(
            validations, contains(
                Validation(Validation.Field.EMAIL, Resource.error(R.string.login_error_invalidEmail)),
                Validation(Validation.Field.PASSWORD, Resource.error(R.string.login_error_invalidPassword))
            )
        )
    }

    @Test
    fun givenInvalidNameValidEmailAndValidPassword_whenValidate_shouldReturnNameError() {
        val name = ""
        val email = "test@gmail.com"
        val password = "password"
        val validations = Validator.validateSignUpFields(name, email, password)
        assertThat(
            validations, contains(
                Validation(Validation.Field.NAME, Resource.error(R.string.signup_error_nameEmpty)),
                Validation(Validation.Field.EMAIL, Resource.success()),
                Validation(Validation.Field.PASSWORD, Resource.success())
            )
        )
    }
}