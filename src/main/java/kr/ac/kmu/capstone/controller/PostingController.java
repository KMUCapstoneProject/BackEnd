package kr.ac.kmu.Capstone.controller;

import jakarta.validation.Valid;
import kr.ac.kmu.Capstone.config.auth.CustomUserDetails;
import kr.ac.kmu.Capstone.dto.posting.PostingResponseDto;
import kr.ac.kmu.Capstone.dto.posting.PostingSaveDto;
import kr.ac.kmu.Capstone.dto.posting.PostingUpdateDto;
import kr.ac.kmu.Capstone.entity.User;
import kr.ac.kmu.Capstone.service.PostingService;
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
@RequestMapping("/api/posting")
@AllArgsConstructor
public class PostingController {

    private PostingService postingService;

    // 카테고리별 게시물 리스트 // 0은 전체
    @GetMapping("/list")
    public List<PostingResponseDto> postingList(@RequestParam Long categoryId) { // @PathVariable 이 MethodArgumentTypeMismatchException 에러 계속나서 @RequestParam 으로 바꿈
        postingService.refresh(); // 데드라인 지난 게시물 삭제
        //log.info(String.valueOf(categoryId));
        return postingService.postingList(categoryId);
    }

    // 수정중
    // 검색된 게시물 리스트
    @GetMapping("/search")
    public ResponseEntity search(@RequestParam String keyword, @RequestParam String categoryId){
        postingService.refresh();
        // 값이 안들어옴 수정필요
        log.info(String.valueOf(categoryId));

        List<PostingResponseDto> searchList = postingService.search(keyword, Long.parseLong(categoryId));
        if (searchList == null) {
            return new ResponseEntity(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
        return new ResponseEntity(searchList, HttpStatus.OK);
    }

    // 게시물 추가
    @PostMapping("/add")
    public ResponseEntity save(@Valid @RequestBody PostingSaveDto postingSaveDto, @AuthenticationPrincipal CustomUserDetails customUserDetails, BindingResult bindingResult) {
        //categoryId, title, content, startTime, deadline, latitude, longitude), session
        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                return new ResponseEntity<>(error.getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        User user = customUserDetails.getUser();
        postingService.save(postingSaveDto, user);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    // 게시물 내용 확인
    @GetMapping("/view")
    public ResponseEntity findById(@RequestParam("postId") Long postId) {
        PostingResponseDto posting = postingService.content(postId);
        postingService.updatePostHits(postId);
        return new ResponseEntity(posting, HttpStatus.OK);
    }

    // 게시물 내용 변경
    @PostMapping("/edit")
    public ResponseEntity edit(@Valid @RequestBody PostingUpdateDto posting, @AuthenticationPrincipal CustomUserDetails customUserDetails, BindingResult bindingResult) {
        // postId, categoryId, title, content, startTime, deadline, latitude, longitude, session
        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                return new ResponseEntity<>(error.getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        User user = customUserDetails.getUser();
        if (!postingService.checkUser(posting.getPostId(), user)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        postingService.update(posting);
        return new ResponseEntity(HttpStatus.OK);
    }


    // 게시물 삭제
    @GetMapping("/delete")
    public ResponseEntity deleteById(@RequestParam("postId") Long postId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        User user = customUserDetails.getUser();
        if (!postingService.checkUser(postId, user)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        postingService.delete(postId);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

}
