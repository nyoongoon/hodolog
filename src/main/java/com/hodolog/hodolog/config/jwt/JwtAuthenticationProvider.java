package com.hodolog.hodolog.config.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

// TODO 해당 클래스 필요가 없는데..? DAO 도 어차피 DB 접근해서 UserDetails 조회해오는 것이니까...?
// 상단에서 엑세스토큰 검증만 해서 다음 필터로 안가면 되는거 아닌가..>?
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationProvider(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        //dao 구현 내용.
//        if (authentication.getCredentials() == null) {
//            this.logger.debug("Failed to authenticate since no credentials provided");
//            throw new BadCredentialsException(this.messages
//                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
//        }
//        String presentedPassword = authentication.getCredentials().toString();
//        if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
//            this.logger.debug("Failed to authenticate since password does not match stored value");
//            throw new BadCredentialsException(this.messages
//                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
//        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // 이곳에서 토큰으로 검증..?
        return this.userDetailsService.loadUserByUsername(username);
    }
}
