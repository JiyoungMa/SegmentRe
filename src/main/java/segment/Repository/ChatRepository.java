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

    public Long save(Chat chat){
        em.persist(chat);
        return chat.getChatId();
    }

    public List<Chat> getAllChatsByChatroom(Chatroom chatroom){
        return em.createQuery("select c from Chat c join fetch c.user u join fetch c.chatroom where c.chatroom = :chatroom")
                .setParameter("chatroom",chatroom)
                .getResultList();
    }

    public List<Chat> getChatsAfterTime(Chatroom chatroom, Long messageId){
        return em.createQuery("select c from Chat c join fetch c.user u join fetch c.chatroom where c.chatroom = :chatroom and c.chatId > :messageId")
                .setParameter("chatroom",chatroom).setParameter("messageId",messageId)
                .getResultList();
    }
}
