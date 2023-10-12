package edu.uclm.esi.ds.Games.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.uclm.esi.ds.Games.entities.Token;

public interface TokenDAO extends JpaRepository<Token,String>{
	Optional<Token> findById(String id);
	Optional<Token> findByUserId(String idUsuarioANtiguo);
	
}