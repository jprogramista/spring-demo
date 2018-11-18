package com.example.webpage.model

import javax.persistence.*

@Entity
@Table(name = "roles", uniqueConstraints = [UniqueConstraint(name = "unique_role_name", columnNames = ["name"])])
class Role(
        @Id @GeneratedValue val id: Long? = null,
        val name: String
)