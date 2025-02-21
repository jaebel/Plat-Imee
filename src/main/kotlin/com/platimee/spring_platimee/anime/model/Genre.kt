package com.platimee.spring_platimee.anime.model

import jakarta.persistence.*

@Entity
@Table(name = "genre")
class Genre(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genreGenerator")
    @SequenceGenerator(name = "genreGenerator", sequenceName = "genreSequence", allocationSize = 1)
    @Column(name = "genre_id")
    var genreId: Long? = null,

    @Column(name = "name", nullable = false, unique = true)
    var name: String
)
