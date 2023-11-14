package kr.ac.kmu.Capstone.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.ac.kmu.Capstone.dto.posting.PostingContentResponseDto;
import kr.ac.kmu.Capstone.dto.posting.PostingResponseDto;
import kr.ac.kmu.Capstone.dto.posting.PostingSaveDto;
import kr.ac.kmu.Capstone.dto.posting.PostingUpdateDto;
import kr.ac.kmu.Capstone.service.PostingService;
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
@RestController
@RequestMapping("/api/posting")
@AllArgsConstructor
public class PostingController {

    private PostingService postingService;

    /*// 전체 게시물 리스트
    @GetMapping("/list")
    public List<PostingResponseDto> postingList0() {
        postingService.refresh(); // 데드라인 지난 게시물 삭제
        return postingService.allPostingList();
    }*/

    // 카테고리별 게시물 리스트
    @GetMapping("/list")
    public List<PostingResponseDto> postingList(@RequestParam("categoryId") Long categoryId) { // @PathVariable 이 MethodArgumentTypeMismatchException 에러 계속나서 @RequestParam 으로 바꿈
        postingService.refresh(); // 데드라인 지난 게시물 삭제
        log.info(String.valueOf(categoryId));
        return postingService.postingList(categoryId);
    }

    // 검색된 게시물 리스트
    @GetMapping("/search")
    public ResponseEntity search(@RequestParam("keyword") String keyword, @RequestParam("categoryId") Long categoryId){
        postingService.refresh();
        List<PostingResponseDto> searchList = postingService.search(keyword, categoryId);
        if (searchList == null) {
            return new ResponseEntity(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
        return new ResponseEntity(searchList, HttpStatus.OK);
    }

    // 게시물 추가
    @PostMapping("/add")
    public ResponseEntity save(@RequestParam("categoryId") Long categoryId, @Valid @RequestBody PostingSaveDto postingSaveDto/*, HttpSession session*/, BindingResult bindingResult) {
        //categoryId, title, content, startTime, deadline, latitude, longitude), session
        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                return new ResponseEntity<>(error.getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        postingService.save(categoryId, postingSaveDto/*, session*/);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    // 게시물 내용 확인
    @GetMapping("/view")
    public ResponseEntity findById(@RequestParam("postId") Long postId) {
        PostingContentResponseDto posting = postingService.content(postId);
        postingService.updatePostHits(postId);
        return new ResponseEntity(posting, HttpStatus.OK);
    }

    // 게시물 내용 변경
    @PostMapping("/edit")
    public ResponseEntity edit(@RequestParam("postId") Long postId, @Valid @RequestBody PostingUpdateDto posting, /*HttpSession session,*/ BindingResult bindingResult) {
        // postId, categoryId, title, content, startTime, deadline, latitude, longitude, session
        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                return new ResponseEntity<>(error.getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        /*if (!postingService.checkUser(postId, session)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }*/
        log.info(String.valueOf(posting.getStartTime()));
        postingService.update(postId, posting);
        return new ResponseEntity(HttpStatus.OK);
    }


    // 게시물 삭제
    @GetMapping("/delete")
    public ResponseEntity deleteById(@RequestParam("postId") Long postId/*, HttpSession session*/) {

        /*if (!postingService.checkUser(postId, session)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }*/

        postingService.delete(postId);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

}
