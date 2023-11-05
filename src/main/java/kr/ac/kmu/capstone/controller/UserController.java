package kr.ac.kmu.Capstone.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.ac.kmu.Capstone.dto.user.CheckPwDto;
import kr.ac.kmu.Capstone.dto.user.LoginDto;
import kr.ac.kmu.Capstone.dto.user.SignupDto;
import kr.ac.kmu.Capstone.dto.user.UserUpdateDto;
import kr.ac.kmu.Capstone.entity.User;
import kr.ac.kmu.Capstone.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@Controller
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("/hello")
    public ResponseEntity hello() {
        return new ResponseEntity("hello", HttpStatus.OK);
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
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email){
        return ResponseEntity.ok(userService.checkEmailDuplicate(email));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto loginDTO, HttpSession session){

        String result = userService.loginUser(loginDTO);
        //login 실패
        if(result == "false"){
            return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
        } else {
            //login 성공
            session.setAttribute("email", result);
            session.setMaxInactiveInterval(1800); // 60s * 30 (30분)

            // user 객체
            User info = userService.getInfo(result);
            return ResponseEntity.ok(info);
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity logout(HttpSession session){
        if(session!=null){
            session.invalidate(); // 세션 삭제
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // 회원정보
    @GetMapping("/info")
    public ResponseEntity getInfo(HttpSession session){
        User info = userService.getUserBySession(session);
        return ResponseEntity.ok(info);
    }

    // 비밀번호 확인
    @PostMapping("/checkPW")
    public ResponseEntity checkPW(@RequestBody CheckPwDto checkPwDTO, HttpSession session){
        User info = userService.getUserBySession(session);
        boolean result = userService.comparePW(info, checkPwDTO.getPassword());
        if(result == false)
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED); //417
        else {
            return ResponseEntity.ok(HttpStatus.OK);
        }
    }

    // 회원정보 업데이트
    @PostMapping("/update")
    public ResponseEntity update(@Valid @RequestBody UserUpdateDto userRequestDto, BindingResult bindingResult){
        // nickname, password, email
        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                return new ResponseEntity<>(error.getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        userService.update(userRequestDto);
        User info = userService.getInfo(userRequestDto.getEmail());
        return ResponseEntity.ok(info);
    }


}