package br.com.devcave.grpc.devh.repository

import br.com.devcave.grpc.devh.entity.Movie
import br.com.devcave.grpc.proto.common.Genre
import org.springframework.stereotype.Repository

@Repository
class MovieRepository {
    fun findByGenre(genre:String): List<Movie> {
        return movieList.filter { it.genre == genre }
    }
    companion object {
        val movieList = listOf(
            Movie(1, "movie 01", 2001, 4.0, Genre.ACTION.name),
            Movie(2, "movie 02", 2002, 3.5, Genre.DRAMA.name),
            Movie(3, "movie 03", 2003, 5.0, Genre.CRIME.name),
            Movie(4, "movie 04", 2004, 2.7, Genre.ACTION.name),
            Movie(5, "movie 05", 2005, 3.9, Genre.CRIME.name),
            Movie(6, "movie 06", 2006, 4.6, Genre.WESTERN.name),
            Movie(7, "movie 07", 2007, 4.7, Genre.ACTION.name),
            Movie(8, "movie 08", 2008, 4.0, Genre.CRIME.name),
            Movie(9, "movie 09", 2009, 3.0, Genre.ACTION.name),
            Movie(10, "movie 10", 2010, 2.1, Genre.DRAMA.name),
            Movie(11, "movie 11", 2011, 1.5, Genre.ACTION.name),
            Movie(12, "movie 12", 2012, 3.5, Genre.WESTERN.name),
            Movie(13, "movie 13", 2013, 4.1, Genre.DRAMA.name),
            Movie(14, "movie 14", 2014, 4.4, Genre.ACTION.name),
            Movie(15, "movie 15", 2015, 5.0, Genre.DRAMA.name)
        )
    }
}