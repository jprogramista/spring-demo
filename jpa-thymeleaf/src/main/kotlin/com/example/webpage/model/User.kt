package com.example.webpage.model

import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(name = "email", columnNames = ["email"])])
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="USER_TYPE", discriminatorType = DiscriminatorType.STRING)
abstract class User(
        @field:Email @field:NotEmpty
        open val email: String,
        @field:NotEmpty
        open val password: String
) : BaseEntity<Long>()