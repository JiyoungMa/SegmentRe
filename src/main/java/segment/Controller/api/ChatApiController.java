package segment.Controller.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import segment.Controller.Dto.ChatDTO;
import segment.Controller.api.UserApiController;
import segment.Entity.Chat;
import segment.Entity.Chatroom;
import segment.Entity.User;
import segment.Exception.ErrorCode;
import segment.Exception.ResourceNotExist;
import segment.Service.ChatService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ChatApiController {

    private final ChatService chatService;

    @PostMapping("/bigchatroom/sendMessage")
    public SendingMessageResponse sendMesages(@RequestBody SendingMessage request) {
        Chat chat = chatService.sendChats(request.getUserId(), request.getChatroomId(), request.getMessage());

        return new SendingMessageResponse(chat.getChatId(),chat.getChatTime());
    }

    @PostMapping("/bigchatroom/getNewMessage")
    public List<ChatDTO> getNewMessage(@RequestBody GetMessage request){
        List<Chat> chats = chatService.getNewChats(request.getChatroomId(), request.getMessageId());
        return chats.stream().map(c -> new ChatDTO(c)).collect(Collectors.toList());
    }


    @Data
    private static class GetMessage{
        String userId;
        Long chatroomId;
        Long messageId;

        public GetMessage(String userId, Long messageId, Long chatroomId){
            this.chatroomId = chatroomId;
            this.userId = userId;
            this.messageId = messageId;
        }
    }


    @Data
    private static class SendingMessage {
        String message;
        Long chatroomId;
        String userId;

        private SendingMessage(String message, Long chatroomId, String userId){

            this.message = message;
            this.chatroomId = chatroomId;
            this.userId = userId;
        }
    }

    @Data
    private static class SendingMessageResponse{
        Long messageId;
        LocalDateTime messageTime;

        private SendingMessageResponse(Long messageId,LocalDateTime messageTime){
            this.messageId = messageId;
            this.messageTime = messageTime;
        }
    }
}
