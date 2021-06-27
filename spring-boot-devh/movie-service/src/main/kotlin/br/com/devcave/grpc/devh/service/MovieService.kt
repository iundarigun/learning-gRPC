package br.com.devcave.grpc.devh.service

import br.com.devcave.grpc.devh.repository.MovieRepository
import br.com.devcave.grpc.proto.movie.MovieDTO
import br.com.devcave.grpc.proto.movie.MovieSearchRequest
import br.com.devcave.grpc.proto.movie.MovieSearchResponse
import br.com.devcave.grpc.proto.movie.MovieServiceGrpc
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class MovieService(
    private val movieRepository: MovieRepository
) : MovieServiceGrpc.MovieServiceImplBase() {

    override fun getMovies(request: MovieSearchRequest, responseObserver: StreamObserver<MovieSearchResponse>) {
        val movies = movieRepository.findByGenre(request.genre.name)
            .map{ MovieDTO.newBuilder()
                .setTitle(it.name)
                .setYear(it.year)
                .setRating(it.rating)
                .build()}
        val response = MovieSearchResponse.newBuilder()
            .addAllMovie(movies)
            .build()
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}