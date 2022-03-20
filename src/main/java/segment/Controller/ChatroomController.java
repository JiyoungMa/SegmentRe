package segment.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import segment.Entity.Chat;
import segment.Entity.Chatroom;
import segment.Entity.Chatroom_User;
import segment.Service.ChatService;
import segment.Service.ChatroomService;
import segment.Service.ChatroomUserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatroomController {

    private final ChatroomService chatroomService;
    private final ChatroomUserService chatroomUserService;
    private final ChatService chatService;

    @GetMapping("/chats/bigchatroomlist")
    public String bigChatroomList(Model model) {

        List<Chatroom> bigchatrooms = chatroomService.getBigChatRoomList();
        model.addAttribute("bigchatrooms", bigchatrooms);
        return "chatrooms/bigchatroomlist";
    }

    @GetMapping("/bigchatroom/{chatroomId}")
    public String EnterBigChatRoom(@PathVariable("chatroomId") Long chatroomId, @CookieValue(name = "userId") String userId, Model model){
        Chatroom_User chatroomUser = chatroomUserService.enterBigChatRoom(chatroomId,userId);

        List<Chat> chats = chatService.getAllChats(chatroomUser.getChatroom());
        model.addAttribute("bigchatroom",chatroomUser.getChatroom());
        model.addAttribute("chats",chats);
        return "chatrooms/bigchatroom";
    }

    @GetMapping("/bigchatroom/{chatroomId}/exit")
    public String ExitBigChatRoom(@PathVariable("chatroomId") Long chatroomId, @CookieValue(name = "userId") String userId, Model model){
        chatroomUserService.exitBigChatRoom(chatroomId,userId);

        return "redirect:/chats/bigchatroomlist";
    }
}
