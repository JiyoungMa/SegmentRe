package segment.Controller.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

@RestController
@RequiredArgsConstructor
public class ChatApiController {

    private final ChatService chatService;

    @PostMapping("/bigchatroom/sendMessage")
    public void sendMesages(@RequestBody SendingMessage request) {
        chatService.sendChats(request.getUserId(), request.getChatroomId(), request.getMessage());
    }

    @GetMapping("/bigchatroom/getNewMessage")
    public List<Chat> getNewMessage(@RequestBody GetMessage request){
        return chatService.getNewChats(request.getChatroomId(), LocalDateTime.now());
    }

    @Data
    private static class GetMessage{
        String userId;
        Long chatroomId;
        String now_time;

        public GetMessage(String userId, String now_time, Long chatroomId){
            this.chatroomId = chatroomId;
            this.userId = userId;
            this.now_time = now_time;
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
        LocalDateTime localDateTime;

        private SendingMessageResponse(LocalDateTime localDateTime){
            this.localDateTime = localDateTime;
        }
    }
}
