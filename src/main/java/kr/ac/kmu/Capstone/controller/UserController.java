package kr.ac.kmu.Capstone.controller;


import jakarta.validation.Valid;
import kr.ac.kmu.Capstone.config.auth.CustomUserDetails;
import kr.ac.kmu.Capstone.config.common.response.ApiResponse;
import kr.ac.kmu.Capstone.config.common.response.StatusCode;
import kr.ac.kmu.Capstone.config.common.response.SuccessCode;
//import kr.ac.kmu.Capstone.config.auth.AuthRequired;
import kr.ac.kmu.Capstone.dto.user.*;
import kr.ac.kmu.Capstone.entity.User;
import kr.ac.kmu.Capstone.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/info")
    public ResponseEntity<?> getinfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

//    //@AuthRequired
//    @PostMapping("/password/{email}")
//    public ResponseEntity<?> modifyPassword(@PathVariable String email, @Valid @RequestBody ModifyPasswordRequest dto,
//                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
//        User user = customUserDetails.getUser();
//        UserResponseDto responseDto = userService.changePassword(dto.toParam(email), user.getEmail());
//        ApiResponse apiResponse = ApiResponse.responseData(StatusCode.SUCCESS,
//                SuccessCode.USER_UPDATE_PASSWORD_SUCESS.getMessage(), responseDto);
//        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
//    }
//
//    //@AuthRequired
//    @PostMapping("/nickname/{email}")
//    public ResponseEntity<?> modifyNickname(@PathVariable String email, @Valid @RequestBody ModifyNicknameRequest dto,
//                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
//        User user = customUserDetails.getUser();
//        UserResponseDto responseDto = userService.changeNickname(dto.toParam(email), user.getEmail());
//        ApiResponse apiResponse = ApiResponse.responseData(StatusCode.SUCCESS,
//                SuccessCode.USER_UPDATE_NICKNAME_SUCCESS.getMessage(), responseDto);
//        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
//    }
//
//    //@AuthRequired
//    @DeleteMapping("/delete/{email}")
//    public ResponseEntity<?> deleteUserInfo(@PathVariable String emial, @Valid @RequestBody DeleteUserRequest dto,
//                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
//        User user = customUserDetails.getUser();
//        userService.deleteUser(dto.toParam(emial), user.getEmail());
//        ApiResponse apiResponse = ApiResponse.responseMessage(StatusCode.SUCCESS,
//                SuccessCode.USER_DELETE_SUCCESS.getMessage());
//        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
//    }
//
//    //@AuthRequired
//    @GetMapping("/{email}/info")
//    public ResponseEntity<?> findUserInfo(@PathVariable String email, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
//        User user = customUserDetails.getUser();
//        FindUserInfoResponse responseDto = userService.getMyInfo(email, user);
//        ApiResponse apiResponse = ApiResponse.responseData(StatusCode.SUCCESS,
//                SuccessCode.USER_FINDMEMBER_SUCCESS.getMessage(), responseDto);
//        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
//    }

}