package com.mycompany.webapp.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.webapp.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/login")
	//json이 {"uid":"user1", "upassword":"12345"} 이런식으로 Body에서 넘어옴
	public Map<String, String> login(@RequestBody Map<String, String> user) {//Map도 가능 Dto도 가능
		String uid = user.get("uid");
		String upassword = user.get("upassword");
		
		//사용자 인증
		UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(uid, upassword);
		Authentication authentication = authenticationManager.authenticate(upat);
		
		//Spring Security 인증 객체 등록
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		//JWT 생성
		String jwt = JwtUtil.createToken(uid);
		
		//JSON 응답 보내기
		Map<String, String> map = new HashMap<>();
		map.put("uid", uid);
		map.put("authToken", jwt);
		
		return map;
	}
}
