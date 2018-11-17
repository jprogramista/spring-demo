package com.example.webpage.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "jobs")
class JobHistory(
        @Id @GeneratedValue val id: Long? = null,
        @NotEmpty val name: String,
        val description: String? = null
)
