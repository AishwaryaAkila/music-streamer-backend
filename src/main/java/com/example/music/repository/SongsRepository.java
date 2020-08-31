package com.example.music.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.music.model.Songs;

public interface SongsRepository extends JpaRepository<Songs,Long> {
	
	@Query(value = "select id,audio_name,audio_image,artist from songs order by id desc limit 10", nativeQuery = true)
	List<Object[]> findlatest();
	  
	@Query(value = "select id,audio_file from songs where id=?1",nativeQuery = true)
	List<Object[]> playSong(Long id);
	
	@Query(value = "select id,audio_name,audio_image,artist,movie_name from songs order by id desc limit 20", nativeQuery = true)
	List<Object[]> findAlllatest();
	
	@Query(value = "select id,audio_name,audio_image,artist,movie_name from songs where category=?1",nativeQuery = true)
	List<Object[]> findCategory(String key);
	
	@Query(value = "select id,audio_name,audio_image,artist,movie_name from songs where language=?1",nativeQuery = true)
	List<Object[]> findLanguage(String key);
	
	@Query(value="select id from songs limit 1",nativeQuery = true)
	Long findFirst();
	
	@Query(value = "select id,audio_name,audio_image,audio_file,artist from songs "
			+ "where id=?1",nativeQuery = true)

	List<Object[]> playnext(Long id);
	
	
	
	@Query(value = "select id,audio_name,audio_image,artist,movie_name from songs where lower(audio_name) like ?1%"
			+ " or lower(movie_name) like ?1% or lower(artist) like ?1%",nativeQuery = true)
	List<Object[]> searchSong(String key);
	
	@Query(value="select id from songs order by id desc limit 1",nativeQuery = true)
	Long findLast();
}
