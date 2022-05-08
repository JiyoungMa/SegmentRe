package segment.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import segment.Entity.User;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserJpaRepository implements UserRepository{
    private final EntityManager em;

    public User save(User user){
        em.persist(user);
        return user;
    }

    public Optional<User> findOne(String userId){
        try{
            return Optional.of(em.find(User.class, userId));
        }catch(EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    public List<User> findAll(){
        return em.createQuery("Select user from User user").getResultList();
    }
}
