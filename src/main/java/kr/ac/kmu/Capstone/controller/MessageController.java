package kr.ac.kmu.Capstone.controller;

import kr.ac.kmu.Capstone.config.auth.CustomUserDetails;
import kr.ac.kmu.Capstone.dto.message.MessageDto;
import kr.ac.kmu.Capstone.entity.User;
import kr.ac.kmu.Capstone.response.Response;
import kr.ac.kmu.Capstone.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MessageController {
    private final MessageService messageService;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/messages")
    public Response<?> sendMessage(@RequestBody MessageDto messageDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();

        return new Response<>("성공", "쪽지를 보냈습니다.", messageService.write(messageDto, user));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/messages/received")
    public Response<?> getReceivedMessage(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();

        return new Response<>("성공", "받은 쪽지를 불러왔습니다.", messageService.receivedMessages(user));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/messages/received/{id}")
    public Response<?> deleteReceivedMessage(@PathVariable("id") Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        User user = customUserDetails.getUser();

        MessageDto messageDto = messageService.findMessageById(id);

        if (messageDto.getReceiverName().equals(user.getEmail())) {
            return new Response<>("삭제 성공", "받은 쪽지인, " + id + "번 쪽지를 삭제했습니다.", messageService.deleteMessageByReceiver(user));
            // return new Response<>("삭제 성공", "받은 쪽지인, " + id + "번 쪽지를 삭제했습니다.", messageService.deleteMessageByReceiver(messageDto, user));
        } else {
            return new Response<>("삭제 실패", "사용자 정보가 다릅니다.", null);
        }

    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/messages/sent")
    public Response<?> getSentMessage(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();

        return new Response<>("성공", "보낸 쪽지를 불러왔습니다.", messageService.sentMessage(user));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/messages/sent/{id}")
    public Response<?> deleteSentMessage(@PathVariable("id") Integer id, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        User user = customUserDetails.getUser();

        MessageDto messageDto = messageService.findMessageById(id);

        if (messageDto.getSenderName().equals(user.getEmail())) {
            return new Response<>("삭제 성공", "보낸 쪽지인, " + id + "번 쪽지를 삭제했습니다.", messageService.deleteMessageBySender(messageDto, user));
        } else {
            return new Response<>("삭제 실패", "사용자 정보가 다릅니다.", null);
        }
    }
}

