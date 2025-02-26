package com.platimee.spring_platimee.lists.repository

import com.platimee.spring_platimee.lists.model.UserList
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserListRepository : JpaRepository<UserList, Long>
