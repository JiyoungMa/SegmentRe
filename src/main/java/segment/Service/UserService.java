package segment.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import segment.Entity.User;
import segment.Repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public String signUp(User user){
        userRepository.save(user);
        return user.getUserRealId();
    }

    public boolean checkUserIdDuplicate(User user){
        User findUser = userRepository.findOne(user.getUserRealId());
        if (findUser != null){
            return false; //겹치지 않음
        }else{
            return true; //겹침
       }
    }
}
