package com.platimee.spring_platimee.entrypointTests

import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional

@Transactional
@Rollback
@TestPropertySource(locations = ["classpath:application-test.yml"])
@ActiveProfiles("test") // comment me out to use the postgres db for tests
@SpringBootTest
@AutoConfigureMockMvc
class IntegrationTestSpec(body: FunSpec.() -> Unit = {}) : FunSpec(body) {

    override fun extensions() = listOf(SpringExtension)
}