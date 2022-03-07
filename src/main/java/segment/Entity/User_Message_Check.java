package segment.Entity;

import javax.persistence.*;

@Entity
public class User_Message_Check {
    @Id @GeneratedValue
    @Column (name = "USER_MESSAGE_ID")
    private Long umcId;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "CHATROOM_ID")
    private Chatroom chatroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;


    public Long getUmcId() {
        return umcId;
    }

    public void setUmcId(Long umcId) {
        this.umcId = umcId;
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
