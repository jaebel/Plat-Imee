package com.platimee.spring_platimee.lists.model

data class UserListResponseDTO(
    val listId: Long,
    val name: String,
    val description: String?,
    val userId: Long,
    val animeIds: List<Long>
)
