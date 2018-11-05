package com.example.demo.dto

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import org.hibernate.validator.constraints.Length
import java.time.LocalDateTime
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty


data class UserDto(val id: Long,
                   val email: String?,
                   @JsonDeserialize(using = LocalDateTimeDeserializer::class)
                   @JsonSerialize(using = LocalDateTimeSerializer::class)
                   val created: LocalDateTime,
                   val details: List<UserDetailDto>)

data class UserDetailDto(val name: String, val value: String)

data class SignUpDto(
        @field:NotEmpty
        @field:Email
        val email: String = "",
        @field:NotEmpty
        @field:Length(min = 6, max = 24)
        val password: String = "")