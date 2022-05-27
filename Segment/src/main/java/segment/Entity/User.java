package segment.Entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @Column (name = "USER_ID")
    private String userRealId;
    private String userPassword;
    private String userName;

    @Enumerated(EnumType.STRING)
    private UserAuth auth = UserAuth.USER;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus = UserStatus.Offline;

    @OneToMany(mappedBy = "user")
    private List<Chatroom_User> userChatrooms = new ArrayList<>();

    public User(){
    }

    public User(String userRealId, String userPassword, String userName){
        this.userRealId = userRealId;
        this.userPassword = userPassword;
        this.userName = userName;
    }

    public String getUserRealId() {
        return userRealId;
    }

    public void setUserRealId(String userRealId) {
        this.userRealId = userRealId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public List<Chatroom_User> getUserChatrooms() {
        return userChatrooms;
    }

    public void setUserChatrooms(List<Chatroom_User> chatrooms) {
        this.userChatrooms = chatrooms;
    }

    public UserAuth getAuth() {
        return auth;
    }
}
