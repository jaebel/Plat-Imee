package com.platimee.spring_platimee.config

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.context.annotation.Configuration

@Configuration
class EnvConfig {

    companion object {
        init {
            println("EnvConfig init running from: ${System.getProperty("user.dir")}")

            val dotenv = Dotenv.configure()
                .directory(System.getProperty("user.dir")) // explicitly set to project root
                .ignoreIfMissing()
                .load()

            println("Loaded .env entries: ${dotenv.entries().size}")

            dotenv.entries().forEach { entry ->
                val key = entry.key
                val value = entry.value
                if (System.getenv(key) == null && System.getProperty(key) == null) {
                    System.setProperty(key, value)
                }
            }
        }
    }
}
