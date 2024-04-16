package kr.ac.kmu.Capstone.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.ac.kmu.Capstone.config.auth.CustomUserDetails;
import kr.ac.kmu.Capstone.dto.user.*;
import kr.ac.kmu.Capstone.entity.User;
import kr.ac.kmu.Capstone.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("/hello")
    public ResponseEntity hello() {
        return new ResponseEntity("CI/CD Testing Success", HttpStatus.OK);
    }

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity signup(@Valid @RequestBody SignupDto signupDTO, BindingResult bindingResult){
        // nickname, email, password
        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                return new ResponseEntity<>(error.getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        userService.joinUser(signupDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    // 중복가입 확인
    // True -> 중복, False -> 중복x
    @GetMapping("/join/{email}/exists")
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable("email") String email){
        return ResponseEntity.ok(userService.checkEmailDuplicate(email));
    }

    // 로그인
    // 로그아웃
    // security에서 제공


    // 회원정보
    @GetMapping("/info") // good!
    public ResponseEntity getInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        User info = customUserDetails.getUser();
        return ResponseEntity.ok(info);
    }

    // 비밀번호 확인 // 별도의 버튼 있어서 따로. 별도의 update 같은걸로 api 만들기. 회원에 같이 안 넣고
    @PostMapping("/checkPW")
    public ResponseEntity checkPW(@RequestBody CheckPwDto checkPwDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        User user = customUserDetails.getUser();
        boolean result = userService.comparePW(user, checkPwDTO.getPassword());
        if(result == false)
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED); //417
        else {
            return ResponseEntity.ok(HttpStatus.OK);
        }
    }

    // 회원정보 업데이트
    @PostMapping("/update")
    public ResponseEntity update(@Valid @RequestBody UserUpdateDto userRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails, BindingResult bindingResult){
        // nickname, password, email
        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                return new ResponseEntity<>(error.getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        // 본인 확인
        User user = customUserDetails.getUser();
        if (!userService.checkUser(userRequestDto.getEmail(), user)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        userService.update(userRequestDto);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
