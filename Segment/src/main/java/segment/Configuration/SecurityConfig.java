package segment.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //https://youngjinmo.github.io/2021/05/passwordencoder/ 참고
    //https://bamdule.tistory.com/53
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers(
                        "/**","/css/**", "/js/**", "/img/**","/bootstraps/**"
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/users/signup/**").permitAll()
                .antMatchers("/users/login").permitAll()
                .antMatchers("/error").permitAll()
                .antMatchers("/chats/bigchatroomlist").permitAll()
                .antMatchers("/bigchatroom/**").permitAll()
                .anyRequest().authenticated();

    }
}