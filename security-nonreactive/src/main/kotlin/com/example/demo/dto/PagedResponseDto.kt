package com.example.demo.dto

data class PagedResponseDto<T>(val results : List<T>, val total: Long, val current: Long)