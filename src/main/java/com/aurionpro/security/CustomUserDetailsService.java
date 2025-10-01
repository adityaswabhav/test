package com.aurionpro.security;

import com.aurionpro.entity.User;
import com.aurionpro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User u = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		var authorities = u.getRoles().stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName()))
				.collect(Collectors.toList());
		return new org.springframework.security.core.userdetails.User(u.getUsername(), u.getPassword(), authorities);
	}
}
