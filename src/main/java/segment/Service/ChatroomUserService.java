package segment.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import segment.Entity.Chatroom;
import segment.Entity.Chatroom_User;
import segment.Entity.User;
import segment.Exception.ErrorCode;
import segment.Exception.ResourceNotExist;
import segment.Repository.ChatroomRepository;
import segment.Repository.ChatroomUserRepository;
import segment.Repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatroomUserService {

    private final ChatroomUserRepository chatroomUserRepository;
    private final ChatroomRepository chatroomRepository;
    private final UserRepository userRepository;

    @Transactional
    public Chatroom_User enterBigChatRoom(Long chatroomId, String userId){
        Chatroom findroom = chatroomRepository.findOne(chatroomId);
        if (findroom == null){
            throw new ResourceNotExist("해당 채팅방은 존재하지 않습니다", ErrorCode.NOT_FOUND);
        }

        User findUser = userRepository.findOne(userId);
        if (findUser == null){
            throw new ResourceNotExist("해당 유저는 존재하지 않습니다", ErrorCode.NOT_FOUND);
        }

        Chatroom_User chatroomUser = new Chatroom_User();
        chatroomUser.setChatroom(findroom);
        chatroomUser.setUser(findUser);
        chatroomUserRepository.save(chatroomUser);

        return chatroomUser;
    }

    @Transactional
    public void exitBigChatRoom(Long chatroomId, String userId){
        Chatroom findroom = chatroomRepository.findOne(chatroomId);
        if (findroom == null){
            throw new ResourceNotExist("해당 채팅방은 존재하지 않습니다", ErrorCode.NOT_FOUND);
        }

        User findUser = userRepository.findOne(userId);
        if (findUser == null){
            throw new ResourceNotExist("해당 유저는 존재하지 않습니다", ErrorCode.NOT_FOUND);
        }

        chatroomUserRepository.delete(findroom,findUser);
    }
}
