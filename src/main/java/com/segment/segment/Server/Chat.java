package Server;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Chat {
    @Id @GeneratedValue
    @Column (name = "CHAT_ID")
    private Long chatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "CHATROOM_ID")
    private Chatroom chatroom;

    private String message;
    private LocalDateTime chatTime;
    private Long chatroomMessageId; //메세지 오류 체크 시 사용할 예정이지만 일단은 사용 하지 않음

    public Chat(){

    }

    public Chat(User user, Chatroom chatroom, String message){
        this.user = user;
        this.chatroom = chatroom;
        this.message = message;
        this.chatTime = LocalDateTime.now();
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Chatroom getChatroom() {
        return chatroom;
    }

    public void setChatroom(Chatroom chatroom) {
        this.chatroom = chatroom;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getChatTime() {
        return chatTime;
    }

    public void setChatTime(LocalDateTime chatTime) {
        this.chatTime = chatTime;
    }

    public Long getChatroomMessageId() {
        return chatroomMessageId;
    }

    public void setChatroomMessageId(Long chatroomMessageId) {
        this.chatroomMessageId = chatroomMessageId;
    }
}
