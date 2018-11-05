package com.example.demo.model

import com.example.demo.dto.UserDto
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Email

@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(name = "email", columnNames = ["email"])])
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        @Email
        val email: String,
        val password: String? = null,
        @JsonDeserialize(using = LocalDateTimeDeserializer::class)
        @JsonSerialize(using = LocalDateTimeSerializer::class)
        @CreationTimestamp
        val created: LocalDateTime,
        val enabled: Boolean,
        @JsonIgnore
        @Enumerated(EnumType.STRING)
        val oauthServiceType: OAuthServiceType? = null,
        @JsonIgnore
        val oauthId: String? = null) {

        @OneToMany(mappedBy = "user")
        val details: MutableList<UserDetail> = mutableListOf()

    fun toDto() : UserDto {
        return UserDto(this.id ?: 0, this.email, this.created, this.details.map { detail -> detail.toDto() })
    }
}