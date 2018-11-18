package com.example.webpage.model

import javax.persistence.*

@Entity
@DiscriminatorValue("E")
class Employee(email: String, password: String,
               val division: String,
               @Embedded val address: Address? = null,
               @ManyToMany
               @JoinTable(name = "employee_role", joinColumns = [JoinColumn(name = "employee_id")], inverseJoinColumns = [JoinColumn(name = "role_id")])
               val roles: List<Role>? = null,
               @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
               @JoinColumn(name = "employee_id")
               val jobHistory: List<JobHistory>? = null
               ) : User(email, password) {

    fun copy() : Employee {
        val employee = Employee(email, password, division, address, roles, jobHistory)
        employee.id = id
        employee.createdAt = createdAt

        return employee
    }

    fun copy(newJobHistory: List<JobHistory>?) : Employee {
        val employee = Employee(email, password, division, address, roles, newJobHistory ?: jobHistory)
        employee.id = id
        employee.createdAt = createdAt

        return employee
    }
}
