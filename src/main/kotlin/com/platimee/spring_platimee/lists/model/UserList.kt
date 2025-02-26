package com.platimee.spring_platimee.lists.model

import com.platimee.spring_platimee.users.model.User
import com.platimee.spring_platimee.anime.model.Anime
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@Entity
@Table(name = "user_list")
class UserList(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userListGenerator")
    @SequenceGenerator(name = "userListGenerator", sequenceName = "userListSequence", allocationSize = 1)
    @Column(name = "list_id")
    var listId: Long? = null,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "description")
    var description: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToMany
    @JoinTable(
        name = "user_list_anime",
        joinColumns = [JoinColumn(name = "list_id")],
        inverseJoinColumns = [JoinColumn(name = "anime_id")]
    )
    var anime: MutableSet<Anime> = mutableSetOf(),

    @Column(name = "created_date", updatable = false)
    @CreatedDate
    var createdDate: Instant = Instant.now(),

    @Column(name = "updated_date")
    @LastModifiedDate
    var updatedDate: Instant = Instant.now()
)
