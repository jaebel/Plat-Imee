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

    @Column(name = "mal_id", unique = true)
    var malId: Long? = null,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "score")
    var score: Double? = null,

    @Column(name = "english_name")
    var englishName: String? = null,

    @Column(name = "japanese_name")
    var japaneseName: String? = null,

    // If you still want an enum for Type, that’s fine—just handle "Unknown" or other cases
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    var type: AnimeType? = null,

    @Column(name = "episodes")
    var episodes: Int? = null,

    @Column(name = "aired")
    var aired: String? = null,

    @Column(name = "premiered")
    var premiered: String? = null,

    @ManyToMany
    @JoinTable(
        name = "anime_genre",
        joinColumns = [JoinColumn(name = "anime_id")],
        inverseJoinColumns = [JoinColumn(name = "genre_id")]
    )
    var genres: MutableSet<Genre> = mutableSetOf(),

    // If you track creation/update times
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

