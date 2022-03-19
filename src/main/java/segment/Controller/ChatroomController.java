package segment.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import segment.Entity.Chatroom;
import segment.Service.ChatroomService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatroomController {

    private final ChatroomService chatroomService;

    @GetMapping("/chats/bigchatroomlist")
    public String bigChatroomList(Model model) {
        List<Chatroom> bigchatrooms = chatroomService.getBigChatRoomList();
        model.addAttribute("bigchatrooms", bigchatrooms);
        return "chatrooms/bigchatroomlist";
    }

//    @GetMapping("/bigchatroom/{chatroomId}")
//    public String EnterBigChatRoom(@PathVariable("chatroomId") Long chatroomId, Model model){
//
//    }
}
