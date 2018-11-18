package com.example.webpage.model

import javax.persistence.Embeddable
import javax.persistence.ManyToOne

@Embeddable
data class Address(val streetFirstLine: String? = null,
                   val streetSecondaryLine: String? = null,
                   val postalCode: String? = null,
                   val city : String? = null,
                   @ManyToOne val country: Country? = null)