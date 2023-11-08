# PD-Folio

## 서비스

### Playdata 수강생들끼리 만든 미니 프로젝트를 공유하고,

### 사이드 프로젝트 및 스터디를 구인하는 사이트

작업기간 : 8월 15일 ~ 8월 25일

---

## 기술스택

### 백엔드

- Java 17, Spring Boot 3.x.x
- Spring Data Jpa, Query Dsl
- MySql

### 프론트엔드

- Javascript(es6)
- React, Bootstrap

---

## ERD

![gather](https://github.com/ktk8916/pdfolio-refactoring/assets/71807768/f68790f2-86f6-42f4-bc93-c9f4b9f2bb8a)

---

## 중요하게 생각한 점

### 쿼리 고민하기

- 여러 조건에 따른 쿼리 성능 최적화
- N + 1 문제에 대한 해결

---

## 개선점

### 1. 테스트 코드

- 테스트 코드가 미비해서 서로가 작성한 기능이 온전히 동작하는지 명확하게 알 수 없음
- 프로젝트에 대한 전체적인 리팩토링을 진행하고 싶으나 사이드 이펙트 예상이 쉽지 않음

### 2. 게시글 이미지 처리

- 현재는 화면에서 작성된 게시글과 사진을 base64 인코딩과 md양식으로 변환하여 RDB에 통으로 저장
- 이를 스토리지 서비스를 사용하거나, 조회용 NoSQL을 사용하여 분리

### 3. 동시성 문제 처리

- 조회 성능 향상을 위해 좋아요 수 컬럼을 필드로 비정규화
- 동시에 여러 좋아요 요청이 왔을 경우 동시성 문제 발생 가능

---

## 리팩토링

### 1. MySql %like% 개선
 - 진행중..

### 1. 테스트 코드 작성

 - 로직에 대한 테스트 코드 N개 작성
 - Jacoco 테스트 커버리지 기존 21% -> 43%
 - 진행중..
   
### 2. 의존성 분리

기존 코드에선 `oauth2` 서버에 유저 정보를 가져오는 Oauth2Service의 `getUserInfo` 메서드와,  
`내 서비스`의 회원인지를 판별하는 코드가 같은 곳에서 사용되어 책임이 불분명하고 테스트가 어려웠음
```java
public class Oauth2Service {

    private final ProviderFactory providerFactory;
    private final AuthService authService;
    private final MemberRepository memberRepository;
    private final MemberSkillRepository memberSkillRepository;

    public Oauth2Response login(String providerName, LoginRequest loginRequest){
        Oauth2UserInfo userInfo = getUserInfo(providerName, loginRequest.accessToken());
        Member member = memberRepository
                .findByProviderIdAndProviderName(
                        userInfo.getProviderId(),
                        userInfo.getProviderName())
                .orElseThrow(UnregisteredMemberException::new);

        TokenDto token = authService.createToken(member);
        LoginInfoDto loginInfoDto = new LoginInfoDto(
                member.getId(),
                providerName,
                userInfo.getUserName(),
                member.getNickName());
        return new Oauth2Response(loginInfoDto, token);
    }
```

이를 `oauth2` 서버에 유저 정보를 가져오는 `Oauth2Client`와  
회원을 확인하는 `AuthService`로 나누어 책임을 분리하였음

```java
public class Oauth2Client {

    private final ProviderFactory providerFactory;

    public Oauth2UserInfo authenticate(String provider, String code) {
        Oauth2AccessToken accessToken = getAccessToken(provider, code);
        return getUserInfo(provider, accessToken.token());
    }
}

//-----------------------------------------------------------------------------------------------------------

public class AuthService {

    private final MemberRepository memberRepository;
    private final Oauth2Client oauth2Client;
    private final JwtProvider jwtProvider;

    public AuthResponse authenticate(String provider, String code) {
        Oauth2UserInfo oauth2UserInfo = oauth2Client.authenticate(provider, code);

        Member member = memberRepository
                .findByProviderAndProviderId(oauth2UserInfo.getProvider(), oauth2UserInfo.getProviderId())
                .orElseGet(() -> memberRepository.save(
                        Member.fromOauth2(
                                oauth2UserInfo.getProvider(),
                                oauth2UserInfo.getProviderId())));

        String token = jwtProvider.generateToken(member);

        return AuthResponse.of(getRedirect(member), token);
    }
}
```
결과적으로 각 메서드의 역할이 분명해졌고,  
`Oauth2Client mocking`으로 인해 테스트가 용이해짐

## 시연

### 메인페이지
![image (1)](https://github.com/pdfolio/server/assets/71807768/305f47b5-c57a-4310-8758-e246b71c1114)


### 게시글 상세보기
![image (2)](https://github.com/pdfolio/server/assets/71807768/bcc927ff-11e6-4024-aac0-486cbe66ab6b)

