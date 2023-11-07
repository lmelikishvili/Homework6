package com.example.homework6

import kotlin.properties.Delegates

class PersonData(
    firstName: String,
    lastName: String,
    age: String,
    email: String,
    password: String
) {
    lateinit var firstName: String
    lateinit var lastName: String
    lateinit var age: String
    lateinit var email: String
    lateinit var password: String
    lateinit var userID: String
}