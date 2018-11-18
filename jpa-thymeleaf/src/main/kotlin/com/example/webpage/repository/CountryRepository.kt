package com.example.webpage.repository

import com.example.webpage.model.Country
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CountryRepository : JpaRepository<Country, String> {
}