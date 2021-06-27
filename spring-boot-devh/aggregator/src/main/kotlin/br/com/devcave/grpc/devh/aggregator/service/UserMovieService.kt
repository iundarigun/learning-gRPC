package br.com.devcave.grpc.devh.aggregator.service

import br.com.devcave.grpc.devh.aggregator.domain.RecommendedMovie
import br.com.devcave.grpc.devh.aggregator.domain.UserRequest
import br.com.devcave.grpc.proto.common.Genre
import br.com.devcave.grpc.proto.movie.MovieSearchRequest
import br.com.devcave.grpc.proto.movie.MovieServiceGrpc
import br.com.devcave.grpc.proto.user.UserGenreUpdateRequest
import br.com.devcave.grpc.proto.user.UserSearchRequest
import br.com.devcave.grpc.proto.user.UserServiceGrpc
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Service
import java.util.Locale

@Service
class UserMovieService {
    @GrpcClient("user-service")
    private lateinit var userStub: UserServiceGrpc.UserServiceBlockingStub

    @GrpcClient("movie-service")
    private lateinit var movieStub: MovieServiceGrpc.MovieServiceBlockingStub

    fun getByUser(loginId: String): List<RecommendedMovie> {
        val userGenreResponse = userStub.getUserGenre(
            UserSearchRequest.newBuilder()
                .setLoginId(loginId)
                .build()
        )

        if (userGenreResponse.loginId.isNullOrBlank()) {
            return emptyList()
        }

        val moviesResponse = movieStub.getMovies(
            MovieSearchRequest.newBuilder()
                .setGenre(userGenreResponse.genre)
                .build()
        )
        return moviesResponse.movieList.map {
            RecommendedMovie(it.title, it.year, it.rating)
        }
    }

    fun updateUser(request: UserRequest) {
        userStub.updateUserGenre(
            UserGenreUpdateRequest.newBuilder()
                .setLoginId(request.loginId)
                .setGenre(Genre.valueOf(request.genre.uppercase(Locale.getDefault())))
                .build()
        )
    }
}