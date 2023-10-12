package edu.uclm.esi.ds.Games.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import edu.uclm.esi.ds.Games.entities.MultiMatchEntity;


public interface MatchDAO extends JpaRepository<MultiMatchEntity,String>{
    default void saveMultiMatch(MultiMatchEntity multiMatchEntity) {
        save(multiMatchEntity);
    }
	
}
