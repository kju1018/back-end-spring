package com.mycompany.webapp.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.GenericFilterBean;

public class JwtAuthenticationFilter extends GenericFilterBean{
	
	private UserDetailsService userDetailsService;
	
	public JwtAuthenticationFilter(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		//JWT 토큰이 요청 헤더로 전송된 경우
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		String jwt = httpRequest.getHeader("authToken"); //요청 헤더에 있는지 먼저 검사 없으면 getParameter로 검사
		if(jwt == null) {
			//JWT가 요청 파라미터로 전달된 경우
			//<img src = "download?bno=3&authToken=xxxx"/>
			jwt = request.getParameter("authToken");
		}
		
		if(jwt != null) {//얘가 실행이 안됐다는건 인증이 안됐다는것
			if(JwtUtil.validateToken(jwt)) {//토큰의 유효시간이 만료되기 전
				
				//jwt에서 uid를 얻음
				String uid = JwtUtil.getUid(jwt);
				
				//db에서 uid에 해당하는 정보를 가져오기(이름, 권한(롤)들) 권한은 하나가 아니(user이자 admin일수도있음)
				UserDetails userDetails = userDetailsService.loadUserByUsername(uid);//uid로 db에서 가져옴
				
				//인증 객체 생성, 인증이 성공이 되면 Authentication객체가 만들어짐
				Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
				
				//Spring Security에 인증 객체 등록
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
				//얘가 등록이 안되면 인증이 안됨
			}
		}
		chain.doFilter(request, response);
	}

}
