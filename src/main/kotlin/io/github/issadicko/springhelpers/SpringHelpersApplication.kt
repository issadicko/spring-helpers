package io.github.issadicko.springhelpers

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringHelpersApplication

fun main(args: Array<String>) {
    runApplication<SpringHelpersApplication>(*args)
}
