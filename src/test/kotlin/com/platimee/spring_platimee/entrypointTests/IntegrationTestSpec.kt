package com.platimee.spring_platimee.entrypointTests

import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@AutoConfigureMockMvc
class IntegrationTestSpec(body: FunSpec.() -> Unit = {}) : FunSpec(body) {

    override fun extensions() = listOf(SpringExtension)
}