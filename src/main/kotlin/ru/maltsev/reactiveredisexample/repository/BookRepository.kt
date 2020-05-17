package ru.maltsev.reactiveredisexample.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import ru.maltsev.reactiveredisexample.model.Book

interface BookRepository : ReactiveCrudRepository<Book, String>
