package com.example.webpage.repository

import com.example.webpage.model.Country
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers.isA
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.junit4.SpringRunner
import java.lang.Exception
import org.junit.rules.ExpectedException


@RunWith(SpringRunner::class)
@DataJpaTest
class CountryRepositoryTest {

    @Rule
    @JvmField
    final val expectedException : ExpectedException = ExpectedException.none()

    @Autowired lateinit var countryRepository: CountryRepository

    @Test
    fun saveAndUpdate() {
        var country = Country("USA", "United States")
        var saved = countryRepository.save(country)
        assertThat(saved).isNotNull
        country = Country("GBR", "United Kingdom")
        countryRepository.save(country)

        try {
            country = Country("GBR", "United Kingdom Other Name")
            countryRepository.save(country)
        } catch (ex: Exception) {
            fail()
        }

        val findAll = countryRepository.findAll()
        assertThat(findAll).hasSize(2)
        assertThat(findAll.map { it.name }).contains("United States", "United Kingdom Other Name")
    }

    @Test
    fun saveUnique() {
        expectedException.expect(DataIntegrityViolationException::class.java)
        var country = Country("GBR", "United Kingdom")
        countryRepository.save(country)
        country = Country("AAA", "United Kingdom")
        countryRepository.saveAndFlush(country)

    }
}