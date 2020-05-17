package ru.maltsev.reactiveredisexample.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("book")
data class Book(
        @Id
        val id: String = ObjectId.get().toHexString(),
        val author: String,
        val name: String
)
