package segment.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import segment.Entity.Chatroom;
import segment.Entity.ChatroomType;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatroomRepository {

    private final EntityManager em;

    public void save(Chatroom chatroom){
        em.persist(chatroom);
    }

    public List<Chatroom> getAllBigRooms(){
        return em.createQuery("select chatroom from Chatroom chatroom where chatroom.chatroomType = :chatroomtype")
                .setParameter("chatroomtype", ChatroomType.Big)
                .getResultList();
    }

    public List<Chatroom> getAllSmallRooms(Chatroom bigroom){
        return em.createQuery("select chatroom from Chatroom chatroom where chatroom.bigChatroom = :bigroom")
                .setParameter("bigroom",bigroom)
                .getResultList();
    }

}
