package com.example.music.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.music.model.Favourites;

public interface FavouritesRepository extends JpaRepository<Favourites,Long> {
	
	@Query(value = "select id,audio_name,audio_image,artist,movie_name from songs "
			+ "where id in(select song from favourites where user=?1)", nativeQuery = true)
	List<Object[]> findFavourites(Long id);
	
	@Query(value="select song from favourites where user=?1",nativeQuery = true)
	List<Long> getFavo(Long key2);
	
//	@Query(value="select song from favourites where song in(select id from songs "
//			+ "where category=?1 )",nativeQuery = true)
//	List<Long> getFavo2(String key2);
//	
//	@Query(value="select song from favourites where song in(select id from songs "
//			+ "where language=?1 )",nativeQuery = true)
//	List<Long> getFavo3(String key2);
//	
//	@Query(value="select song from favourites where song in(select id from songs "
//			+ "where order by desc id)",nativeQuery = true)
//	List<Long> getFavo4();
	
	@Modifying
	@Query(value="delete from favourites where user=?1 and song=?2",nativeQuery = true)
	void remove(Long user_id,Long song_id);
	
	
	

}
