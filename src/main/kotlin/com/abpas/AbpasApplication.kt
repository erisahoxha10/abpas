package com.abpas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class AbpasApplication

fun main(args: Array<String>) {
    runApplication<AbpasApplication>(*args)
}
