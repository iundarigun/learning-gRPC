package br.com.devcave.grpc.devh.service

import br.com.devcave.grpc.devh.repository.UserRepository
import br.com.devcave.grpc.proto.common.Genre
import br.com.devcave.grpc.proto.user.UserGenreUpdateRequest
import br.com.devcave.grpc.proto.user.UserResponse
import br.com.devcave.grpc.proto.user.UserSearchRequest
import br.com.devcave.grpc.proto.user.UserServiceGrpc
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class UserService(
    private val userRepository: UserRepository
) : UserServiceGrpc.UserServiceImplBase() {

    override fun getUserGenre(request: UserSearchRequest, responseObserver: StreamObserver<UserResponse>) {
        val builderResponse = UserResponse.newBuilder()
        userRepository.findById(request.loginId)?.let {
            builderResponse.setLoginId(it.login)
                .setName(it.name)
                .setGenre(Genre.valueOf(it.genre))
        }
        responseObserver.onNext(builderResponse.build())
        responseObserver.onCompleted()
    }

    override fun updateUserGenre(
        request: UserGenreUpdateRequest,
        responseObserver: StreamObserver<UserResponse>
    ) {
        val builderResponse = UserResponse.newBuilder()
        userRepository.findById(request.loginId)?.let {
            it.genre = request.genre.name
            userRepository.save(it)

            builderResponse.setLoginId(it.login)
                .setName(it.name)
                .setGenre(Genre.valueOf(it.genre))
        }
        responseObserver.onNext(builderResponse.build())
        responseObserver.onCompleted()
    }
}