package kr.ac.kmu.Capstone.controller;

import jakarta.servlet.http.HttpSession;
import kr.ac.kmu.Capstone.dto.posting.PostingContentResponseDto;
import kr.ac.kmu.Capstone.entity.Posting;
import kr.ac.kmu.Capstone.entity.User;
import kr.ac.kmu.Capstone.service.PostingService;
import kr.ac.kmu.Capstone.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private UserService userService;
    private PostingService postingService;

    // 전체 글 리스트로 불러오기
    @GetMapping("/posting/list")
    public List<PostingContentResponseDto> getAllPostings(HttpSession session) {
        /*if (!postingService.checkUser(postId, session)) {
            //return new ResponseEntity(HttpStatus.BAD_REQUEST);
         */
        List<PostingContentResponseDto> list = postingService.allPostingsinAdmin();
        return list;
    }

    // 등록 대기 글 리스트만 불러오기
    @GetMapping("/posting/list/waiting0")
    public List<PostingContentResponseDto> getWaiting0Postings(HttpSession session) {
        List<PostingContentResponseDto> list = postingService.waiting0PostingsinAdmin();
        return list;
    }

    // 업데이트 대기 글 리스트만 불러오기
    @GetMapping("/posting/list/waiting1")
    public List<PostingContentResponseDto> getWaiting1Postings(HttpSession session) {
        List<PostingContentResponseDto> list = postingService.waiting1PostingsinAdmin();
        return list;
    }

    // 게시물 상태 변경
    @GetMapping("/posting/updatestatus")
    public ResponseEntity updateStatus(@RequestParam("postId") Long postId) {
        postingService.adminStatusUpdate(postId);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}
