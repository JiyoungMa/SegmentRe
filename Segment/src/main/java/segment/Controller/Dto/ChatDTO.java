package segment.Controller.Dto;

import lombok.Data;
import segment.Entity.Chat;

import java.time.LocalDateTime;

@Data
public class ChatDTO{
    String username;
    Long chatId;
    String message;
    LocalDateTime chatTime;

    public ChatDTO(Chat chat){
        this.username = chat.getUser().getUserName();
        this.chatId = chat.getChatId();
        this.message = chat.getMessage();
        this.chatTime = chat.getChatTime();
    }
}