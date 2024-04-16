package kr.ac.kmu.Capstone.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum Role {
    ADMIN, // 어드민
    MANAGER, // 게시물 올리는 학교 관계 사용자
    USER; // 일반 사용자

    // 쪽지는 단방향? 양방향? -> 물어보고 후에 수정
    // 일반 사용자 없애고 비회원? 채팅은 어떻게 할지,,?
    // 채팅은 앱버그 정도로만
    // 글 관리자와는 전화번호 넣어서 앱 외에서 문의하도록

    //private String key;

}
