# PHC-WORLD
### 프로젝트 특징
> * spring-boot 2.0.3.RELEASE
> * JPA
> * Maven
> * h2-database
> * bootstrap
> * jQuery
> * Ajax
> * Lombok
> * mustache template
> * devtools (liveReload)
> * Junit 테스트
***
### 프로젝트 기능
* 게시판을 이용할 수 있는 사이트입니다.
* 사이트의 모든 기능은 회원만 이용 가능합니다.
* 게시판은 두 종류입니다.
  1. 회원이 이용할 수 있는 자유게시판
  2. 자신의 글만 모아놓은 일기 게시판
* 회원간에는 메세지를 주고 받을 수 있습니다. (업데이트 필요)
* 일기 게시판은 회원 email을 주소에 타이핑하여 다른 회원의 글을 볼 수 있습니다. (업데이트 필요)
* 자유게시판의 정렬은 bootstrap dataTable을 사용하였습니다.
* 일기게시판의 정렬은 Page를 사용하였습니다. 
* 로그인 후 첫페이지는 로그인 한 User의 행적(타임라인)을 최신순으로 5개를 나타냈습니다. 
* 페이지 상단에 알림 메뉴를 클릭했을 시 로그인한 User가 쓴 게시물(자유게시판, 일기게시판)에 좋아요와 댓글이 달렸을 경우 나타납니다.
* 타임라인은 자유게시판 글쓰기, 자유게시판 댓글, 일기게시판 글쓰기, 일기게시판 댓글, 일기게시판 좋아요를 보여줍니다.
***
### 프로젝트 구현 설명
* MVC패턴 - Controller로 요청을 받고 Service Interface에서 선언한 메서드를 구현한 ServiceImpl에서 Repository로 처리 후 Controller에서 View로 응답합니다. (추상화 예정)
* 모든 Service, Repository, Controller에 대한 Junit테스트를 구현했습니다.
  * Service 테스트에서는 @MockBean을 사용하여 테스트하였습니다.
  * Repository 테스트에서는 기본 CRUD테스트와 새로 선언한 메서드를 테스트하였습니다.
  * Controller에서는 @MockBean과 WebMvc를 사용하여 테스트하였습니다.
  * User만 Validation 테스트를 위해 UserTest가 있습니다.
  * (Mockito에 대해 더 공부해야한다.)
* FreeBoardAnswer와 DiaryAnswer, Message는 RestController로 구현하였습니다. 
* RestController에서 로그인 회원이 없을 경우, 찾는 회원이 없을 경우, Message에 대해 자기자신에게 보낼경우 Exception으로 에러 메세지를 보내도록 하였습니다.
* User의 경우 탈퇴(삭제)를 구현하지 않았습니다.
* User의 Controller는 profile페이지에서 ajax로 타임라인을 불러오기 위해 RestController가 하나 더 있으며 타임라인을 불러오는 기능만 있습니다.
* Diary도 좋아요를 ajax로 처리하기 위해서 RestController가 하나 더 있으며 좋아요의 개수를 가져오는 기능만 있습니다.
* 회원가입을 하면 랜덤값을 가입요청한 메일주소로 보내고 받은 메일의 링크를 클릭하면 랜덤값을 저장된 값과 비교해 회원가입 승인을 합니다.
* 타임라인과 알림은 생성과 삭제만 사용하기 때문에 수정 기능은 구현하지 않았습니다.
* 자유게시판, 일기게시판, 프로필 사진에서 이미지 업로드를 사용할 수 있으며 ajax로 서버에 요청해서 MultipartFile을 이용해 파일을 쓰고 정보를 db에 저장 후 해당 정보를 가져와 본문 또는 회원 프로필에 적용하였습니다.
* 이미지의 용량은 5MB로 javascript(jQuery)로 제한하였습니다.
* 자유게시판과 일기게시판에 글쓰기는 다음에디터를 사용하였습니다.
***
### 업데이트 예정
* 댓글 수정기능
* 회원탈퇴 요청시 회원을 삭제하지 않고 자격정지
* 이미지가 있는 글을 삭제할 때 이미지파일도 삭제
* 등록된 글의 회원을 클릭했을 때 회원페이지로 이동
* 회원정보를 나타내는 페이지에서 다이어리 글로 이동
***
### User 설명

**Entity**

* User는 Entity이므로 @Entity 어노테이션을 사용하였습니다.
* 모든 필드에 getter와 setter, 서로 다른 User인지 비교하기위해  EqualsAndHashCode, log로 User를 보기위해 toString을 사용하기 위해 Lombok의 @Data어노테이션을 사용하였습니다.
* User의 toString은 password는 제외시켜 log에 나타나지 않게 하였습니다.
* 기본 생성자 @NoArgsConstructor 어노테이션, 모든 필드의 생성자 @AllArgsConstructor를 사용하였습니다.
* User를 생성할 때 편리하게 사용하기위해 @Builder어노테이션을 사용하였습니다.
* 필드
  * id
    * primary key @Id어노테이션 사용
      * 읽기, 수정, 삭제를 위한 고유키
    * 자동증가를 위해 @GenaratedValue 어노테이션 사용
      * pk에 대한 전략으로 데이터베이스에 위임 IDENTITY (mysql과 같이 h2-database에서도 가능)
  * email
    * 이메일
    * spring-boot-starter-validation에 있는 hibernate validation의 @Email 어노테이션 사용
      * Email형식이 아닐 경우 "이메일 형식이 아닙니다." errorMessage 전달
    * 회원가입 때 email주소를 반드시 기입하도록 하기위해 @NotNull어노테이션 사용
    * pk값 이외에 email중복 방지를 위해 @Column 어노테이션의 unique 설정
  * password
    * 비밀번호
    * 회원가입 때 password를 반드시 기입하도록 하기위해 @NotNull어노테이션 사용
    * 짧은 비밀번호를 막기위해 @Size 어노테이션 사용
      * 최소 4자 이상
      * 4자 미만일 경우 errorMessage 전달
    * Json으로 변경하고 사용할 때 password를 나타나지 않게 하기위해 @JsonIgnore 어노테이션 사용
    * 보안을 위해 회원가입시 SHA-256을 사용해 변경후 저장
    * 로그인 시에도 SHA-256으로 변경 후 확인
      * SHA-256으로 변환하는 클래스를 따로 관리
  * name
    * 게시물 작성자를 나타내기 위해 반드시 회원가입 때 기입하도록 @NotNull 어노테이션 사용
    * 짧은 이름 방지 및 긴 이름 방지를 위해 @Size 어노테이션 사용
    * 이름에 javascript 명령어 등으로 장난치지 않게 @Pattern 어노테이션으로 정규표현식 적용
  * authority
    * 회원 권한
    * 회원 권한으로 게시물을 삭제할 수 있다.
    * 예시 회원
      * email : test@test.test(관리자 권한) password : test
      * email : test2@test.test(회원 권한) password : test2
    * email이 pakoh200@naver.com(나)이면 회원권한을 관리자로 변경 (다른 방법으로 생각할 필요 있음)
  * createDate
    * 가입한 날짜(@CreatedDate 업데이트 필요)
  * profileImage
    * 회원 프로필 이미지명
    * 회원가입 때는 기본 이미지 명을 사용
    * 프로필 이미지를 등록했을 경우 프로필 이미지명으로 변경
    * 이미지를 사용할 때는 이미지 경로 앞부분 + 이미지명으로 사용
* User간의 확인보다는 pk값인 id를 비교하기 위해 만든 메서드
* password를 비교하기 위해 만든 메서드
  * 프로젝트가 시작될 때 예시회원의 비밀번호는 SHA-256으로 변환되지 않고 db에 저장되기 때문에 예시회원의 비밀번호 인 "test" 또는 "test2"는 바로 확인 (변경해야함)
  * 이외에 패스워드들은 SHA-256으로 변환 후 확인  
* User정보를 수정했을 때 사용되는 메서드