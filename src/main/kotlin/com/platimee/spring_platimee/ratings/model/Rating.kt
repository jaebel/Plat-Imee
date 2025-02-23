package com.platimee.spring_platimee.ratings.model

import com.platimee.spring_platimee.users.model.User
import com.platimee.spring_platimee.anime.model.Anime
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(
    name = "rating",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "anime_id"])]
)
class Rating(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ratingGenerator")
    @SequenceGenerator(name = "ratingGenerator", sequenceName = "ratingSequence", allocationSize = 1)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anime_id", nullable = false)
    val anime: Anime,

    @Column(name = "rating", nullable = false)
    var rating: Double,

    @Column(name = "rated_at", nullable = false)
    var ratedAt: Instant = Instant.now()
)
