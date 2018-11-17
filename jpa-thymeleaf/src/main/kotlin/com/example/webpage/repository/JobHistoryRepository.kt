package com.example.webpage.repository

import com.example.webpage.model.JobHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JobHistoryRepository : JpaRepository<JobHistory, Long> {
}