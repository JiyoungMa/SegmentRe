package segment.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import segment.Configuration.SecurityConfig;
import segment.Entity.User;
import segment.Exception.ErrorCode;
import segment.Exception.PasswordNotMatched;
import segment.Exception.ResourceNotExist;
import segment.Repository.UserRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService{
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
        Optional<User> findUser = userRepository.findOne(id);
        if (findUser.isPresent()){
            return true; //존재함
        }else{
            return false; //존재안함
       }
    }

    public boolean login(User user){
        Optional<User> findUser = userRepository.findOne(user.getUserRealId());

        if (findUser.isEmpty()){
            throw new ResourceNotExist("해당 아이디를 가진 유저는 존재하지 않습니다.", ErrorCode.NOT_FOUND);
        }
        boolean passwordResult = checkPassword(findUser.get(), user.getUserPassword());

        if (passwordResult == false){
            throw new PasswordNotMatched("아이디와 비밀번호가 일치하지 않습니다.", ErrorCode.PASSWORD_NOT_EXACT);
        }

        return true;
    }

    public boolean checkPassword(User findUser, String password){
        PasswordEncoder passwordEncoder = securityConfig.getPasswordEncoder();
        if (!passwordEncoder.matches(password, findUser.getUserPassword())){
            return false;
        }
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> findResult = userRepository.findOne(username);

        if(findResult.isEmpty())
            throw new UsernameNotFoundException(username);

        User findUser = findResult.get();
        return new org.springframework.security.core.userdetails.User(findUser.getUserRealId(), findUser.getUserPassword(), true,true,true,true,new ArrayList<>());
    }
}
