package segment.Repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import segment.Entity.User;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "classpath:application-test.yml")
@Transactional
class UserJpaRepositoryTest {

    @Autowired
    UserJpaRepository userJpaRepository;

    User user = new User("testId", "testPassword", "testerName");


    @Test
    void save() {
        userJpaRepository.save(user);

        var foundUser = userJpaRepository.findOne(user.getUserRealId());

        assertThat(foundUser.get()).as("User").usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    void findOne() {
        User newUser = new User("test2Id", "test2Password", "tester2Name");
        userJpaRepository.save(newUser);

        var foundUser = userJpaRepository.findOne(newUser.getUserRealId());

        assertThat(foundUser.get()).as("User").usingRecursiveComparison().isEqualTo(newUser);

    }

    @Test
    void findAll() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i<10; i++){
            User newUser = new User(MessageFormat.format("testId{0}", i), MessageFormat.format("testPassword{0}", i), MessageFormat.format("testerName{0}", i));
            userList.add(newUser);
            userJpaRepository.save(newUser);
        }

        var foundList = userJpaRepository.findAll();

        assertThat(foundList).usingRecursiveFieldByFieldElementComparator()
                .hasSameElementsAs(userList);
    }
}