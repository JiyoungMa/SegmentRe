package com.example.segmentrewithspringsecurity.user;

import com.example.segmentrewithspringsecurity.userRelatedEntity.Group;
import com.example.segmentrewithspringsecurity.userRelatedEntity.GroupRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public UserService(UserRepository userRepository,
        GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLoginId(username)
                .map(user ->
                        User.builder()
                                .username(user.getLoginId())
                                .password(user.getPassword())
                                .authorities(user.getGroup().getAuthorities())
                                .build()
                )
                .orElseThrow(() -> new UsernameNotFoundException("Could not found user for "+ username));
    }

    @Transactional
    public Long save(SignUpRequest request){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Long groupId = 1L;
        if (request.isAdmin()){
            groupId = 2L;
        }
        Group group = groupRepository.findById(groupId).get();

        Users newUser = Users.builder()
            .loginId(request.getLoginId())
            .password(encodedPassword)
            .group(group)
            .build();

        return userRepository.save(newUser).getId();
    }

}
