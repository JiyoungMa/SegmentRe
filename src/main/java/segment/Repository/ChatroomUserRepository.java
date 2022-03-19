package segment.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import segment.Entity.Chatroom;
import segment.Entity.Chatroom_User;
import segment.Entity.User;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatroomUserRepository {

    private static EntityManager em;

    public void save(Chatroom_User chatroom_user){
        em.persist(chatroom_user);
    }

    public List<User> findByRoom(Chatroom chatroom){
        return em.createQuery("Select cu.user from Chatroom_User cu where cu.chatroom = :chatroom")
                .setParameter("chatroom",chatroom)
                .getResultList();
    }

    public List<Chatroom> findByUser(User user){
        return em.createQuery("Select cu.chatroom from Chatroom_User cu where cu.user = :user")
                .setParameter("user", user)
                .getResultList();
    }

    public boolean CheckUserInRoom(Chatroom chatroom, User user){
        List<Chatroom_User> chatroomUserList = em.createQuery("select cu from Chatroom_User cu where cu.chatroom = :chatroom and cu.user = :user")
                .setParameter("chatroom",chatroom)
                .setParameter("user", user)
                .getResultList();

        if (chatroomUserList.size() == 0){
            return false;
        }else{
            return true;
        }
    }
}
