package segment.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import segment.Entity.Chatroom_User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "login_id",length=20)
    private String loginId;

    @Column(name = "passwd", length = 80)
    private String passwd;

    @Column
    private String userName;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus = UserStatus.Offline;

    @OneToMany(mappedBy = "user")
    private List<Chatroom_User> userChatrooms = new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id")
    private Group group;

    public Long getId() {
        return id;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getPasswd() {
        return passwd;
    }

    public Group getGroup() {
        return group;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", loginId='" + loginId + '\'' +
                ", passwd='" + passwd + '\'' +
                ", group=" + group +
                '}';
    }
}
