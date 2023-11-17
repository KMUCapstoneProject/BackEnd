package kr.ac.kmu.Capstone.service;

import jakarta.servlet.http.HttpSession;
import kr.ac.kmu.Capstone.dto.user.LoginDto;
import kr.ac.kmu.Capstone.dto.user.SignupDto;
import kr.ac.kmu.Capstone.dto.user.UserResponseDto;
import kr.ac.kmu.Capstone.dto.user.UserUpdateDto;
import kr.ac.kmu.Capstone.entity.Posting;
import kr.ac.kmu.Capstone.entity.Role;
import kr.ac.kmu.Capstone.entity.User;
import kr.ac.kmu.Capstone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User getUserInfo(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.get();
    }

    public User joinUser(SignupDto signupDTO){
        signupDTO.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        return userRepository.save(signupDTO.toEntity());
    }

    public boolean checkUser(String email, User user) {

        if (email == user.getEmail()) {
            return true;
        }
        return false;
    }

    public Boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Transactional
    public void update(UserUpdateDto dto){

        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );

        // 암호화되지않은 비번이 들어온다
        if(passwordEncoder.matches(dto.getPassword(), user.getPassword())){
            user.update(user.getPassword(), dto.getNickname());
        }
        else{
            String encodePW = passwordEncoder.encode(dto.getPassword());
            user.update(encodePW, dto.getNickname());
        }
    }

    @Transactional
    public void updateStatusToManager(String email) {
        User user = getInfo(email);
        user.setRoles(Role.MANAGER);
    }

    @Transactional
    public void updateStatusToAdmin(String email) {
        User user = getInfo(email);
        user.setRoles(Role.ADMIN);
    }

    public User getInfo(String email){
        Optional<User> user = userRepository.findByEmail(email);
        return user.get();
    }

    public List<UserResponseDto> getAllUserInfo() {
        List<User> userList = userRepository.findAll();
        List<UserResponseDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            UserResponseDto userResponseDto = UserResponseDto.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .email(user.getEmail())
                    .role(user.getRoleKey())
                    .build();

            //log.info(String.valueOf(user.getId()));
            userDtoList.add(userResponseDto);
        }
        return userDtoList;
    }


    public boolean comparePW(User user, String checkPW){
        if(!passwordEncoder.matches(checkPW, user.getPassword())){
            return false;
        }
        return true;
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
