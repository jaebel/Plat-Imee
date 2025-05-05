package com.platimee.spring_platimee.seeder

import com.opencsv.CSVReader
import com.platimee.spring_platimee.anime.model.Anime
import com.platimee.spring_platimee.anime.model.AnimeType
import com.platimee.spring_platimee.anime.repository.AnimeRepository
import com.platimee.spring_platimee.anime.model.Genre
import com.platimee.spring_platimee.anime.repository.GenreRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.io.InputStreamReader
import java.time.Instant

@Component
@Profile("!test")
class AnimeSeeder(
    private val animeRepository: AnimeRepository,
    private val genreRepository: GenreRepository
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        if (animeRepository.count() == 0L) {
            seedAnimeFromCsv()
        } else {
            println("Database already seeded with anime data. Skipping.")
        }
    }

    private fun seedAnimeFromCsv() {
        val resource = ClassPathResource("animeDatasource/anime.csv")

        if (!resource.exists()) {
            println("CSV file not found!")
            return
        }
        val csvFile = resource.file
        CSVReader(InputStreamReader(csvFile.inputStream())).use { reader ->
            // Read header line
            val header = reader.readNext()
            println("CSV Header: ${header?.joinToString(",")}")

            var line: Array<String>?
            while (reader.readNext().also { line = it } != null) {
                line?.let { columns ->
                    if (columns.size >= 10) {
                        val malId = columns[0].trim().toLongOrNull()
                        val name = columns[1].trim()
                        val score = columns[2].trim().toDoubleOrNull()
                        val genreString = columns[3].trim()
                        val englishName = columns[4].trim().ifBlank { null }
                        val japaneseName = columns[5].trim().ifBlank { null }
                        val typeString = columns[6].trim()
                        val episodes = columns[7].trim().toIntOrNull()
                        val aired = columns[8].trim().ifBlank { null }
                        val premiered = columns[9].trim().ifBlank { null }

                        val animeType = try {
                            AnimeType.valueOf(typeString.uppercase())
                        } catch (e: Exception) {
                            AnimeType.TV // fallback if not recognized
                        }

                        val animeEntity = Anime(
                            malId = malId!!,
                            name = name,
                            englishName = englishName,
                            japaneseName = japaneseName,
                            type = animeType,
                            episodes = episodes,
                            score = score,
                            aired = aired,
                            premiered = premiered,
                            genres = mutableSetOf(), // will set genres next
                            createdDate = Instant.now(),
                            updatedDate = Instant.now()
                        )

                        // Process genres from the comma-separated string
                        if (genreString.isNotEmpty()) {
                            val genreNames = genreString.split(",")
                            val genreEntities = genreNames.mapNotNull { gName ->
                                val trimmedName = gName.trim()
                                if (trimmedName.isNotEmpty()) {
                                    // Check if genre exists - else create a new one
                                    val existingGenre = genreRepository.findByName(trimmedName)
                                    existingGenre ?: genreRepository.save(Genre(name = trimmedName))
                                } else null
                            }.toSet()
                            animeEntity.genres.addAll(genreEntities)
                        }

                        // Save the anime entity
                        animeRepository.save(animeEntity)
                    } else {
                        println("Skipping line due to insufficient columns: ${columns.joinToString(",")}")
                    }
                }
            }
        }
        println("Seeding of anime.csv completed successfully!")
    }
}
