package com.example.common.dto

import java.io.Serializable
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ActivationMessageDto(
        @field:NotNull
        val userId: Long,
        @field:NotBlank
        @field:Email
        val email: String) : Serializable