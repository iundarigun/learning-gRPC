package br.com.devcave.grpc.devh

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MovieServiceApplication

fun main(args: Array<String>) {
	runApplication<MovieServiceApplication>(*args)
}
