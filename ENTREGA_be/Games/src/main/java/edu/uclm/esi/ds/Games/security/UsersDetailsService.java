package edu.uclm.esi.ds.Games.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uclm.esi.ds.Games.dao.UserDAO;
import edu.uclm.esi.ds.Games.entities.User;

@Service
public class UsersDetailsService implements UserDetailsService  {
	
	
	@Autowired
	private UserDAO userRepo;

	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepo.findByName(username);
		List <GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
		
		return new org.springframework.security.core.userdetails.User(user.getName(), user.getPwd(),authorities);
	}
	
	

}
