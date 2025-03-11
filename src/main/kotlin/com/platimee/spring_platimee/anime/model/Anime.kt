package com.platimee.spring_platimee.anime.model

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@Entity
@Table(name = "anime")
class Anime(
    @Id
    @Column(name = "mal_id", unique = true, nullable = false)
    var malId: Long,  // Now using malId as the primary key

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "score")
    var score: Double? = null,

    @Column(name = "english_name")
    var englishName: String? = null,

    @Column(name = "japanese_name")
    var japaneseName: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    var type: AnimeType? = null,

    @Column(name = "episodes")
    var episodes: Int? = null,

    @Column(name = "aired")
    var aired: String? = null,

    @Column(name = "premiered")
    var premiered: String? = null,

    // Update join column from anime_id to mal_id
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "anime_genre",
        joinColumns = [JoinColumn(name = "mal_id")],
        inverseJoinColumns = [JoinColumn(name = "genre_id")]
    )
    var genres: MutableSet<Genre> = mutableSetOf(),

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
