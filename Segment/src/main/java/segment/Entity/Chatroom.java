package segment.Entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Chatroom {
    @Id @GeneratedValue
    @Column (name = "CHATROOM_ID")
    private Long chatroomId;

    @Enumerated(EnumType.STRING)
    private ChatroomType chatroomType;

    private String chatroomName;

    @OneToMany(mappedBy = "chatroom")
    private List<Chatroom_User> chatroomUsers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BIGCHATROOM_ID")
    private Chatroom bigChatroom;

    @OneToMany(mappedBy = "bigChatroom")
    private List<Chatroom> smallChatRooms = new ArrayList<>();

    public Chatroom(){
    }

    public Chatroom(String chatroomName){
        this.chatroomType = ChatroomType.Big;
        this.chatroomName = chatroomName;
    }

    public Chatroom(String chatroomName, Chatroom bigChatroom){
        this.chatroomType = ChatroomType.Small;
        this.chatroomName = chatroomName;
        this.bigChatroom = bigChatroom;
        bigChatroom.getSmallChatRooms().add(this);
    }

    public Chatroom getBigChatroom() {
        return bigChatroom;
    }

    public void setBigChatroom(Chatroom bigChatroom) {
        this.bigChatroom = bigChatroom;
    }

    public List<Chatroom> getSmallChatRooms() {
        return smallChatRooms;
    }

    public void setSmallChatRooms(List<Chatroom> smallChatRooms) {
        this.smallChatRooms = smallChatRooms;
    }

    public String getChatroomName() {
        return chatroomName;
    }

    public void setChatroomName(String chatroomName) {
        this.chatroomName = chatroomName;
    }

    public Long getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(Long chatroomId) {
        this.chatroomId = chatroomId;
    }

    public ChatroomType getChatroomType() {
        return chatroomType;
    }

    public void setChatroomType(ChatroomType chatroomType) {
        this.chatroomType = chatroomType;
    }

    public List<Chatroom_User> getChatroomUsers() {
        return chatroomUsers;
    }

    public void setChatroomUsers(List<Chatroom_User> users) {
        this.chatroomUsers = users;
    }
}
