package kr.ac.kmu.Capstone.service;

import jakarta.servlet.http.HttpSession;
import kr.ac.kmu.Capstone.dto.posting.*;
import kr.ac.kmu.Capstone.entity.Category;
import kr.ac.kmu.Capstone.entity.Posting;
import kr.ac.kmu.Capstone.entity.User;
import kr.ac.kmu.Capstone.repository.CategoryRepository;
import kr.ac.kmu.Capstone.repository.PostingRepository;
import kr.ac.kmu.Capstone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostingService {

    // search 추가해야하는데 검색어로만? 아니면 다른 구분할 키워드라던가 위치라던가 이런게 필요할지?
    // 검색과 연관되게, list로 불러올 때 어떻게 불러올지?

    private final PostingRepository postingRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    /*@Transactional
    public Posting save(Long categoryId, PostingSaveDto postingSaveDto, HttpSession session) {

        User newuser = getInfo(session);
        Category newCategory = makeTempCategory(categoryId);

        Posting newPosting = postingSaveDto.toEntity(newuser, newCategory);
        return postingRepository.save(newPosting);
    }*/

    public User getUserInfo(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.get();
    }

    @Transactional
    public Posting save(Long categoryId, PostingSaveDto postingSaveDto) {

        User newuser = getUserInfo("test@ab.cd");
        Category newCategory = makeTempCategory(categoryId);

        Posting newPosting = postingSaveDto.toEntity(newuser, newCategory);
        return postingRepository.save(newPosting);
    }

    @Transactional
    public void update(Long postId, PostingUpdateDto updateParam) {

        Posting posting = makeTempPosting(postId);
        Category category = makeTempCategory(updateParam.getCategoryId());

        posting.setCategory(category);
        posting.setTitle(updateParam.getTitle());
        posting.setContent(updateParam.getContent());
        posting.setStartTime(updateParam.getStartTime());
        posting.setDeadline(updateParam.getDeadline());
        posting.setLatitude(updateParam.getLatitude());
        posting.setLongitude(updateParam.getLongitude());
        posting.setStatus(1); // 업데이트 대기
        postingRepository.save(posting);
    }

    @Transactional
    public void adminStatusUpdate(Long postId) {

        Posting posting = makeTempPosting(postId);
        posting.setStatus(2); // 등록
        postingRepository.save(posting);
    }

    public void updatePostHits(Long postId) {

        Posting posting = makeTempPosting(postId);
        posting.setPostHits(posting.getPostHits() + 1);
        postingRepository.save(posting);
    }


    public PostingContentResponseDto content(Long postId) {

        Optional<Posting> posting = postingRepository.findById(postId);

        DateTime startTime = DateTime.builder()
                .year(posting.get().getStartTime().getYear())
                .month(posting.get().getStartTime().getMonthValue())
                .day(posting.get().getStartTime().getDayOfMonth())
                .hour(posting.get().getStartTime().getHour())
                .minute(posting.get().getStartTime().getMinute())
                .build();

        DateTime dealine = DateTime.builder()
                .year(posting.get().getDeadline().getYear())
                .month(posting.get().getDeadline().getMonthValue())
                .day(posting.get().getDeadline().getDayOfMonth())
                .hour(posting.get().getDeadline().getHour())
                .minute(posting.get().getDeadline().getMinute())
                .build();

        Optional<PostingContentResponseDto> postingResponse = Optional.ofNullable(PostingContentResponseDto.builder()
                .postId(posting.get().getPostId())
                .userId(posting.get().getUser().getId())
                .categoryId(posting.get().getCategory().getCategoryId())
                .nickname(posting.get().getUser().getNickname())
                .title(posting.get().getTitle())
                .content(posting.get().getContent())
                .startTime(startTime)
                .deadline(dealine)
                .postHits(posting.get().getPostHits() + 1) // 조회수 1 증가
                .latitude(posting.get().getLatitude())
                .longitude(posting.get().getLongitude())
                .build());

        return postingResponse
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 상세보기 실패 : postId를 찾을 수 없습니다.");
                });
    }

    @Transactional
    public void delete(Long postId) {
        postingRepository.deleteById(postId);
    }


    @Transactional
    public void refresh() {
        List<Posting> postings = postingRepository.findAll();

        ListIterator<Posting> iterator = postings.listIterator();

        while(iterator.hasNext()){
            Posting posting = iterator.next();
            checkDeadline(posting);
        }
    }


    public boolean checkUser(Long postId, HttpSession session) {
        Posting posting = makeTempPosting(postId);
        User user = getInfo(session);

        if (posting.getUser().getId() == user.getId()) {
            return true;
        }
        return false;
    }


    public void checkDeadline(Posting posting) {
        LocalDateTime currentdate = LocalDateTime.now();

        if (posting.getDeadline().compareTo(currentdate) < 0) { // 데드라인이 지났을 경우
            delete(posting.getPostId());
        }
    }


    public List<PostingResponseDto> search(String keyword, Long categoryId){

        if (categoryId == 0) {
            List<Posting> postsListAll = postingRepository.findByTitleContaining(keyword);
            List<PostingResponseDto> postingResponseAll = postingListtoPostingResponseList(postsListAll);
            return postingResponseAll;
        }
        else if (categoryId < 3){
            Category category = makeTempCategory(categoryId);
            List<Posting> postsList = postingRepository.findByTitleContainingAndCategory(keyword, category);
            List<PostingResponseDto> postingResponseError = postingListtoPostingResponseList(postsList);
            return postingResponseError;
        }
        return null;

    }

    public List<PostingResponseDto> allPostingList() {
        List<Posting> postings = postingRepository.findAll();
        return postingListtoPostingResponseList(postings);
    }


    // 글 목록(카테고리)
    public List<PostingResponseDto> postingList(Long categoryId) {

        if (categoryId == 0) {
            return allPostingList();
        }

        Category category = makeTempCategory(categoryId);

        List<Posting> postings = postingRepository.findByCategory(category);
        List<PostingResponseDto> postingResponse = postingListtoPostingResponseList(postings);
        return postingResponse;
    }

    // 어드민에서 모든 포스팅 리스트로 가져오게 하는 것
    public List<PostingContentResponseDto> allPostingsinAdmin() {
        List<Posting> postings = postingRepository.findAll();
        return postingListtoPostingContentResponseList(postings);
    }

    // 등록 대기 포스팅만 가져오기
    public List<PostingContentResponseDto> waiting0PostingsinAdmin() {
        List<Posting> postings = postingRepository.findByStatus(0);
        return postingListtoPostingContentResponseList(postings);
    }

    // 업데이트 대기 포스팅만 가져오기
    public List<PostingContentResponseDto> waiting1PostingsinAdmin() {
        List<Posting> postings = postingRepository.findByStatus(1);
        return postingListtoPostingContentResponseList(postings);
    }


    private User getInfo(HttpSession session){
        String email = (String) session.getAttribute("email");
        Optional<User> user = userRepository.findByEmail(email);
        return user.get();
    }

    private Posting makeTempPosting(Long postId) {
        Posting newPosting = postingRepository.findById(postId)
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 찾기 실패 : postId를 찾을 수 없습니다.");
                });
        return newPosting;
    }

    private Category makeTempCategory(Long categoryId) {
        Category newCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 찾기 실패 : categoryId를 찾을 수 없습니다.");
                });
        return newCategory;
    }



    private List<PostingResponseDto> postingListtoPostingResponseList(List<Posting> postings){

        List<PostingResponseDto> postingResponseList = new ArrayList<>();
        for (Posting posting : postings) {

            //deadline 년, 월, 일, 시, 분
            DateTime dealine = DateTime.builder()
                    .year(posting.getDeadline().getYear())
                    .month(posting.getDeadline().getMonthValue())
                    .day(posting.getDeadline().getDayOfMonth())
                    .hour(posting.getDeadline().getHour())
                    .minute(posting.getDeadline().getMinute())
                    .build();

            PostingResponseDto postingResponseDto = PostingResponseDto.builder()
                    .postId(posting.getPostId())
                    .categoryId(posting.getCategory().getCategoryId())
                    .userId(posting.getUser().getId())
                    .nickname(posting.getUser().getNickname())
                    .title(posting.getTitle())
                    //.deadline(posting.getDeadline())
                    .deadline(dealine)
                    .postHits(posting.getPostHits())
                    .build();

            postingResponseList.add(postingResponseDto);
        }
        return postingResponseList;
    }





    private List<PostingContentResponseDto> postingListtoPostingContentResponseList(List<Posting> postings){

        //Collections.reverse(postings);
        List<PostingContentResponseDto> postingResponseList = new ArrayList<>();
        for (Posting posting : postings) {

            DateTime startTime = DateTime.builder()
                    .year(posting.getStartTime().getYear())
                    .month(posting.getStartTime().getMonthValue())
                    .day(posting.getStartTime().getDayOfMonth())
                    .hour(posting.getStartTime().getHour())
                    .minute(posting.getStartTime().getMinute())
                    .build();

            DateTime dealine = DateTime.builder()
                    .year(posting.getDeadline().getYear())
                    .month(posting.getDeadline().getMonthValue())
                    .day(posting.getDeadline().getDayOfMonth())
                    .hour(posting.getDeadline().getHour())
                    .minute(posting.getDeadline().getMinute())
                    .build();

            PostingContentResponseDto postingContentResponseDto = PostingContentResponseDto.builder()
                    .postId(posting.getPostId())
                    .categoryId(posting.getCategory().getCategoryId())
                    .userId(posting.getUser().getId())
                    .nickname(posting.getUser().getNickname())
                    .title(posting.getTitle())
                    .content(posting.getContent())
                    .startTime(startTime)
                    .deadline(dealine)
                    .postHits(posting.getPostHits())
                    .latitude(posting.getLatitude())
                    .longitude(posting.getLongitude())
                    .build();

            postingResponseList.add(postingContentResponseDto);
        }
        return postingResponseList;
    }


}
