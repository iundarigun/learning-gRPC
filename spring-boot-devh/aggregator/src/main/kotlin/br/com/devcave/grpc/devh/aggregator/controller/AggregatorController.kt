package br.com.devcave.grpc.devh.aggregator.controller

import br.com.devcave.grpc.devh.aggregator.domain.RecommendedMovie
import br.com.devcave.grpc.devh.aggregator.domain.UserRequest
import br.com.devcave.grpc.devh.aggregator.service.UserMovieService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("aggregator")
class AggregatorController(
    private val userMovieService: UserMovieService
) {

    @GetMapping("user/{loginId}")
    fun getByUser(@PathVariable loginId:String): List<RecommendedMovie> {
        return userMovieService.getByUser(loginId)
    }

    @PutMapping("user")
    fun setUserGenre(@RequestBody request: UserRequest) {
        userMovieService.updateUser(request)
    }
}