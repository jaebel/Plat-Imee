package com.platimee.spring_platimee.entrypointTests

import com.fasterxml.jackson.databind.ObjectMapper
import com.platimee.spring_platimee.model.User
import com.platimee.spring_platimee.model.UserCreateDTO
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*

internal fun MockMvc.createUser(objectMapper: ObjectMapper, user: UserCreateDTO) : MvcResult = this.post("/api/v1/users"){
    content = objectMapper.writeValueAsString(user)
    contentType = MediaType.APPLICATION_JSON
}.andReturn()

internal fun MockMvc.deleteUser(accountNumber: Long) : MvcResult = this.delete("/bankAccounts/deleteByAccountNumber/$accountNumber").andReturn()

internal fun MockMvc.getBankAccountByAccountNumber(accountNumber: Long): MvcResult = this.get("/bankAccounts/requestByAccountNumber/$accountNumber").andReturn()

