package com.qa.choonz.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qa.choonz.persistence.domain.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
	
	@Query(value ="SELECT * FROM album Where artist_id=?1", nativeQuery = true)
    List<Album> readArtist(Long id);
	
	@Query(value ="SELECT * FROM album Where genre_id=?1", nativeQuery = true)
    List<Album> readGenre(Long id);

}
