package kr.ac.kmu.Capstone.service;

import kr.ac.kmu.Capstone.dto.message.MessageDto;
import kr.ac.kmu.Capstone.entity.Message;
import kr.ac.kmu.Capstone.entity.User;
import kr.ac.kmu.Capstone.repository.MessageRepository;
import kr.ac.kmu.Capstone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    public MessageDto write(MessageDto messageDto, User user) {
        Optional<User> receiver = userRepository.findByEmail(messageDto.getReceiverName());
        Optional<User> sender = userRepository.findByEmail(user.getEmail());

        Message message = new Message();
        message.setReceiver(receiver.get());
        message.setSender(sender.get());

        message.setTitle(messageDto.getTitle());
        message.setContent(messageDto.getContent());
        message.setDeletedByReceiver(false);
        message.setDeletedBySender(false);
        messageRepository.save(message);

        return MessageDto.toDto(message);
    }

    @Transactional(readOnly = true)
    public MessageDto findMessageById(int id) {
        Message message = messageRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("메시지를 찾을 수 없습니다.");
        });

        return MessageDto.toDto(message);
    }

    @Transactional(readOnly = true)
    public List<MessageDto> receivedMessages(User user) {
        // 받은 편지함 불러오기
        // 한 명의 유저가 받은 모든 메시지
        List<Message> messages = messageRepository.findAllByReceiver(user);
        List<MessageDto> messageDtos = new ArrayList<>();

        for (Message message : messages) {
            // message 에서 받은 편지함에서 삭제하지 않았으면 보낼 때 추가해서 보내줌
            if (!message.isDeletedByReceiver()) {
                messageDtos.add(MessageDto.toDto(message));
            }
        }
        return messageDtos;
    }

    @Transactional
    public Object deleteMessageByReceiver(User user) {
        List<Message> messagesReceivedByUser = messageRepository.findAllByReceiver(user);

        for (Message message : messagesReceivedByUser) {
            if (!message.isDeletedByReceiver()) {
                message.deleteByReceiver(); // 받은 사람에게 메시지 삭제
                if (message.isDeleted()) {
                    messageRepository.delete(message); // 메시지가 양쪽 모두 삭제되었으면 데이터베이스에서 삭제
                }
            }
        }
        return "메시지 삭제 완료";
    }

    @Transactional(readOnly = true)
    public List<MessageDto> sentMessage(User user) {
        // 보낸 편지함 불러오기
        // 한 명의 유저가 받은 모든 메시지
        List<Message> messages = messageRepository.findAllBySender(user);
        List<MessageDto> messageDtos = new ArrayList<>();

        for (Message message : messages) {
            // message 에서 받은 편지함에서 삭제하지 않았으면 보낼 때 추가해서 보내줌
            if (!message.isDeletedBySender()) {
                messageDtos.add(MessageDto.toDto(message));
            }
        }
        return messageDtos;
    }

    // 보낸 편지 삭제
    @Transactional
    public Object deleteMessageBySender(MessageDto messageDto, User user) {
        Message message = messageRepository.findById(user.getId().intValue()).get();
        message.deleteBySender(); // 받은 사람에게 메시지 삭제
        if (message.isDeleted()) {
            // 받은사람과 보낸 사람 모두 삭제했으면, 데이터베이스에서 삭제요청
            messageRepository.delete(message);
            return "양쪽 모두 삭제";
        }
        return "한쪽만 삭제";
    }
}