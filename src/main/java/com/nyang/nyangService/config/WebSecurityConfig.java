package com.nyang.nyangService.config;

//import com.iris.user.account.auth.jwt.JwtAuthFilter;
//import com.iris.user.account.auth.jwt.JwtProvider;
//import com.iris.user.account.auth.jwt.UsersProvider;
//import com.iris.user.account.user.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//    private final UsersProvider usersProvider;
//    private final CustomUserDetailService customUserDetailService;
//    private final JwtProvider jwtProvider;
//    private final RedisTemplate redisTemplate;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().disable()
                .authorizeRequests()
                .requestMatchers().permitAll()
                .antMatchers("/users/**").permitAll();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(usersProvider);
//        auth.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
//    }
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
}