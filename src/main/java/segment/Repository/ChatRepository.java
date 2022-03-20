package segment.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import segment.Entity.Chat;
import segment.Entity.Chatroom;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRepository {

    private final EntityManager em;

    public void save(Chat chat){
        em.persist(chat);
    }

    public List<Chat> getAllChatsByChatroom(Chatroom chatroom){
        return em.createQuery("select c from Chat c where c.chatroom = :chatroom")
                .setParameter("chatroom",chatroom)
                .getResultList();
    }

    public List<Chat> getChatsAfterTime(Chatroom chatroom, LocalDateTime dateTime){
        return em.createQuery("select c from Chat c where c.chatroom = :chatroom and c.chatTime > :dateTime")
                .setParameter("chatroom",chatroom).setParameter("dateTime",dateTime)
                .getResultList();
    }
}
