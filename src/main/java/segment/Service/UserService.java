package segment.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import segment.Configuration.SecurityConfig;
import segment.Entity.User;
import segment.Repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SecurityConfig securityConfig;

    @Transactional
    public String signUp(User user){
        user.setUserPassword(getEncodedPassword(user.getUserPassword()));
        userRepository.save(user);
        return user.getUserRealId();
    }

    private String getEncodedPassword(String password){
        PasswordEncoder passwordEncoder = securityConfig.getPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    public boolean checkUserIdDuplicate(String id){
        User findUser = userRepository.findOne(id);
        if (findUser != null){
            return true; //존재함
        }else{
            return false; //존재안함
       }
    }
}
