# PD-Folio

## 서비스

### Playdata 수강생들끼리 만든 미니 프로젝트를 공유하고,

### 사이드 프로젝트 및 스터디를 구인하는 사이트

작업기간 : 8월 15일 ~ 8월 25일

---

## 기술스택

### 백엔드

- Java 17
- Spring Boot 3.x.x, Spring Security, Spring Data Jpa
- MySQL
- JPA, Query Dsl
- Oauth2, JWT
- Jacoco, JUnit


### 프론트엔드

- Javascript
- React, Bootstrap

---

## ERD

![gather](https://github.com/ktk8916/pdfolio-refactoring/assets/71807768/f68790f2-86f6-42f4-bc93-c9f4b9f2bb8a)

---

## 중요하게 생각한 점

### 쿼리 고민하기

- 여러 조건에 따른 조회 쿼리 성능 최적화
- N + 1 문제에 대한 해결

---

## 개선점

### 1. 테스트 코드

- 테스트 코드가 미비해서 서로가 작성한 기능이 온전히 동작하는지 명확하게 알 수 없음
- 프로젝트에 대한 전체적인 리팩토링을 진행하고 싶으나 사이드 이펙트 예상이 쉽지 않음

### 2. 게시글 이미지 처리

- 현재는 화면에서 작성된 게시글과 사진을 base64 인코딩과 md양식으로 변환하여 RDB에 통으로 저장
- 이를 스토리지 서비스를 사용하도록 변경

---

## 리팩토링

### 1. %keyword% 쿼리 개선

#### 문제상황

- 게시물에 대한 검색을 위해 title, content 컬럼에 대해서 %keyword% 검색을 하고있었음
- 이는 인덱스를 활용하지 못하고 모든 테이블에 대해 검색하는 풀스캔이 일어나서 성능 이슈가 생길 수 있음
- 작성한 게시글 200개와 임의로 생성한 30만개의 게시글에 대해 테스트한 결과 평균 조회시간이 8초 정도 소요

#### 해결

- 이를 해결하기 위해 MySQL 에서 지원하는 fulltext index와 match against 쿼리를 사용
- 게시글은 대부분 한국어로 작성되기 때문에 내장 Ngram Parser를 사용하여 title, content에 대해 2글자 단위로 fulltext index 생성
- Query DSL 에서 match ~ against 라는 MySQL 방언을 사용하기 위해 `CustomFunctionContributor` 등록


평균 `8000ms` 에서 `16ms` 으로 조회 성능 향상


### 2. fetch join paging

#### 문제상황

![image](https://github.com/ktk8916/pdfolio-refactoring/assets/71807768/b277ef40-752f-4141-9469-39d5de34ab02)

- 프로젝트에서 사용하는 모집글 테이블은 하나의 게시글에 여러개의 기술스택이 달릴 수 있는 1 : N 구조였음  
- 특정 기술스택에 맞는 모집글을 가져오기 위해 게시글과 기술스택을 fetch join 하여 where 쿼리 실행
- fetch join 결과에 paging 처리 시 Hibernate가 모든 데이터를 메모리로 가져와서 paging 하여 `firstResult/maxResults specified with collection fetch; applying in memory` 발생

#### 해결

- 모집글에 가지고 있는 skills에 대해 @BatchSize를 지정하여 즉시로딩

   
### 3. 의존성 분리

#### 문제상황

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

#### 해결

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

### 4. 테스트 코드 작성

 - 테스트 코드 60개 작성
 - Jacoco 테스트 커버리지 기존 21% -> 48%
 - 꾸준하게 진행 중

## 시연

### 메인페이지
![image (1)](https://github.com/pdfolio/server/assets/71807768/305f47b5-c57a-4310-8758-e246b71c1114)


### 게시글 상세보기
![image (2)](https://github.com/pdfolio/server/assets/71807768/bcc927ff-11e6-4024-aac0-486cbe66ab6b)

