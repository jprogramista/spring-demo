package com.example.webpage.service

import com.example.webpage.model.Country
import com.example.webpage.repository.CountryRepository
import org.springframework.stereotype.Service

interface CountryService {
    fun findAll() : List<Country>
    fun findById(abbr: String): Country?
}

@Service
class JpaCountryService(val countryRepository: CountryRepository) : CountryService {

    override fun findById(abbr: String): Country? {
        return countryRepository.findById(abbr).orElse(null)
    }

    override fun findAll() : List<Country> {
        return countryRepository.findAll()
    }
}