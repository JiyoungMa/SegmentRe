package segment.Entity;

import javax.persistence.*;

@Entity
public class Chatroom_User {
    @Id @GeneratedValue
    @Column (name = "CHATROOM_USER_ID")
    private Long cuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CHATROOM_ID")
    private Chatroom chatroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    public Chatroom_User(){

    }

    public Chatroom_User(Chatroom chatroom, User user){
        this.chatroom = chatroom;
        chatroom.getChatroomUsers().add(this);
        this.user = user;
        user.getUserChatrooms().add(this);
    }

    public Long getCuId() {
        return cuId;
    }

    public void setCuId(Long cuId) {
        this.cuId = cuId;
    }

    public Chatroom getChatroom() {
        return chatroom;
    }

    public void setChatroom(Chatroom chatroom) {
        this.chatroom = chatroom;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
