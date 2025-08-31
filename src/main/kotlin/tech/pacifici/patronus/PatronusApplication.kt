package tech.pacifici.patronus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PatronusApplication

fun main(args: Array<String>) {
    runApplication<PatronusApplication>(*args)
}
