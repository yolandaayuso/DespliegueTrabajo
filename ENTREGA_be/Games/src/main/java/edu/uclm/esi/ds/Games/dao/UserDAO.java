package edu.uclm.esi.ds.Games.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.uclm.esi.ds.Games.entities.User;

public interface UserDAO extends JpaRepository<User,String>{

	User findByName(String name);
	User findByEmail(String email);
	User findByNameAndPwd(String name,String pwd);
	
	@Modifying
	@Query(value = "UPDATE users SET vip = '"+1+"' WHERE id = :id", nativeQuery = true)
	void updateUser(@Param("id") String id);
	
	@Modifying
	@Query(value = "UPDATE users SET pwd = :pwd WHERE name = :name", nativeQuery = true)
	void updateUserpwd(@Param("name") String id, @Param("pwd") String pwd);
	
}