package kr.ac.kmu.Capstone.service;

import kr.ac.kmu.Capstone.config.common.response.ErrorCode;
import kr.ac.kmu.Capstone.dto.user.*;
import kr.ac.kmu.Capstone.entity.Role;
import kr.ac.kmu.Capstone.entity.User;
import kr.ac.kmu.Capstone.repository.RefreshTokenRepository;
import kr.ac.kmu.Capstone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenRepository refreshTokenRepository;


	public UserResponseDto changePassword(ModifyPasswordTarget target, String email) {
		validUser(target.getEmail(), email);
		User user = findUserByEmail(target.getEmail());
		user.changePassword(passwordEncoder.encode(target.getPassword()));

		return UserResponseDto.of(userRepository.save(user));
	}


	public UserResponseDto changeNickname(ModifyNicknameTarget target, String email) {
		validUser(target.getEmail(), email);
		User user = findUserByEmail(target.getEmail());
		user.changeNickname(target.getNickname());
		return UserResponseDto.of(userRepository.save(user));
	}


	public void deleteUser(DeleteUserTarget target, String email) {
		User user = findUserByEmail(target.getEmail());

		validUser(target.getEmail(), email);
		validPassword(target.getPassword(), user.getPassword());

		userRepository.deleteById(user.getId());
		refreshTokenRepository.deleteById(String.valueOf(user.getId()));
	}


//	@Override
//	public UserApiResponse saveDeviceToken(ReSaveDeviceTokenTarget reSaveDeviceTokenTarget) {
//		User user = findUserByEmail(reSaveDeviceTokenTarget.getEmail());
//
//		user.changeDeviceToken(reSaveDeviceTokenTarget.getDeviceToken());
//		userRepository.save(user);
//
//		return UserApiResponse.of(user);
//	}

	// 현재 SecurityContext 에 있는 유저 정보 가져오기

	public FindUserInfoResponse getMyInfo(String email, User user) {
		validUser(email, user.getEmail());
		return FindUserInfoResponse.of(user);
	}

	private User findUserByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new NullPointerException(ErrorCode.ID_NOT_EXIST.getMessage()));
	}

	private void validUser(String email, String eamil2) {
		if (email != eamil2) {
			throw new SecurityException(ErrorCode.ID_NOT_MATCH.getMessage());
		}
	}

	private void validPassword(String requestPassword, String encodedPassword) {
		if (!passwordEncoder.matches(requestPassword, encodedPassword)) {
			throw new SecurityException(ErrorCode.PASSWORD_NOT_MATCH.getMessage());
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
					.authority(user.getRoles())
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