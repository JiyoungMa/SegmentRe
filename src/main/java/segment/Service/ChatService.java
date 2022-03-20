package segment.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import segment.Entity.Chat;
import segment.Entity.Chatroom;
import segment.Entity.User;
import segment.Exception.ErrorCode;
import segment.Exception.ResourceNotExist;
import segment.Repository.ChatRepository;
import segment.Repository.ChatroomRepository;
import segment.Repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatroomRepository chatroomRepository;
    private final UserRepository userRepository;

    @Transactional
    public void sendChats(String userId, Long chatroomId, String message){
        Chatroom findroom = chatroomRepository.findOne(chatroomId);
        if (findroom == null){
            throw new ResourceNotExist("해당 채팅방은 존재하지 않습니다", ErrorCode.NOT_FOUND);
        }

        User findUser = userRepository.findOne(userId);
        if (findUser == null){
            throw new ResourceNotExist("해당 유저는 존재하지 않습니다", ErrorCode.NOT_FOUND);
        }

        Chat chat = new Chat();
        chat.setChatroom(findroom);
        chat.setUser(findUser);
        chat.setMessage(message);
        chat.setChatTime(LocalDateTime.now());

        chatRepository.save(chat);
    }

    public List<Chat> getAllChats(Chatroom chatroom){
        return chatRepository.getAllChatsByChatroom(chatroom);
    }

    public List<Chat> getNewChats(Long chatroomid, LocalDateTime dateTime){
        Chatroom chatroom = chatroomRepository.findOne(chatroomid);
        return chatRepository.getChatsAfterTime(chatroom, dateTime);
    }
}
