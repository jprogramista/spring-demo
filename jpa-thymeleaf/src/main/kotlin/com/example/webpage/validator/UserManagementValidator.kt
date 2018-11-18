package com.example.webpage.validator

import com.example.webpage.dto.CreateUserDto
import com.example.webpage.facade.UserManagementFacade
import org.springframework.validation.Errors
import org.springframework.validation.Validator

class UserManagementValidator(val userManagementFacade: UserManagementFacade) : Validator {

    override fun validate(target: Any, errors: Errors) {
        val userDto = target as CreateUserDto
        if (userDto.password != null && userDto.password != userDto.confirmPassword) {
            errors.rejectValue("password", "error.passAndConfirm")
        }

        userDto.address?.country?.let {
            if (it.isNotBlank()) {
                userManagementFacade.getCountry(it) ?: errors.rejectValue("address.country", "error.notKnownCountry")
            }
        }

        userDto.roles?.forEach {
            userManagementFacade.getRole(it) ?: errors.rejectValue("roles",  "error.rolesUnknown", arrayOf(it),"Unknown role")
        }
    }

    override fun supports(clazz: Class<*>): Boolean {
        val assignableFrom = CreateUserDto::class.java.isAssignableFrom(clazz)
        return assignableFrom
    }
}