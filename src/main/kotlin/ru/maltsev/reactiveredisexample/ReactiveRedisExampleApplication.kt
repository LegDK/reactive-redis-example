package ru.maltsev.reactiveredisexample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReactiveRedisExampleApplication

fun main(args: Array<String>) {
    runApplication<ReactiveRedisExampleApplication>(*args)
}
