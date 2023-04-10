# numble_insta
2023-03 넘블 인스타그램 서버 API 프로젝트
순서 | 목차 
| --- | ---
| 0 | 작업 기간
| 1 | 아키텍처 구조
| 2 | DB 스키마 & ERD
| 3 | API 문서
| 4 | 회고록 & 기타 기록
| 5 | 사용 기술
| 6 | 폴더 구조
## 작업 기간
23.03.24 ~ 23.04.13
## 아키텍처 구조
## DB 스키마 & ERD
## API 문서
## 회고록 & 기타 기록
[velog Numble 시리즈 글](https://velog.io/@devholic22/series/%EB%84%98%EB%B8%94%EC%B1%8C%EB%A6%B0%EC%A7%80)
## 사용 기술
* Spring Boot 3.0.5
* Gradle
* Java 17
* Spring Web
* Lombok
* Spring Data JPA
* MySQL 8.0.31
* JWT
* Spring Security
* Github Actions
* AWS Elastic beanstalk
* AWS EC2
* AWS RDS
## 폴더 구조
```bash
📂 instagram
├── 📂 .ebextensions
│   ├── 📜 00-makeFiles.config
│   └── 📜 00-set-timezone.config
├── 📂 .github
│   └── 📂 workflows
│       └── 📜 deploy.yml
├── 📂 .gradle
│   └── 📂 .....
├── 📂 .idea
│   └── 📂 .....
├── 📂 .platform
│   └── 📂 nginx
│       └── 📜 nginx.conf
├── 📂 gradle
│   └── 📂 wrapper
│       ├── 📜 gradle-wrapper.jar
│       └── 📜 gradle-wrapper.properties
├── 📂 out
│   └── 📂 production
│       └── 📂 .....
└── 📂 src
    ├── 📂 main
    │   ├── 📂 generated
    │   ├── 📂 java
    │   │   └── 📂 com
    │   │       └── 📂 numble
    │   │           └── 📂 instagram
    │   │               ├── 📂 config
    │   │               │   └── 📜 SecurityCinfig
    │   │               ├── 📂 controller
    │   │               │   ├── 📜 ChatRoomController
    │   │               │   ├── 📜 CommentController
    │   │               │   ├── 📜 FeedController
    │   │               │   ├── 📜 FollowController
    │   │               │   ├── 📜 HomeController
    │   │               │   ├── 📜 MessageController
    │   │               │   ├── 📜 PostController
    │   │               │   ├── 📜 ReplyController
    │   │               │   └── 📜 UserController
    │   │               ├── 📂 dto
    │   │               │   ├── 📂 comment
    │   │               │   │   ├── 📜 CommentDto
    │   │               │   │   ├── 📜 EditCommentDto
    │   │               │   │   └── 📜 GetCommentDto
    │   │               │   ├── 📂 feed
    │   │               │   │   └── 📜 GetFeedDto
    │   │               │   ├── 📂 jwt
    │   │               │   │   └── 📜 TokenDto
    │   │               │   ├── 📂 message
    │   │               │   │   ├── 📜 GetMessageDto
    │   │               │   │   └── 📜 MessageDto
    │   │               │   ├── 📂 post
    │   │               │   │   └── 📜 PostDto
    │   │               │   ├── 📂 reply
    │   │               │   │   ├── 📜 EditReplyDto
    │   │               │   │   ├── 📜 GetReplyDto
    │   │               │   │   └── 📜 ReplyDto
    │   │               │   ├── 📂 room
    │   │               │   │   └── 📜 GetRoomDto
    │   │               │   └── 📂 user
    │   │               │       ├── 📜 EditUserDto
    │   │               │       ├── 📜 LoginDto
    │   │               │       ├── 📜 UserDto
    │   │               │       └── 📜 UserInfoDto
    │   │               ├── 📂 entity
    │   │               │   ├── 📜 ChatRoom
    │   │               │   ├── 📜 Comment
    │   │               │   ├── 📜 Follow
    │   │               │   ├── 📜 Message
    │   │               │   ├── 📜 Post
    │   │               │   ├── 📜 Reply
    │   │               │   └── 📜 User
    │   │               ├── 📂 exception
    │   │               │   ├── 📜 ExceptionResponse
    │   │               │   ├── 📜 AlreadyExistUserException
    │   │               │   ├── 📜 AlreadyExitedUserException
    │   │               │   ├── 📜 AlreadyFollowException
    │   │               │   ├── 📜 ChatRoomException
    │   │               │   ├── 📜 ExitedTargetUserException
    │   │               │   ├── 📜 ExitedUserException
    │   │               │   ├── 📜 NotFollowException
    │   │               │   ├── 📜 NotPermissionException
    │   │               │   ├── 📜 NotQualifiedDtoException
    │   │               │   ├── 📜 NotSearchedTargetException
    │   │               │   ├── 📜 SelfFollowAPIException
    │   │               │   └── 📜 SelfMessageException
    │   │               ├── 📂 jwt
    │   │               │   ├── 📜 JwtAccessDeniedHandler
    │   │               │   ├── 📜 JwtAuthenticationEntryPoint
    │   │               │   ├── 📜 JwtFilter
    │   │               │   ├── 📜 JwtSecurityConfig
    │   │               │   └── 📜 TokenProvider
    │   │               ├── 📂 repository
    │   │               │   ├── 📜 ChatRoomRepository
    │   │               │   ├── 📜 CommentRepository
    │   │               │   ├── 📜 FollowRepository
    │   │               │   ├── 📜 MessageRepository
    │   │               │   ├── 📜 PostRepository
    │   │               │   ├── 📜 ReplyRepository
    │   │               │   └── 📜 UserRepository
    │   │               ├── 📂 service
    │   │               │   ├── 📜 ChatRoomService
    │   │               │   ├── 📜 CommentService
    │   │               │   ├── 📜 CustomUserDetailsService
    │   │               │   ├── 📜 FeedService
    │   │               │   ├── 📜 FollowService
    │   │               │   ├── 📜 MessageService
    │   │               │   ├── 📜 PostService
    │   │               │   ├── 📜 ReplyService
    │   │               │   └── 📜 UserService
    │   │               ├── 📂 util
    │   │               │   ├── 📜 SecurityUtil
    │   │               │   └── 📜 UserUtil
    │   │               └── 📜 InstagramApplication        
    │   └── 📂 resources
    │       ├── 📂 static
    │       ├── 📂 templates
    │       └── 📜 application.yml
    │
    ├── 📂 test
    │   └── 📂 java
    │       └── 📂 com
    │           └── 📂 numble
    │               └── 📂 instagram
    │                   └── 📜 InstagramApplicationTests
    ├── 📜 gradlew.bat
    ├── 📜 .gitignore
    ├── 📜 build.gradle
    ├── 📜 settings.gradle
    ├── 📜 HELP.md
    ├── 📜 gradlew
    └── 📜 Procfile
``` 