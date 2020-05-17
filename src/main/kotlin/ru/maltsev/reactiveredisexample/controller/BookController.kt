package ru.maltsev.reactiveredisexample.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.bson.types.ObjectId
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import ru.maltsev.reactiveredisexample.model.Book
import ru.maltsev.reactiveredisexample.repository.BookRepository
import javax.annotation.PostConstruct
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@RestController
@RequestMapping("/book")
class BookController(private val bookRepository: BookRepository,
                     private val reactiveRedisTemplate: ReactiveRedisTemplate<String, String>,
                     private val objectMapper: ObjectMapper) {

    var reactiveValueOps = reactiveRedisTemplate.opsForValue()

    @PostConstruct
    fun init() {
        println("Set keys for redis, keys:")
        bookRepository.findAll()
                .flatMap { reactiveValueOps.set(it.id, objectMapper.writeValueAsString(it)) }
                .thenMany(reactiveRedisTemplate.keys("*"))
                .flatMap { reactiveValueOps.get(it) }
                .subscribe { println(it) }
    }

    @GetMapping
    fun getAll(): Flux<Book> {
        return reactiveRedisTemplate.keys("*")
                .flatMap {
                    println("GetData from redis")
                    reactiveValueOps.get(it)
                            .map {
                                objectMapper.readValue(it, Book::class.java)
                            }
                }
                .switchIfEmpty(bookRepository.findAll())
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): Mono<Book> {
        return reactiveValueOps.get(id)
                .map {
                    println("Get item from redis")
                    objectMapper.readValue(it, Book::class.java)
                }
                .switchIfEmpty(bookRepository.findById(id))
    }

    @PostMapping
    fun createOrUpdateBook(@RequestBody book: Book): Mono<Book> {
        return reactiveValueOps.set(book.id, objectMapper.writeValueAsString(book))
                .flatMap {
                    bookRepository.save(book)
                }
    }

    @DeleteMapping("/{id}")
    fun deleteBook(@PathVariable id: String): Mono<Void> {
        return reactiveValueOps.delete(id)
                .flatMap {
                    bookRepository.deleteById(id)
                }.then()
    }

}
