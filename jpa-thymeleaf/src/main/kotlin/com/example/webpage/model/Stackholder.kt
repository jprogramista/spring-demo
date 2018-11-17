package com.example.webpage.model

import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Entity
@DiscriminatorValue("SH")
class Stackholder(email: String, password: String,
                  val division: String,
                  @NotNull @Min(1) val stockAmount: Int? = null
               ) : User(email, password) {

    fun copy() : Stackholder {
        val employee = Stackholder(email, password, division, stockAmount)
        employee.id = id
        employee.createdAt = createdAt

        return employee
    }
}
