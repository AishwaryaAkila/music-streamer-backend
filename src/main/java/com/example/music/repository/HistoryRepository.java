package com.example.music.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import com.example.music.model.History;

public interface HistoryRepository extends JpaRepository<History,Long> {
	
	@Query(value = "select count(*) from history where user= ?1",nativeQuery = true)
	int findCount(Long id);
	
 
	@Query(value = "select id from history where user= ?1 limit 1",nativeQuery = true)
	Long get_first(Long id);
	
	@Modifying
	@Query(value="delete from history where id=?1",nativeQuery = true)
	void Remove(Long id);
	
//		EntityManagerFactory emf=Persistence.createEntityManagerFactory("history");
//		EntityManager em=emf.createEntityManager(); 
//		em.getTransaction().begin(); 
//		History s=em.find(History.class,id);
//		em.remove(s);  
//		em.getTransaction().commit();  
//		emf.close();  
//		em.close(); 
	
	@Query(value="select id,audio_name,audio_image,artist from songs "
			+ "where id in(select song from history where user=?1 order by id) limit 8",nativeQuery = true)
	List<Object[]> getRecent(Long id);
	
	@Query(value="select id,audio_name,audio_image,artist,movie_name from songs "
			+ "where id in(select song from history where user=?1) limit 13",nativeQuery = true)
	List<Object[]> getAllRecent(Long id);
	
	@Query(value="select id from history where user=?1 and song=?2",nativeQuery = true)
	Long getIdBysong(Long id1,Long id2);
	

}
