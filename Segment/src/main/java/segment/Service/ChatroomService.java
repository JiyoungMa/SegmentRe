package segment.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import segment.Entity.Chatroom;
import segment.Repository.ChatroomRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;

    public List<Chatroom> getBigChatRoomList() {
        return chatroomRepository.getAllBigRooms();
    }
}
