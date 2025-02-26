package com.platimee.spring_platimee.lists.model

import jakarta.validation.constraints.NotBlank

data class UserListCreateDTO(
    @field:NotBlank(message = "List name cannot be blank.")
    val name: String,

    val description: String? = null,

    // The user who owns the list
    val userId: Long,

    // Initial list of anime IDs to include
    val animeIds: List<Long> = emptyList()
)
