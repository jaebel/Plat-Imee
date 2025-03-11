package com.platimee.spring_platimee.useranime.model

import com.platimee.spring_platimee.anime.model.Anime
import com.platimee.spring_platimee.users.model.User
import jakarta.persistence.*
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@Entity
@Table(
    name = "user_anime",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "mal_id"])]
)
class UserAnime(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userAnimeGenerator")
    @SequenceGenerator(name = "userAnimeGenerator", sequenceName = "userAnimeSequence", allocationSize = 1)
    @Column(name = "id")
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mal_id", nullable = false)
    var anime: Anime,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: UserAnimeStatus? = null,

    @field:DecimalMin(value = "0.0", inclusive = true, message = "Rating must be at least 0.0")
    @field:DecimalMax(value = "10.0", inclusive = true, message = "Rating must be at most 10.0")
    @Column(name = "rating")
    var rating: Double? = null,

    @Column(name = "episodes_watched")
    var episodesWatched: Int? = null,

    @Column(name = "created_date", updatable = false)
    @CreatedDate
    var createdDate: Instant = Instant.now(),

    @Column(name = "updated_date")
    @LastModifiedDate
    var updatedDate: Instant = Instant.now()
)

enum class UserAnimeStatus {
    WATCHING,
    COMPLETED,
    ON_HOLD,
    PLAN_TO_WATCH,
    DROPPED
}
