package segment.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import segment.Entity.User;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final EntityManager em;

    public void save(User user){
        em.persist(user);
    }

    public User findOne(String userId){
        return em.find(User.class, userId);
    }

    public List<User> findAll(){
        return em.createQuery("Select user from User user").getResultList();
    }
}
