package kr.ac.kmu.Capstone.controller;

import io.swagger.annotations.Api;
import jakarta.validation.Valid;
import kr.ac.kmu.Capstone.config.common.response.ApiResponse;
import kr.ac.kmu.Capstone.config.common.response.StatusCode;
import kr.ac.kmu.Capstone.config.common.response.SuccessCode;
import kr.ac.kmu.Capstone.dto.auth.AuthApiResponse;
import kr.ac.kmu.Capstone.dto.auth.TokenRequestDto;
import kr.ac.kmu.Capstone.dto.auth.UserLoginRequest;
import kr.ac.kmu.Capstone.dto.auth.UserSignUpRequest;
import kr.ac.kmu.Capstone.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Api
@Slf4j
public class AuthController {
	private final AuthService authService;

	//회원가입
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody UserSignUpRequest userSignUpRequest) {
		authService.signup(userSignUpRequest.toParam());
		ApiResponse apiResponse = ApiResponse.responseMessage(StatusCode.SUCCESS,
			SuccessCode.USER_SIGN_UP_SUCCESS.getMessage());
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
		AuthApiResponse tokenDto = authService.login(userLoginRequest.toParam());
		log.info("where");
		ApiResponse apiResponse = ApiResponse.responseData(StatusCode.SUCCESS,
			SuccessCode.USER_LOGIN_SUCCESS.getMessage(),tokenDto);
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	@PostMapping("/reissue")
	public ResponseEntity<?> reissue(@Valid @RequestBody TokenRequestDto tokenRequestDto) {
		AuthApiResponse tokenDto = authService.reissue(tokenRequestDto);
		ApiResponse apiResponse = ApiResponse.responseData(StatusCode.SUCCESS,
			SuccessCode.USER_REFRESH_SUCCESS.getMessage(), tokenDto);
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	@GetMapping("/check-duplicate-user-id/{email}")
	public ResponseEntity<?> checkDuplicateEmail(@Valid @PathVariable("email") String email) {
		boolean result = authService.checkDuplicateEmail(email);

		if (result) {
			ApiResponse apiResponse = ApiResponse.responseData(StatusCode.SUCCESS,
				SuccessCode.USER_ID_ALREADY_EXIST.getMessage(), !result);
			return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
		}

		ApiResponse apiResponse = ApiResponse.responseData(StatusCode.SUCCESS,
			SuccessCode.USER_ID_REGISTER_POSSIBLE.getMessage(), !result);
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	@GetMapping("/check-duplicate-nickname/{nickname}")
	public ResponseEntity<?> checkNickname(@Valid @PathVariable String nickname) {
		boolean result = authService.checkDuplicateNickname(nickname);

		if (result) {
			ApiResponse apiResponse = ApiResponse.responseData(StatusCode.SUCCESS,
				SuccessCode.NICKNAME_ALREADY_EXIST.getMessage(), !result);
			return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
		}

		ApiResponse apiResponse = ApiResponse.responseData(StatusCode.SUCCESS,
			SuccessCode.NICKNAME_REGISTER_POSSIBLE.getMessage(), !result);
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}
}