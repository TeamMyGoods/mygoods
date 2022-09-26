package com.hoiae.mygoods.config;

import com.hoiae.mygoods.member.service.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationService authenticationService;
    public SecurityConfig(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authenticationService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //인증 무시 설정
        web.ignoring().antMatchers("/css/**", "/js/**", "/static/**", "/lib/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //권한별 접근 페이지 설정
        http
                .authorizeRequests()
                .mvcMatchers("/product/**", "/member/mypage/**", "/payment/**").hasAnyAuthority("ROLE_MEMBER", "ROLE_ADMIN")
                //.mvcMatchers("/notice/regist").hasAnyAuthority("ROLE_ADMIN")
                .mvcMatchers("/**").permitAll()
                .and()
                .csrf().disable();
        //로그인 로그아웃 설정
        http
                .formLogin()
                .loginPage("/member/login")             //커스텀 로그인 페이지 사용
                .defaultSuccessUrl("/")                 //로그인 성공시 이동 페이지
                //.failureUrl("/member/loginfail")
                .usernameParameter("memberId")			// 아이디 파라미터명 설정
                .passwordParameter("memberPwd")			// 패스워드 파라미터명 설정
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/");

    }
}
