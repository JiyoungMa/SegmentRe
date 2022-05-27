package segment.Repository;

import segment.Entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    public User save(User user);

    public Optional<User> findOne(String userId);

    public List<User> findAll();
}
