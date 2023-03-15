package com.chicochico.domain.user.service;


import com.chicochico.common.service.AuthTokenProvider;
import com.chicochico.domain.user.dto.LoginRequestDto;
import com.chicochico.domain.user.dto.ProfileSimpleResponseDto;
import com.chicochico.domain.user.entity.UserEntity;
import com.chicochico.domain.user.repository.UserRepository;
import com.chicochico.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

import static com.chicochico.exception.ErrorCode.PASSWORD_NOT_MATCH;
import static com.chicochico.exception.ErrorCode.USER_NOT_FOUND;


@Log4j2
@Service
@RequiredArgsConstructor
public class LoginService {

	private final AuthTokenProvider authTokenProvider;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;


	/**
	 * 로그인을 수행합니다
	 *
	 * @param loginRequestDto 이메일과 비밀번호 (email, password)
	 * @param response        엑세스 토큰을 담을 response
	 */
	public ProfileSimpleResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {

		// 유저 존재 확인
		Optional<UserEntity> user = userRepository.findByEmail(loginRequestDto.getEmail());

		if (user.isEmpty()) {
			// 유저가 존재하지 않을 때 error 발생
			throw new CustomException(USER_NOT_FOUND);
		}

		log.info("[login] email : {}", user.get().getEmail());

		log.info("[login] 비밀번호 비교 수행");
		// 비밀번호 체크
		if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.get().getPassword())) {
			throw new CustomException(PASSWORD_NOT_MATCH);
		}

		log.info("[login] 비밀번호 패스워드 일치");

		log.info("[login] 토큰 생성 및 응답");

		// 토큰 생성 및 응답
		String accessToken = authTokenProvider.createAccessToken(user.get().getId(), user.get().getNickname());
		String refreshToken = authTokenProvider.createRefreshToken(user.get().getId(), user.get().getNickname());
		authTokenProvider.setHeaderAccessToken(response, accessToken);
		authTokenProvider.setHeaderRefreshToken(response, refreshToken);

		return ProfileSimpleResponseDto.fromEntity(user.get());
	}


	/**
	 * 깃허브로그인을 수행합니다
	 *
	 * @param loginRequestHeader 이메일과 비밀번호 (email, password)
	 * @param response           엑세스 토큰을 담을 response
	 */
	public void githubLogin(Map<String, Object> loginRequestHeader, HttpServletResponse response) {
	}


	/**
	 * 엑세스 토큰을 재발급합니다
	 *
	 * @param loginRequestHeader 엑세스 토큰
	 * @param response           엑세스 토큰을 담을 response
	 */
	public void createAccessToken(Map<String, Object> loginRequestHeader, HttpServletResponse response) {
		// 어세스, 리프레시 토큰 발급 및 헤더 설정 (아래는 진행 예시)
		//	String accessToken = jwtTokenProvider.createAccessToken(member.getEmail(), member.getRoles());
		//	String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail(), member.getRoles());
		//	jwtTokenProvider.setHeaderAccessToken(response, accessToken);
		//	jwtTokenProvider.setHeaderRefreshToken(response, refreshToken);
	}


	/**
	 * 로그아웃합니다 (엑세스 토큰 삭제)
	 *
	 * @param logoutRequestHeader 엑세스 토큰
	 */
	public void deleteAccessToken(Map<String, Object> logoutRequestHeader) {
	}

}
