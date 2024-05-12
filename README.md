# 프로젝트 설명

## 인증 방식

### 토큰 - JWT

### 암호화 방식

1. 해시 - 해시 방식은 salt가 없다
2. BCrypt, SCrypt, Argon2 ..

- salt 값 -> salt 값을 매번 다르게 하여 비밀번호 암호화

## 스프링 시큐리티

### UsernamePasswordAuthenticatonFilter

- UsernamePasswordAuthenticatonFilter는 로그인 요청을 처리 하고, 
- 전달받은 아이디와 비밀번호를 AuthenticationManagor에 전달하여 인증을 시도합니다.
- AuthenticationManager는 AuthenticationProvider를 사용 하여 실제 인증을 처리합니다.
- AuthenticationProvider는 주어진 인증 요청을 처리하고, 인증이 성공하면 Authentication 객체를 반환하거나, 인증이 실패하면 예외를 던집니다.
- AuthenticationManager는 AuthenticationProvider가 반환한 Authentication 객체를 받아서, 
- -> 인증이 성공하면 SecurityContextHolder에 인증된 Authentication 객체를 저장합니다.
- 그 후, 요청을 처리하거나 필터 체인의 다음 단계로 전달합니다.
- 따라서, UsernanePasswordAuthenticationFilter 가 사용자가 입력한 아이디와 비밀번호를 받아서 인증을 시도하고,
- AuthenticationManager가 실제 인증을 처리하며,
- AuthenticationProvidor가 인증 요청을 처리하여 성공하면
- SecurityContextHolder에 인증 정보를 저장하는 구조입니다.

### Spring Security JDBC Session



# 댓글 기능 Restful한 url 고민
## RequestParam - 쿼리 파라미터
- 어느 유형의 댓글인지 갈지 고민을 해야함 ex)블로그의 댓글 vs 쇼핑몰에 댓글 
POST /comments?postId=1
{
  author: ..
}

## PathVariable
- 대댓글을 다려면 url이 길어질 수가 있음
POST /posts/{postId}/comments
{

}