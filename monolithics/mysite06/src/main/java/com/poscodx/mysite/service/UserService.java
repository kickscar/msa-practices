package com.poscodx.mysite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.poscodx.mysite.repository.UserRepository;
import com.poscodx.mysite.vo.UserVo;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public void join(UserVo vo) {
		vo.setPassword(passwordEncoder.encode(vo.getPassword()));
		userRepository.insert(vo);
		
		userRepository.insert(vo);

	}

	public UserVo getUser(Long no) {
		return userRepository.findByNo(no);
	}
	
	public UserVo getUser(String email) {
		return userRepository.findByEmail(email);
	}

	public void update(UserVo userVo) {
		userVo.setPassword(userVo.getPassword().equals("") ? "" : passwordEncoder.encode(userVo.getPassword()));
		userRepository.update(userVo);
	}
}
