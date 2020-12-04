package com.web.blog;

import java.util.Arrays;

import com.web.blog.service.JWTInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class WebBlogApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(WebBlogApplication.class, args);
	}

	@Autowired
	private JWTInterceptor jwtInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// add 는 jwt 있어야 접근 가능한곳
		// exclude는 jwt가 없어도 접근 가능한 곳
		// "/account/EmailDuplicateCheck", "/account/login", "/account/signup",
		// "/account/socialCheck", "/account/usernameDuplicateCheck", "/github",
		// "/github/callback",
		// "/github/getProfile", "/hashtag", "/kakao", "/kakao/callback",
		// "/kakao/getProfile",
		// "/kakao/logout", "/like", "/naver", "/naver/callback", "/naver/deleteToken",
		// "/naver/getProfile", "/naver/invalidate", "/naver/refreshToken",
		// "/getPopluarpostrelated",
		// "/selectpost", "/account/SendCheckEmail", "/typo"
		registry.addInterceptor(jwtInterceptor).excludePathPatterns(Arrays.asList("/**"));
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("*").allowedHeaders("*")
				.exposedHeaders("auth-token");
	}
}
