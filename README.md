# numble_insta
## 2023-03 넘블 인스타그램 서버 API 프로젝트
본 브랜치는 23년 3월에 진행했던 넘블 인스타그램 서버 API 프로젝트를 전체적으로 리팩터링 해보는 브랜치입니다.  
당시 진행했던 코드 (~~흑역사~~)는 main 브랜치를 확인하시면 됩니다.
### 프로젝트 진행 당시의 문제점들
프로젝트 진행을 했던 때 가지고 있던 문제점들은 다음과 같았습니다.
#### 1. CI/CD 실패
CI / CD에 대한 개념을 학습하지 못한 채로 프로젝트를 진행했었으며, 때문에 최종적으로 완료하지 못했었습니다.  
현재 (23년 8월) 또한 CI / CD에 대한 개념을 완벽히 알고 있다고 확신하지는 못하지만, 적어도 기능 구현은 할 수 있기에 이를 개선할 것입니다.
#### 2. 테스트 코드의 부재
테스트 코드를 아예 작성하지 않은 채로 코드를 제출했었습니다. 기능을 개발할 때 마다 관련 테스트 코드를 작성하여 발생할 수 있는 예외를 미리 방지하는 훈련을 할 것입니다.
#### 3. 깔끔하지 않은 응답 방식
과거 진행했던 방식에서는 예외 처리를 컨트롤러에서 매번 try-catch 했기 때문에 컨트롤러가 매우 오염되었습니다. 예시를 보겠습니다.
```java
@PutMapping("/{id}")
public ResponseEntity<?> editComment(@RequestBody EditCommentDto editCommentDto, @PathVariable Long id) {
    try {
        return ResponseEntity.ok(commentService.edit(editCommentDto, id, userUtil.getLoggedInUser()));
    } catch (ExitedUserException |
        NotQualifiedDtoException |
        NotSearchedTargetException |
        NotPermissionException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
               .body(exceptionResponse);
        }
    }
```
* 발생할 수 있는 예외를 놓치게 되면 catch에 걸리지 않게 됩니다.
* 에러가 어떤 경우든지간에 HttpStatus가 BAD_REQUEST로 고정됩니다.
* 컨트롤러 코드를 쓸 때 마다 try-catch를 써야 합니다.

이 문제를 개선하기 위해 개인 블로그에서도 기록해 둔 API 응답 방식을 적용할 것입니다. `@ControllerAdvice` 등을 적절히 활용합니다.
#### 4. 전형적인 웹 계층에 따른 패턴 고수
공식마냥 service, repository, controller, model에 대한 패키징만 하였습니다. 때문에 기능이 늘어날수록 한 패키지 안에 여러 파일들이 존재했었고, 이에 따라 쉽게 원하는 파일을 탐색하기 어려웠습니다. 때문에 이번에는 도메인 중심으로 패키징을 할 생각입니다.
#### 5. 클린코드하지 않은 문제점
* 넘블에서 제공한 클린코드 규칙을 제대로 준수하지 못했습니다.
* 구글의 클린 코드 규칙도 준수하지 않았습니다.
* DTO 관련 파일 네이밍도 깔끔하지 못했습니다.

이 외에도 추가적인 문제점들이 있었을 것입니다. 시간이 오래 걸리더라도 천천히 기능을 새롭게 구현해보겠습니다.
### 프로젝트 요구사항
당시 프로젝트에서 요구했던 요구사항들은 [이곳](https://thoughtful-arch-8c2.notion.site/Spring-c83f01ab221a4166a2713120728aa552)에서 보실 수 있습니다.
