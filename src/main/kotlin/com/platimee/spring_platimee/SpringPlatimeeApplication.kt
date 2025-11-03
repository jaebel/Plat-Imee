package com.platimee.spring_platimee

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@EnableScheduling
class SpringPlatimeeApplication

fun main(args: Array<String>) {
    runApplication<SpringPlatimeeApplication>(*args)
}

@RestController
@RequestMapping("/")
class HelloController {
    @GetMapping
    fun sayHello(): String {
        return "Hello, World!"
    }
}