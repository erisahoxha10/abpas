package com.abpas.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class RaspPiController {

    @GetMapping("")
    fun getMain(): String {
        return "ABPaS Back-end app"
    }

}