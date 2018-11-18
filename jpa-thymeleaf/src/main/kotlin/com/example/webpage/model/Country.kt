package com.example.webpage.model

import javax.persistence.*

@Entity
@Table(name = "COUNTRY", uniqueConstraints = [UniqueConstraint(name = "unique_name", columnNames = ["name"])])
data class Country(
        @Id val abbr: String, val name: String)