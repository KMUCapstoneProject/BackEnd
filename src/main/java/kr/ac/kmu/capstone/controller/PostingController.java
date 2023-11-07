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
@Controller
@RequestMapping("/api/posting")
@AllArgsConstructor
public class PostingController {

    private PostingService postingService;

    // 전체 게시물 리스트
    @GetMapping("/list")
    public List<PostingResponseDto> postingList0() {
        postingService.refresh(); // 데드라인 지난 게시물 삭제
        return postingService.allPostingList();
    }

    // 카테고리별 게시물 리스트
    @GetMapping("/list/{categoryId}")
    public List<PostingResponseDto> postingList(@PathVariable Long categoryId) {
        postingService.refresh(); // 데드라인 지난 게시물 삭제
        return postingService.postingList(categoryId);
    }

    // 검색된 게시물 리스트
    @GetMapping("/search/{categoryId}")
    public ResponseEntity search(@RequestBody String keyword, @RequestBody String categoryId){
        postingService.refresh();
        Long newcategoryId = Long.parseLong(categoryId);
        List<PostingResponseDto> searchList = postingService.search(keyword, newcategoryId);
        if (searchList == null) {
            return new ResponseEntity(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
        return new ResponseEntity(searchList, HttpStatus.OK);
    }

    // 게시물 추가
    @PostMapping("/add/{categoryId}")
    public ResponseEntity save(@PathVariable Long categoryId, @Valid @RequestBody PostingSaveDto postingSaveDto/*, HttpSession session*/, BindingResult bindingResult) {
        //categoryId, title, content, startTime, deadline, latitude, longitude), session
        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                return new ResponseEntity<>(error.getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        postingService.save(categoryId, postingSaveDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    // 게시물 내용 확인
    @GetMapping("/{postId}")
    public ResponseEntity findById(@PathVariable Long postId) {
        PostingContentResponseDto posting = postingService.content(postId);
        postingService.updatePostHits(postId);
        return new ResponseEntity(posting, HttpStatus.OK);
    }

    // 게시물 내용 변경
    @PostMapping("/{postId}/edit")
    public ResponseEntity edit(@PathVariable Long postId, @Valid @RequestBody PostingUpdateDto posting/*, HttpSession session*/, BindingResult bindingResult) {
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
        postingService.update(postId, posting);
        return new ResponseEntity(HttpStatus.OK);
    }


    // 게시물 삭제
    @GetMapping("/{postId}/delete")
    public ResponseEntity deleteById(@PathVariable Long postId/*, HttpSession session*/) {

        /*if (!postingService.checkUser(postId, session)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }*/

        postingService.delete(postId);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

}
