package segment.Controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import segment.Controller.Dto.ChatDTO;
import segment.Entity.Chat;
import segment.Entity.Chatroom;
import segment.Entity.Chatroom_User;
import segment.Service.ChatService;
import segment.Service.ChatroomService;
import segment.Service.ChatroomUserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        model.addAttribute("bigchatroom",chatroomUser.getChatroom());

        List<Chat> chats = chatService.getAllChats(chatroomUser.getChatroom());
        List<ChatDTO> chatDtos = chats.stream().map(c -> new ChatDTO(c)).collect(Collectors.toList());
        model.addAttribute("chats",chatDtos);

        return "chatrooms/bigchatroom";
    }

    @GetMapping("/bigchatroom/{chatroomId}/exit")
    public String ExitBigChatRoom(@PathVariable("chatroomId") Long chatroomId, @CookieValue(name = "userId") String userId, Model model){
        chatroomUserService.exitBigChatRoom(chatroomId,userId);

        return "redirect:/chats/bigchatroomlist";
    }

}
