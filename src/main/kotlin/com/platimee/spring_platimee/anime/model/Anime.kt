package com.platimee.spring_platimee.anime.model

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@Entity
@Table(name = "anime")
class Anime(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "animeGenerator")
    @SequenceGenerator(name = "animeGenerator", sequenceName = "animeSequence", allocationSize = 1)
    @Column(name = "anime_id")
    var animeId: Long? = null,

    @Column(name = "name", nullable = false)
    var name: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    var type: AnimeType,  // Enum instead of string

    @Column(name = "episodes")
    var episodes: Int? = null,

    @Column(name = "rating")
    var rating: Double? = null,

    @Column(name = "members", nullable = true)
    var members: Int? = null,

    @ManyToMany
//    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "anime_genre",
        joinColumns = [JoinColumn(name = "anime_id")],
        inverseJoinColumns = [JoinColumn(name = "genre_id")]
    )
    var genres: MutableSet<Genre> = mutableSetOf(), // Many-to-many relationship with Genre

    @Column(name = "created_date", updatable = false)
    @CreatedDate
    var createdDate: Instant = Instant.now(),

    @Column(name = "updated_date")
    @LastModifiedDate
    var updatedDate: Instant = Instant.now()
)

enum class AnimeType {
    TV,
    MOVIE,
    OVA,
    ONA,
    SPECIAL
}

