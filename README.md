# PHC-WORLD

### 프로젝트 특징

> * JAVA 8
> * spring-boot 2.1.13.RELEASE
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

* 이미지파일 경로 외부에 설정
* 회원탈퇴 요청시 회원을 삭제하지 않고 자격정지
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
* 날짜 형식을 현재 시간과 등록된 시간을 비교해 1분이내에 쓰여진 글이면 "방금전", 1분이 지났을 때부터 "몇분 전", 1시간이후로는 "몇시간 전", 24시간 이후로는 " 월 일", 년도 다를경우는 " 년 월 일"을 출력
* 권한을 확인하는 메서드
* pakoh200@naver.com(나)일 경우 권한을 변경하는 메서드


**service**

* UserService는 interface없이 바로 구현
* User 생성 메서드는 builder()를 사용하지 않고 setter를 사용
* 등록된 Email이 있는지 확인하기 위해 Email로 User정보를 가져오는 메서드
* 주소로 id값이 넘어오면 id로 User정보를 가져오는 메서드
* User 정보를 수정하는 메서드
* profile 이미지를 변경하는 메서드

**Controller**

1. User의 timeline을 profile페이지에서 ajax로 보기 위해 만든 RestController
2. 회원가입, 로그인, 회원 정보 수정 Controller
   * 생성 Create
     * 회원가입시 넘어오는 User 정보를 유효성 체크를 하고 Entity의 설정과 다를 경우 Model에 담아 view페이지에서  errorMessage를 출력
     * 유효성 error가 없다면 넘어온 email로 User정보를 찾아서 있으면 errorMessage를 Model에 담아 view페이지에 출력
     * email로 찾은 User가 없다면 User생성
     * email과 랜덤 값을 db에 저장하고 가입한 email로 저장된 랜덤값을 링크로 보내 클릭하면 db에 저장된 email과 랜덤값을 확인 후 맞으면 승인 로그인할 때 승인여부 확인
     * 전부 통과되면 로그인페이지로 이동
   * 회원가입 form 과 login form
     * 이미 로그인이 되어이쓰면 dashboard페이지로 이동
     * 로그인이 되어있지 않으면 각각 페이지로 이동
   * 로그인
     * email과 password가 있는 모델 사용
     * email로 회원 검색해서 없으면 errorMessage를 로그인 form에 전달 후 출력
     * email로 회원 검색해서 있으면 password비교 후 틀리면 errorMessage를 로그인 form에 전달 후 출력
     * 이메일로 가입 승인을 받았는지 승인 값을 확인하고 승인되지 않았으면 errorMessage를 로그인 form에 전달 후 출력
     * 전부 통과했으면 HttpSession으로 User 정보를 저장
     * 로그인 User가 받은 메세지 중 일지 않은 메세지 개수 모델에 담아 header-navbar.html에서 사용
     * 읽지 않은 받은 메세지에서 상위 5개만 모델에 담아 header-navbar.html에 메세지에 사용
     * 알림을 가져와서 header-navbar.html에 사용
   * 로그아웃
     * 저장된 Session정보 삭제 후 로그인 form으로 redirect 이동
   * 회원 정보 수정
     * 로그인 User가 없으면 errorMessage를 로그인 form에 전달 후 출력
     * 요청 User id와 로그인 한 User id와 비교 후 같지 않으면 로그인 form에 errorMessage를 전달 후 출력
     * 전부 통과하면 Model에 로그인한 User정보를 담아 update form에 전달
     * 가져온 정보를 수정하고 update를 요청하면 로그인 한 User가 있는지 확인 후 없으면 errorMessage를 로그인 form으로 전달 후 출력
     * update요청한 id와 로그인 한 User의 id를 비교 후 같지 않으면 errorMessage를 로그인 form에 전달 후 출력
     * 수정하는 값들을 유효성 검사 후 틀리면 errorMessage를 redirect 한 update form으로 전달 후 출력
     * 전부 통과하면 update 수행 후 dashboard로 redirect
   * User 프로필 페이지
     * User 프로필 페이지는 타임라인, 받은 메세지, 보낸 메세지를 볼 수 있다.
     * 로그인이 되어있지 않으면 errorMessage를 로그인 form에 전달 후 출력
     * 로그인 User가 저장한 타임라인을 가져와서 1개 이상이면 show more 버튼을 나오게 model에 boolean값을 담아 profile페이지에서 사용
     * 넘어온 id와 로그인 한 User id와 비교 후 같으면 보낸 메시지와 받은 메세지를 가져와서 model에 담아 profile페이지에서 사용

***

### FreeBoard 설명

**Entity**

* FreeBoard는 Entity이므로 @Entity 어노테이션을 사용하였습니다.
* 모든 필드에 getter와 setter, 서로 다른 FreeBoard인지 비교하기위해  EqualsAndHashCode, log로 FreeBoard를 보기위해 toString을 사용하기 위해 Lombok의 @Data어노테이션을 사용하였습니다.
* 기본 생성자 @NoArgsConstructor 어노테이션, 모든 필드의 생성자 @AllArgsConstructor를 사용하였습니다.
* FreeBoard를 생성할 때 편리하게 사용하기위해 @Builder어노테이션을 사용하였습니다.
* 생성된 날짜와 수정된 날짜를 자동으로 관리하기 위해 @EntityListeners(AuditingEntityListener.class) 어노테이션을 사용하였습니다.
* 필드
  * id
    * primary key @Id어노테이션 사용
      * 읽기, 수정, 삭제를 위한 고유키
    * 자동증가를 위해 @GenaratedValue 어노테이션 사용
      * pk에 대한 전략으로 데이터베이스에 위임 IDENTITY
  * writer
    * 한명의 User가 다수의 자유게시판 게시물을 작성 할 수 있어서 @ManyToOne으로 User와 매핑한다.
    * User에서는 FreeBoard를 사용하지 않기 때문에 매핑을하지 않았다.
    * FreeBoard에서 writer는 User의 외래키를 갖기 때문에 @JoinColumn을 foreignKey를 설정한다.
  * titile
    * 게시물의 제목
  * contents
    * 게시물의 내용
    * 게시물의 내용이 많을 수도 있기 때문에 @Lob어노테이션 사용
  * icon
    * 게시물에 사진이 첨부되면 freeboard_form.html의 icon hidden 태그의 값을 변경하여 저장하게 한다.
    * 변경하는 코드는 resource/static/daumeditor/js/trex/attachbox/attachbox_ui.js 파일에서 이미지가 추가될 때와 삭제할 때 값이 변하게 코드를 추가했다.
  * badge
    * 모든 자유게시판을 불러올 때 현재시간을 기준으로 등록된 글이 24시간내에 등록된 글이면 "New"를 저장하여 새롭게 등록된 글인지 확인하는 필드
    * 24시간이 지나면 사용하지 않는 필드다.(다른 방법을 생각할 필요있음)
  * createDate
    * 등록한 날짜를 기록하는 필드
  * updateDate
    * 수정한 날짜를 기록하는 필드
  * count
    * 해당 게시물의 조회수
  * List<FreeBoardAnswer> freeBoardAnswer
    * 해당 게시물의 댓글
    * 자유게시판 게시글 하나에 여러개의 댓글이 달리기 때문에 자유게시판과의 관계는 @OneToMany
    * 무한루프에 빠지지 않기 위해 @JsonBackReference어노테이션 사용 (공부할 것)
    * 자유게시판 게시글을 지우면 해당 게시글의 댓글도 지우기 위해 cascade REMOVE 설정

* 해당 게시물의 댓글의 개수를 가져오기 위해 freeBoardAnswer의 size()를 가져와서 String으로 변경하는 메서드
* 게시글을 읽어올 때 조회수를 올리는 메서드
* 날짜 형식을 변경하는 메서드(User createDate와 같음)
* 게시물을 update하는 메서드
* 게시물 글쓴이와 로그인 User와 비교하는 메서드. 만약 글쓴이 이외에 회원 권한을 가진 User가 게시물을 삭제할 때 쓰인다.

**FreeBoardRequest**

* FreeBoard에 대한 요청을 보낼 때 사용할 DTO
* 필드
  * id
    * 수정을 요청할 때 사용하기 위한 id
  * title
    * 생성을 요청할 때 사용할 제목
  * contents
    * 생성과 수정을 요청할 때 사용할 내용
  * icon
    * 생성과 수정을 요청할 때 이미지가 있는지 없는지 사용할 icon

**FreeBoardResponse**

* FreeBoard를 생성하고 응답으로 건내줄 DTO
* 필드
  * id
    * 수정 및 삭제를 위해 생성된 FreeBoard의 id값
  * writer
    * FreeBoard의 글쓴이 정보
  * title
    * FreeBoard의 제목
  * contents
    * FreeBoardAnswer의 내용
  * icon
    * FreeBoard에 이미지가 있으면 Bootstrap icon을 저장
  * badge
    * FreeBoard의 등록 날짜가 24시간 내의 글이면 "New"를 저장
  * updateDate
    * FreeBoard의 글이 등록 또는 수정된 날짜
  * count
    * FreeBoard의 조회수
  * countOfAnswers
    * FreeBoard의 댓글 개수
  * freeBoardAnswerList
    * FreeBoard의 댓글을 FreeBoardAnswerApiResponse의 리스트로 저장


**service**

* 자유게시판의 모든 게시물을 가져온다.
  * 최근 등록한 게시물은 가져온 게시물 List의 마지막에 저장되기 때문에 가져온 게시물 List 끝에 해당하는 마지막 처음까지(index 0번) 확인하여 현재 시간을 기준으로 24시간내의 등록된 글이면 badge필드에 "New"를 넣는다.
  * 만약 확인하는 중에 "New" badge를 넣지 않는 상황이 발생하면 24시간내의 글이 더이상 없는 것이기 때문에 검사할 필요가 없으니 반복문을 빠져나온다.
  * badge를 설정한 List를 stream()메소드와 람다식을 사용하여 FreeBoardResponse에 데이터를 넣어 List로 만들어 반환한다.
* 로그인 한 유저의 정보와 입력받은 자유게시판 정보를 Lombok의 builder()를 사용해서 db에 저장 후 저장된 자유게시판 게시물을 timeline에 @OneToOne관계로 저장하고 FreeBoardResponse로 변환 후 반환한다.
* 자유게시판 id를 가지고 id에 해당하는 게시물을 가져와서 FreeBoardResponse로 변환 후 반환한다.
* 자유게시판 id를 가지고 id에 해당하는 게시물을 가져와 조회수를 증가시키고 db에 저장 후 FreeBoardResponse로 변환하여 반환한다.
* 입력받은 자유게시판 정보로 update하고 db에 저장 후 FreeBoardResponse로 변환하여 반환한다.
* timeline과 관계매핑을 하지 않아서 삭제할 게시물의 타임라인을 삭제하고 해당 자유게시판 게시물에 달린 댓글들의 타임라인과 알림을 삭제하고 자유게시판의 게시물을 삭제한다.
* User의 정보로 User가 등록한 게시물을 가져온다.
* FreeBoard의 정보를 FreeBoardResponse로 변환하는 메소드


**controller**

* 자유게시판의 모든 게시물을 가져와서 model에 담아 freeboard페이지에서 사용한다.
* 자유게시판 글쓰기 페이지
  * 로그인을 하지 않았으면 로그인페이지로 이동한다.
* 게시물 생성
  * 로그인 한 유저와 넘겨받은 자유게시판 정보로 db에 저장 후 생성한 게시물 상세페이지로 이동한다.
* 자유게시판 상세페이지
  * 우선 조회수를 증가시키고 로그인 한 유저가 있는지 해당 게시물의 작성자와 로그인한 유저가 같은지 그리고 관리자 권한인지 확인하여 model에 boolean값을 넣어 해당 페이지에서 버튼을 다르게 나오게 한다.
* 수정페이지
  * 로그인한 유저가 있는지 확인하고 수정을 원하는 게시물의 작성자와 로그인한 유저가 같은지 확인하고 같지 않으면 errorMessage를 담아 로그인페이지에 나타낸다. 로그인 한 유저가 작성자라면 수정페이지로 이동하여 수정할 수 있다.
* 수정 요청
  *  다시 한번 로그인을 했는지 확인하고 수정 요청한 글의 작성자와 로그인한 유저와 같으면 수정을 하고 해당 게시물의 상세페이지로 이동한다.
* 게시물 삭제
  *  로그인 한 유저가 있는지 확인하고 로그인 한 유저가 해당 게시물의 작성자와 같은지 확인해서 같다면 게시물을 삭제하고 list페이지로 이동한다.

***

### FreeBoardAnswer 설명

**Entity**

* FreeBoardAnswer는 Entity이므로 @Entity 어노테이션을 사용하였습니다.
* 모든 필드에 getter와 setter, 서로 다른 FreeBoardAnswer인지 비교하기위해  EqualsAndHashCode, log로 FreeBoardAnswer를 보기위해 toString을 사용하기 위해 Lombok의 @Data어노테이션을 사용하였습니다.
* 기본 생성자 @NoArgsConstructor 어노테이션, 모든 필드의 생성자 @AllArgsConstructor를 사용하였습니다.
* 생성된 날짜와 수정된 날짜를 자동으로 관리하기 위해 @EntityListeners(AuditingEntityListener.class) 어노테이션을 사용하였습니다.
* FreeBoardAnswer를 생성할 때 편리하게 사용하기위해 @Builder어노테이션을 사용하였습니다.
* 필드
  * id
    * primary key @Id어노테이션 사용
      * 읽기, 수정, 삭제를 위한 고유키
    * 자동증가를 위해 @GenaratedValue 어노테이션 사용
      * pk에 대한 전략으로 데이터베이스에 위임 IDENTITY
  * writer
    * 한명의 User가 다수의 자유게시판 댓글을 작성 할 수 있어서 @ManyToOne으로 User와 매핑한다.
    * User에서는 FreeBoardAnswer를 사용하지 않기 때문에 매핑을하지 않았다.
    * FreeBoardAnswer에서 writer는 User의 외래키를 갖기 때문에 @JoinColumn을 foreignKey를 설정한다.
  * freeBoard
    * FreeBoardAnswer는 FreeBoard의 댓글이기 때문에 어떤 글의 댓글인지 @ManyToOne으로 매핑
    * 외래키이기 때문에 @JoinColumn으로 ForeignKey를 설정하고 반드시 값이 있어야하기 때문에 nullable을 false로 설정한다.
    * 순환참조 무한루프에 빠지지 않기위해 @JsonIgnoreProperties와  @JsonManagedReference 어노테이션을 설정(검색으로 찾아서 확실하게 알아봐야함)
  * contents
    * 댓글의 내용
    * 댓글의 내용이 많을 수도 있기 때문에 @Lob어노테이션 사용
  * createDate
    * 등록한 날짜를 기록하는 필드
  * updateDate
    * 수정한 날짜를 기록하는 필드
* 날짜 형식을 변경하는 메서드
* 작성자와 넘어오는 User가 같은지 확인하는 메서드
* 댓글을 수정하는 메서드

**FreeBoardAnswerApiResponse**

* FreeBoardAnswer를 생성하고 응답으로 건내줄 DTO
* 필드
  * id
    * 수정 및 삭제를 위해 생성된 FreeBoardAnswer의 id값
  * writer
    * FreeBoardAnswer의 글쓴이 정보
  * contents
    * FreeBoardAnswer의 내용
  * freeBoardId
    * 수정, 삭제에 사용할 FreeBoard id
    * 순환참조 방어를 위해 id만 저장
  * countOfAnswers
    * FreeBoardAnswer를 생성한 FreeBoard의 댓글 개수
    * 순환참조를 방어하기위해 String으로 저장
  * createDate
    * FreeBoardAnswer가 생성된 날짜
    * LocalDateTimeUtils로 변환한 값을 저장


**service**

* 댓글 생성
  * 로그인 유저(작성자), 댓글을 작성하는 자유게시판 게시글의 id, 댓글의 내용을 받아서 FreeBoardAnswer의 builer()를 사용하여 FreeBoardAnswer를 생성
  * 댓글의 내용은 html로 보여지기 때문에 String의 줄바꿈을 html 줄바꿈 태그로 변경
  * 생성한 FreeBoardAnswer를 FreeBoardAnswerApiResponse builder()로 FreeBoardAnswerApiResponse 생성 후 리턴
  * db에 저장 후 timeline생성
  * 댓글을 단 자유게시판 작성자와 댓글의 작성자가 다르면 알림에 저장
* 댓글 수정
  * FreeBoardAnswer id로 FreeBoardAnswer 정보를 가져와서 FreeBoardAnswerApiResponse를 생성 후 리턴
  * 리턴 받은 정보로 view페이지에 form 생성
  * form에서 FreeBoardAnswer의 id와 수정된 contents만 받아서 update 후 변경된 FreeBoardAnswer를 db에 저장
  * 저장된 FreeBoardAnswer를 FreeBoardAnswerApiResponse로 생성 후 리턴
  * 리턴받은 정보를 view페이지에 html로 그림
* 댓글 삭제
  * 넘어온 댓글의 id로 댓글을 db에서 가져온다.
  * 댓글의 작성자와 로그인 유저(삭제를 요청한 유저)가 같은지 확인한다.
  * 작성자와 로그인유저가 같다면 타임라인에서 댓글을 삭제
  * 댓글의 작성자와 로그인 유저가 같으면 알림의 댓글을 삭제
  * 댓글을 삭제
  * 댓글을 삭제한 자유게시판의 게시물을 가져와서 게시물의 댓글에서 지우는 댓글을 삭제 후 자유게시판의 댓글의 개수를 SuccessReponse에 담아 생성한 SuccessReponse를 리턴 (댓글이 지워지면 자유게시판의 게시물과 매핑을 했기 때문에 자유게시판의 게시물의 댓글도 지워지는데 처리가 안되는지 로직이 완료되지 않아서 인지 바뀐 댓글의 수가 리턴되지 않아 게시물의 댓글을 삭제하는 메서드를 넣음. 넣지 않을경우 ajax로는 이전 개수가 나오고 페이지를 새로고침하면 제대로 반영된다. 이부분에 대해서 공부할 필요가 있음.)
* 로그인유저(댓글 작성자)가 쓴 모든 자유게시판 게시물의 댓글을 가져온다.

**controller**

* 생성
  * 로그인을 하지 않았다면 Exception 발생
  * ExceptionHandler에서 @ResponseStatus사용
  * ErrorResponse에 http상태 코드와 error 메시지를 담아 리턴
  * ajax error에서 알림으로 메시지 출력
  * 댓글의 내용이 없을 경우 Exception이 발생
  * ErrorResponse에 http상태 코드와 error 메시지를 담아 리턴
  * ajax error에서 알림으로 메시지 출력
  * 댓글을 작성하는 자유게시판 게시물의 id와 댓글의 내용을 받아 댓글 생성
* 읽기
  * 파라미터로 받은 id로 FreeBoardAnswer 정보를 가져와서 FreeBoardAnswerApiResponse로 생성 후 FreeBoardAnswerApiResponse를 리턴
* 수정
  * 댓글의 내용이 없을 경우 Exception이 발생
  * ErrorResponse에 http상태 코드와 error 메시지를 담아 리턴
  * ajax error에서 알림으로 메시지 출력
  * 내용만 변경하기 때문에 @PatchMapping 어노테이션 사용
  * 파라미터로 받은 id로 FreeBoardAnswer의 정보를 가져와서 파라미터로 받은 contents를 변경 후 db에 저장
  * 저장한 FreeBoardAnswer를 FreeBoardAnswerApiResponse로 생성 후 FreeBoardAnswerApiResponse를 리턴
* 삭제
  * 로그인을 하지 않았다면 Exception 발생
  * ExceptionHandler에서 @ResponseStatus사용
  * ErrorResponse에 http상태 코드와 error 메시지를 담아 리턴
  * ajax error에서 알림으로 메시지 출력
  * 삭제하는 댓글의 id를 받아서 삭제 후 댓글의 개수 리턴

***

### Diary 설명

**Entity**

* Diary는 Entity이므로 @Entity 어노테이션을 사용하였습니다.
* 모든 필드에 getter와 setter, 서로 다른 Diary인지 비교하기위해  EqualsAndHashCode, log로 Diary를 보기위해 toString을 사용하기 위해 Lombok의 @Data어노테이션을 사용하였습니다.
* 기본 생성자 @NoArgsConstructor 어노테이션, 모든 필드의 생성자 @AllArgsConstructor를 사용하였습니다.
* Diary를 생성할 때 편리하게 사용하기위해 @Builder어노테이션을 사용하였습니다.
* 필드
  * id
    * primary key @Id어노테이션 사용
      * 읽기, 수정, 삭제를 위한 고유키
    * 자동증가를 위해 @GenaratedValue 어노테이션 사용
      * pk에 대한 전략으로 데이터베이스에 위임 IDENTITY
  * writer
    * 한명의 User가 다수의 자유게시판 댓글을 작성 할 수 있어서 @ManyToOne으로 User와 매핑한다.
    * User에서는 Diary를 사용하지 않기 때문에 매핑을하지 않았다.
    * Diary에서 writer는 User의 외래키를 갖기 때문에 @JoinColumn을 foreignKey를 설정한다.
  * titile
    * 게시물의 제목
  * contents
    * 게시물의 내용
    * 게시물의 내용이 많을 수도 있기 때문에 @Lob어노테이션 사용
  * thumbnail
    * 게시물에 사진을 등록하면 제일 처음등록 사진명을 저장하여 목록에서 사용
  * List<DiaryAnswer> diaryAnswers
    * 해당 게시물의 댓글
    * 일기게시판 게시글 하나에 여러개의 댓글이 달리기 때문에 관계는 @OneToMany로 설정
    * 순환참조를 방어하기위해 @JsonBackReference어노테이션 사용 (공부할 것)
    * 일기게시판 게시글을 지우면 해당 게시글의 댓글도 지우기 위해 cascade REMOVE 설정
  * List<Good> goodPushedUser
    * 해당 게시물의 좋아요. 좋아요를 누른 일기게시판 게시물과 User가 있다.
    * 순환참조를 방어하기위해 @JsonBackReference어노테이션 사용
  * createDate
    * 등록한 날짜를 기록하는 필드  
  * update
    * 수정한 날짜를 기록하는 필드
* 날짜 형식을 변경하는 메서드
* 게시물의 댓글의 개수를 나타내는 메서드
* 게시물의 좋아요의 개수를 나타내는 메서드
* 게시물의 작성자와 넘어온 User가 같은지 확인하는 메서드
* 게시물을 수정하는 메서드
* 게시물의 id와 넘어온 id가 같은지 확인하는 메서드

**service**

* 게시물 가져오기
  * 로그인 유저, 페이지 숫자, email주소 유저(url에 email주소 입력 접근)를 받아서 PageRequest.of에 넘겨받은 페이지 숫자를 넣어 목록을 가져온다. 로그인 유저가 없거나 로그인유저와 email주소 유저가 다르면 email주소 유저가 작성한 게시물이 나온다.
  * 로그인 유저와 email주소 유저와 같으면 로그인 유저가 작성한 게시물이 나온다.
* 생성
  * 로그인유저와 입력받은 일기게시판 게시물 정보를 builder()를 사용해서 db에 저장 후 저장된 일기게시판 게시물을 timeline에 @OneToOne관계로 저장
* 하나의 게시물 가져오기
  * id를 통해 게시물 하나를 가져온다.
* 수정
  * 새로 수정된 게시물로 update
* 삭제
  * timeline과 관계매핑을 하지 않아서 삭제할 게시물의 타임라인을 삭제하고 해당 일기게시판 게시물의 좋아요와 댓글들의 타임라인과 알림을 삭제하고 일기게시판의 게시물을 삭제한다.
* User의 정보로 User가 등록한 게시물을 가져온다.
* 게시물의 좋아요의 개수를 올리고 내리는 메서드
  * 일기게시판의 게시물에 Good을 가지고 있지 않으면 추가하고 Good을 가지고 있으면 삭제한다. 
  * 로직은 GoodService에서 수행한다.
  * Good의 개수를 Json으로 리턴한다. (Json의 객체는 개수 하나만 가지고 있다.)

**controller**

* 일기게시판 게시물 가져오기
  * email과 pageNum을 받아서 email의 유저를 찾아서 email의 유저가 작성한 일기게시판 게시물을 pageNum에서 6개를 Page로 가져온다. pageNum을 받지 않으면 pageNum은 1이다.
  * PageNationsUtil을 사용해서 게시물의 페이지 숫자를 model에 담아 diary페이지에서 사용
    * 페이지의 숫자는 10개씩 나오게 하였고 1~10을 입력 받으면 1~10을, 11~20을 입력 받으면 11~20을 나오게 처리
    * 총 페이지가 나타나는 페이지의 숫자(10, 20, 30...)보다 작으면 총 페이지 숫자만큼 나오게 하였다.
    * 이전페이지와 다음페이지는 10개씩 이동되게 하였고 이전페이지가 없을 경우 (1페이지) 이전페이지는 나타나지 않는다. 다음페이지도 마찬가지로 현재 보고있는 페이지의 끝페이지(10, 20, 30...)의 다음페이지가 없으면 나타나지 않는다.
  * 요청받은 email유저를 model에 담아 페이지를 이동할 때 email유저의 email을 계속 받아서 처리하게 하였다.
* 글쓰기 form
  * 로그인을 하지 않았으면 로그인페이지로 이동
  * 로그인 한 유저가 있으면 로그인한 유저를 model에 담아 취소버튼을 눌렀을 때 유저의 email을 사용할 때 사용
* 생성
  * 로그인을 하지 않았으면 로그인페이지로 이동
  * 게시물 service로 생성
  * 일기게시판 목록페이지로 redirect
* 일기게시판 상세페이지
  * 로그인 한 유저와 해당 게시물의 작성자가 같은지 그리고 관리자 권한인지 확인하여 model에 boolean값을 넣어 해당 페이지에서 버튼을 다르게 나오게 한다.
  * 게시물의 정보를 model에 담아 페이지에 나오게 한다.
* 수정페이지
  * 로그인을 하지 않았으면 로그인페이지로 이동
  * 로그인 한 유저와 수정을 요청한 게시물의 작성자가 같은지 확인해서 같지 않으면 errorMessage를 담아 로그인페이지로 이동 후 출력
  * 게시물의 정보를 model에 담아 contents는 다음에디터로 보여주어 수정 가능하게 한다. (다음에디터도 따로 정리)
* 수정
  * 로그인을 하지 않았으면 로그인페이지로 이동
  * 다시 한번 로그인 한 유저와 수정을 요청한 게시물의 작성자가 같은지 확인해서 같지 않으면 errorMessage를 담아 로그인페이지로 이동 후 출력
  * service update실행 후 해당 게시물의 상세페이지로 redirect
* 삭제
  * 로그인을 하지 않았으면 로그인페이지로 이동
  * 로그인 한 유저와 삭제를 요청한 게시물의 작성자가 같지않고 관리자 권한을 갖지 않았다면  errorMessage를 담아 로그인페이지로 이동 후 출력
  * 모두 통과되면 게시물 삭제 후 목록페이지로 redirect
* 좋아요 개수 수정
  * 로그인을 하지 않았으면 Exception으로 던져서 ExceptionHeandler가 처리하여 Exception의 message를 보낸다. 
  * 좋아요를 눌렀을 때 개수를 Json으로 받기 위해 @ResponseBody를 사용한다.
  * 원래는 RestController로 구현했으나 로직이 하나뿐이어서 DiaryController로 이동하였다.(DiaryRestController 삭제하지 않음)

***

### DiaryAnswer 설명

**Entity**

* DiaryAnswer는 Entity이므로 @Entity 어노테이션을 사용하였습니다.
* 모든 필드에 getter와 setter, 서로 다른 DiaryAnswer인지 비교하기위해  EqualsAndHashCode, log로 DiaryAnswer를 보기위해 toString을 사용하기 위해 Lombok의 @Data어노테이션을 사용하였습니다.
* 기본 생성자 @NoArgsConstructor 어노테이션, 모든 필드의 생성자 @AllArgsConstructor를 사용하였습니다.
* 생성된 날짜와 수정된 날짜를 자동으로 관리하기 위해 @EntityListeners(AuditingEntityListener.class) 어노테이션을 사용하였습니다.
* DiaryAnswer를 생성할 때 편리하게 사용하기위해 @Builder어노테이션을 사용하였습니다.
* 필드
  * id
    * primary key @Id어노테이션 사용
      * 읽기, 수정, 삭제를 위한 고유키
    * 자동증가를 위해 @GenaratedValue 어노테이션 사용
      * pk에 대한 전략으로 데이터베이스에 위임 IDENTITY
  * writer
    * 한명의 User가 다수의 일기게시판 게시물에 댓글을 작성 할 수 있어서 @ManyToOne으로 User와 매핑
    * User에서는 DiaryAnswer를 사용하지 않기 때문에 매핑을하지 않았다.
    * DiaryAnswer에서 writer는 User의 외래키를 갖기 때문에 @JoinColumn을 foreignKey를 설정한다.
  * diary
    * DiaryAnswer는 Diary의 댓글이기 때문에 어떤 글의 댓글인지 @ManyToOne으로 매핑
    * 외래키이기 때문에 @JoinColumn으로 ForeignKey를 설정하고 반드시 값이 있어야하기 때문에 nullable을 false로 설정한다.
    * 순환참조 무한루프에 빠지지 않기위해 @JsonIgnoreProperties 설정
  * contents
    * 댓글의 내용
    * 댓글의 내용이 많을 수도 있기 때문에 @Lob어노테이션 사용
  * createDate
    * 등록한 날짜를 기록하는 필드
  * updateDate
    * 수정한 날짜를 기록하는 필드
* 날짜 형식을 변경하는 메서드
* 작성자와 넘어오는 User가 같은지 확인하는 메서드

**DiaryAnswerApiResponse**

* DiaryAnswer를 생성하고 응답으로 건내줄 DTO
* 필드
  * id
    * 수정 및 삭제를 위해 생성된 DiaryAnswer의 id값
  * writer
    * DiaryAnswer의 글쓴이 정보
  * contents
    * DiaryAnswer의 내용
  * diaryId
    * 수정, 삭제에 사용할 Diary id
    * 순환참조 방어를 위해 id만 저장
  * countOfAnswers
    * DiaryAnswer를 생성한 FreeBoard의 댓글 개수
    * 순환참조를 방어하기위해 String으로 저장
  * createDate
    * DiaryAnswer가 생성된 날짜
    * LocalDateTimeUtils로 변환한 값을 저장

**service**

* 댓글 생성
  * 로그인 유저(작성자), 댓글을 작성하는 일기게시판 게시글의 id, 댓글의 내용을 받아서 DiaryAnswer의 builer()를 사용하여 DiaryAnswer를 생성
  * 댓글의 내용은 html로 보여지기 때문에 String의 줄바꿈을 html 줄바꿈 태그로 변경
  * db에 저장 후 timeline생성 및 저장
  * 댓글을 단 일기게시판 작성자와 댓글의 작성자가 다르면 알림에 저장
* 댓글 수정
  * DiaryAnswer id로 DiaryAnswer정보를 가져와서 DiaryAnswerApiResponse를 생성 후 리턴
  * 리턴 받은 정보로 view페이지에 form 생성
  * form에서 DiaryAnswer의 id와 수정된 contents만 받아서 update 후 변경된 DiaryAnswer를 db에 저장
  * 저장된 DiaryAnswer를 DiaryAnswerApiResponse로 생성 후 리턴
  * 리턴받은 정보를 view페이지에 html로 그림
* 댓글 삭제
  * 넘어온 댓글의 id로 댓글을 db에서 가져온다.
  * 댓글의 작성자와 로그인 유저(삭제를 요청한 유저)가 같은지 확인하여 다르면 예외로 던진다.
  * 작성자와 로그인유저가 같다면 삭제할 댓글의 타임라인을 삭제
  * 댓글의 작성자와 로그인 유저가 같으면 알림의 댓글을 삭제
  * 댓글을 삭제
  * 댓글을 삭제한 일기게시판의 게시물을 가져와서 지우는 댓글을 삭제 후 일기게시판의 댓글의 개수를 리턴 (댓글이 지워지면 일기게시판의 게시물과 매핑을 했기 때문에 일기게시판의 게시물의 댓글도 지워지는데 처리가 안되는지 로직이 완료되지 않아서 인지 바뀐 댓글의 수가 리턴되지 않아 게시물의 댓글을 삭제하는 메서드를 넣음. 넣지 않을경우 ajax로는 이전 개수가 나오고 페이지를 새로고침하면 제대로 반영된다. 이부분에 대해서 공부할 필요가 있음.)
* 로그인유저(댓글 작성자)가 쓴 모든 일기게시판 게시물의 댓글을 가져온다.

**controller**

* 생성
  * 댓글의 내용이 없을 경우 Exception이 발생
  * ErrorResponse에 http상태 코드와 error 메시지를 담아 리턴
  * ajax error에서 알림으로 메시지 출력
  * 로그인을 하지 않았다면 Exception 발생
  * ExceptionHandler에서 @ResponseStatus사용
  * ErrorResponse에 http상태 코드와 error 메시지를 담아 리턴
  * ajax error에서 알림으로 메시지 출력 
  * 댓글을 작성하는 일기게시판 게시물의 id와 댓글의 내용을 받아 댓글 생성
* 읽기
  * 파라미터로 받은 id로 DiaryAnswer 정보를 가져와서 DiaryAnswerApiResponse로 생성 후 DiaryAnswerApiResponse를 리턴
* 수정
  * 댓글의 내용이 없을 경우 Exception이 발생
  * ErrorResponse에 http상태 코드와 error 메시지를 담아 리턴
  * ajax error에서 알림으로 메시지 출력
  * 내용만 변경하기 때문에 @PatchMapping 어노테이션 사용
  * 파라미터로 받은 id로 DiaryAnswer의 정보를 가져와서 파라미터로 받은 contents를 변경 후 db에 저장
  * 저장한 DiaryAnswer를 DiaryAnswerApiResponse로 생성 후 DiaryAnswerApiResponse를 리턴
* 삭제
  * 로그인을 하지 않았다면 Exception 발생
  * ExceptionHandler에서 @ResponseStatus사용
  * ErrorResponse에 http상태 코드와 error 메시지를 담아 리턴
  * ajax error에서 알림으로 메시지 출력
  * 삭제하는 댓글의 id를 받아서 삭제 후 댓글의 개수 리턴

***

### Good 설명 

**Entity**

* 일기게시판 게시물에 좋아요를 담당하는 Entity이다.
* 일기게시판의 좋아요를 눌렀을 때 누른 게시물과 유저를 담는다.
* 일기게시판 게시물 하나에 다수의 Good이 저장될 수 있고 Good에는 좋아요를 누른 하나의 일기게시판 게시물과 유저 정보를 저장한다.
* 모든 필드에 getter와 setter, log로 Timeline를 보기위해 toString을 사용하기 위해 Lombok의 @Data어노테이션을 사용하였습니다.
* 기본 생성자 @NoArgsConstructor 어노테이션, 모든 필드의 생성자 @AllArgsConstructor를 사용하였습니다.
* Good을 생성할 때 편리하게 사용하기위해 @Builder어노테이션을 사용하였습니다.
* 필드
  * id
    * primary key @Id어노테이션 사용
      * Tilmeline과의 매핑을 위해 사용
    * 자동증가를 위해 @GenaratedValue 어노테이션 사용
      * pk에 대한 전략으로 데이터베이스에 위임 IDENTITY
  * diary
    * 하나의 Diary가 다수의 Good의 정보를 가질 수 있기 때문에 @ManyToOne으로 Diary와 매핑한다.
    * Good에서 Diary는 Diary의 외래키를 갖기 때문에 @JoinColumn을 foreignKey를 설정한다.
  * user
    * 한명의 User가 다수의 Good의 정보를 가질 수 있기 때문에 @ManyToOne으로 User와 매핑한다.
    * User에서는 Good을 사용하지 않기 때문에 매핑을하지 않았다.
    * Good에서 User는 User의 외래키를 갖기 때문에 @JoinColumn을 foreignKey를 설정한다.
  * createDate
    * 등록한 날짜를 기록하는 필드(변경 필요)
* 날짜 형식을 변경하는 메서드


**service**

* 일기게시판의 게시물에 좋아요를 눌렀을 때 해당 일기게시판 게시물의 정보와 좋아요를 누른 유저(로그인 유저)를 받아서 해당 게시물에 유저가 좋아요를 눌렀는지 찾아보고 없으면 Good을 생성하고 타임라인을 생성한다. 만약 좋아요를 누른 일기게시판의 게시물이 자신의 글이 아니면 알림도 생성한다.
* 만약 일기게시판의 게시물에 좋아요를 눌렀다면 찾은 Good을 삭제하고 타임라인을 삭제한다. 만약 좋아요를 누른 일기게시판의 게시물이 자신의 글이 아니면 알림도 삭제한다.
* 변경된 Diary를 리턴한다.
* GoodService에서는 이에대한 로직 하나만 수행한다.
* 좋아요의 개수를 증가시키고 감소시키는 것은 Diary가 하는 기능이지만 Good도 하나의 Entity이기 때문에 비록 하나뿐지만 Good service를 구현했다. 
* 유저가 누른 좋아요의 목록을 가져온다.

**controller**

* DiaryRestController로 구현했으나 DiaryController로 해당 로직을 옮겼다. (DiaryController 설명에 추가함)

***

### Timeline 설명

* Timeline은 유저가 자유게시판, 자유게시판 댓글, 일기게시판, 일기게시판 댓글, 일기게시판 좋아요를 했을 때 @OneToOne으로  매핑된다.
* 부모 Entity에 해당하는 자유게시판, 자유게시판 댓글, 일기게시판, 일기게시판 댓글, 일기게시판 좋아요와의 매핑을 하였지만 부모 Entity에서 자식 Entity를 사용하지 않아서 삭제하였다.
* 타임라인의 삭제를 부모 Entity 로직에서 구현하지 않으려면 부모Entity에 매핑해야한다.
* Timeline는 Entity이므로 @Entity 어노테이션을 사용하였습니다.
* 모든 필드에 getter와 setter, log로 Timeline를 보기위해 toString을 사용하기 위해 Lombok의 @Data어노테이션을 사용하였습니다.
* 기본 생성자 @NoArgsConstructor 어노테이션, 모든 필드의 생성자 @AllArgsConstructor를 사용하였습니다.
* Timeline를 생성할 때 편리하게 사용하기위해 @Builder어노테이션을 사용하였습니다.
* 필드
  * id
    * primary key @Id어노테이션 사용
      * 읽기를 위한 고유키
    * 자동증가를 위해 @GenaratedValue 어노테이션 사용
      * pk에 대한 전략으로 데이터베이스에 위임 IDENTITY
    * id를 사용하여 타임라인을 가져와서 저장된 게시물 정보로 redirect 하는게 id를 사용하지 않고 redirect하는 것보다 더 간단하게 구현할 수 있어서 id를 사용했지만 더 생각해 볼 필요가 있다.
  * type
    * 저장되는 게시물의 종류를 나타낸다.
  * icon
    * veiw에서 보여줄 아이콘을 저장
  * freeBoard
    * 작성한 자유게시판의 게시글을 저장
    * FreeBoard 하나당 Timeline도 하나이기 때문에 @OneToOne으로 매핑
    * FreeBoard에서는 Timeline을 사용하지 않기 때문에 매핑을 하지 않았다.(매핑을 하는 것이 사제할 때 편리)
    * 외래키를 갖기 때문에 @JoinColumn을 foreignKey를 설정
  * freeBoardAnswer
    * 작성한 자유게시판의 게시물의 댓글을 저장
    * FreeBoardAnswer 하나당 Timeline도 하나이기 때문에 @OneToOne으로 매핑
    * FreeBoardAnwer에서는 Timeline을 사용하지 않기 때문에 매핑을 하지 않았다.
    * 외래키를 갖기 때문에 @JoinColumn을 foreignKey를 설정
  * diary
    * 작성한 일기게시판의 게시물을 저장
    * Diary 하나당 Timeline도 하나이기 때문에 @OneToOne으로 매핑
    * Diary에서는 Timeline을 사용하지 않기 때문에 매핑을 하지 않았다.
    * 외래키를 갖기 때문에 @JoinColumn을 foreignKey를 설정
  * diaryAnswer
    * 작성한 일기게시판의 게시물의 댓글을 저장
    * DiaryAnswer 하나당 Timeline도 하나이기 때문에 @OneToOne으로 매핑
    * DiaryAnswer에서는 Timeline을 사용하지 않기 때문에 매핑을 하지 않았다.
    * 외래키를 갖기 때문에 @JoinColumn을 foreignKey를 설정
  * good
    * 일기게시판의 좋아요를 눌렀을 때 Good을 저장
    * Good 하나당 Timeline도 하나이기 때문에 @OneToOne으로 매핑
    * Good에서는 Timeline을 사용하지 않기 때문에 매핑을 하지 않았다.
    * 외래키를 갖기 때문에 @JoinColumn을 foreignKey를 설정
  * user
    * 자유게시판 게시물, 자유게시판 게시물의 댓글, 일기게시판 게시물, 일기게시판 게시물의 댓글의 작성자와 일기게시판 게시물의 좋아요를 누른 유저를 저장
    * User 한명이 다수의 Timeline을 만들기 때문에 @ManyToOne으로 매핑
    * User에서는 Timeline을 사용하지 않기 때문에 매핑을 하지 않았다.
    * 외래키를 갖기 때문에 @JoinColumn을 foreignKey를 설정
  * saveDate
    * 자유게시판 게시물, 자유게시판 게시물의 댓글, 일기게시판 게시물, 일기게시판 게시물의 댓글, 일기게시판 게시물의 좋아요가 만들어진 날짜를 저장
* 날짜 형식을 변경하는 메서드
* 저장된 type을 확인하여 등록된 게시물에 접근하는 메서드


**service **

* 페이지 숫자와 유저 정보를 받아 유저가 등록한 타임라인을 요청받은 페이지의 타임라인 5개를 List로 반환한다.
* 타임라인 id로 해당 id의 타임라인 정보를 가져온다.
* create
  * 메서드 오버로딩으로 일기게시판, 일기게시판 댓글, 일기게시판 좋아요, 자유게시판, 자유게시판 댓글 각각의 자료가 들어오면 각각의 자료에 맞게 타임라인을 생성한다.
* delete
  * 삭제도 생성과 마찬가지로 메서드 오버로딩으로 일기게시판, 일기게시판 댓글, 일기게시판 좋아요, 자유게시판, 자유게시판 댓글 각각의 자료가 들어오면 각각의 자료에 맞게 타임라인을 삭제한다.

***

### TempTimeline 설명

**TempTimeline**

* 이 클래스는 db에 저장된 데이터를 TempTimeline의 필드형에 맞게 가져와 List<TempTimeline>에 담아 위의 Timelime역할을 하는 클래스이다.
* 현재는 Timeline Entity를 사용하고 있어서 TempTimeline으로 클래스명으로 정하고 사용하지 않는다.
* 저장된 데이터를 또 db에 저장하는 것이 아까워서 일종의 DTO로 구현했다. 그렇기때문에 Entity가 아니다.
* 필드는 Timeline과 크게 다르지 않고 id가 없기 때문에 redirect할 url을 저장하여 사용한다.


**service**

* 로그인 유저의 정보를 받아 로그인 저장된 유저가 작성한 자유게시판, 자유게시판 댓글, 일기게시판, 일기게시판 댓글과 좋아요를 누른 일기게시판을 최신 5개씩 가져와 List에 저장하고 Comparator를 날짜 내림차순(최신순)으로 구현하여 List를 정렬한다.
* 로그인 유저의 프로필 페이지에서 타임라인으로 사용하려면 추가 작업이 필요하지만 임시로 구현했기 때문에 구현하지 않았다.

***

### Alert 설명

* Alert는 자유게시판의 게시물에 작성자 이외의 유저가 댓글을 달았을 때와 일기게시판의 게시물에 작성자 이외의 유저가 댓글과 좋아요를 눌렀을 때 작성자에게 알려주는 기능이다.
* Alert는 Timeline과 비슷한 구조를 가진다.

**Entity**

* 모든 필드에 getter와 setter, log로 Alert를 보기위해 toString을 사용하기 위해 Lombok의 @Data어노테이션을 사용하였습니다.
* 기본 생성자 @NoArgsConstructor 어노테이션, 모든 필드의 생성자 @AllArgsConstructor를 사용하였습니다.
* Alert를 생성할 때 편리하게 사용하기위해 @Builder어노테이션을 사용하였습니다.
* 필드
  * id    
    * primary key @Id어노테이션 사용
      * Tilmeline과의 매핑을 위해 사용
    * 자동증가를 위해 @GenaratedValue 어노테이션 사용
      * pk에 대한 전략으로 데이터베이스에 위임 IDENTITY
    * id로 해당 게시물에 접근
  * type
    * 알림에 나타날 게시물의 종류를 나타내기 위한 필드
  * good
    * 일기게시판의 좋아요를 눌렀을 때 Good을 저장
    * Good 하나당 Alert도 하나이기 때문에 @OneToOne으로 매핑
    * Good에서는 Alert를 사용하지 않기 때문에 매핑을 하지 않았다.
    * 외래키를 갖기 때문에 @JoinColumn을 foreignKey를 설정
  * diaryAnswer
    * 작성한 일기게시판의 게시물의 댓글을 저장
    * DiaryAnswer 하나당 Alert도 하나이기 때문에 @OneToOne으로 매핑
    * DiaryAnswer에서는 Alert를 사용하지 않기 때문에 매핑을 하지 않았다.
    * 외래키를 갖기 때문에 @JoinColumn을 foreignKey를 설정
  * freeBoardAnswer
    * 작성한 자유게시판의 게시물의 댓글을 저장
    * FreeBoardAnswer 하나당 Alert도 하나이기 때문에 @OneToOne으로 매핑
    * FreeBoardAnwer에서는 Alert를 사용하지 않기 때문에 매핑을 하지 않았다.
    * 외래키를 갖기 때문에 @JoinColumn을 foreignKey를 설정
  * postWriter
    * 자유게시판 게시물의 댓글, 일기게시판 게시물의 댓글의 작성자와 일기게시판 게시물의 좋아요를 누른 유저를 저장
    * User 한명이 다수의 Alert를 만들기 때문에 @ManyToOne으로 매핑
    * User에서는 Alert를 사용하지 않기 때문에 매핑을 하지 않았다.
    * 외래키를 갖기 때문에 @JoinColumn을 foreignKey를 설정
  * createDate
    * 자유게시판 게시물의 댓글, 일기게시판 게시물의 댓글, 일기게시판 게시물의 좋아요가 만들어진 날짜를 저장
* 날짜 형식을 변경하는 메서드

**service**

* 하나의 alert가져오기
  * alert의 id로 id의 Alert 정보 하나 가져온다.
  * alert와 연결된 게시물에 redirect할 때 사용
* Alert를 List로 가져오기
  * PageRequest of()메서드로 Alert의 id 역순(최신순)으로 정렬하여 상위 5개를 로그인유저의 정보로 가져와서 view페이지의 상위메뉴의 알림에 사용
* create
  * 메서드 오버로딩으로 일기게시판 댓글, 일기게시판 좋아요, 자유게시판 댓글 각각의 자료가 들어오면 각각의 자료에 맞게 Alert를 생성하며 postWriter를 게시물의 작성자를 저장하여 알림 목록을 불러올 때 로그인 유저 정보로 찾아서 가져온다.
* delete
  * 삭제도 생성과 마찬가지로 메서드 오버로딩으로일기게시판 댓글, 일기게시판 좋아요, 자유게시판 댓글 각각의 자료가 들어오면 각각의 자료에 맞게 알림을 삭제한다.

**controller**

* UserController에서 로그인을 요청하고 응답받을 때 로그인을 요청한 유저의 정보로 Alert 목록을 가져와서 view페이지의 상위 메뉴 알림에서 사용
* DashboardController에서 로그인 한 유저의 정보로 Alert목록을 가져와서 Alert의 개수를 view페이지에서 사용

**Alert도 TempTimeline처럼 db데이터를 변환하여 사용할 수 있지만 구현을 하지 않았다.**

* README를 작성하며 Timeline과 Alert처럼 Entity로 관리하는 것 보다 TempTimeline으로 db에 저장하지 않고 변환하는 것이 좋아보인다.

***

### EmailAuth 설명

1. EmailAuth는 회원가입을 요청한 email과 랜덤값, 그리고 인증되었는지 확인할 boolean값(false)을 저장한다.
2. email 주소로 저장한 email과 랜덤값을 파라미터로 한 url 링크를 보낸다.  
3. email에서 링크를 클릭하면 EmailAuth에 저장한 email로 정보를 불러와 랜덤값을 확인하여 인증을 확인하는 값을 인증이 되었다는 값(true)으로 변경한다.

* 모든 필드에 getter와 setter, log로 EmailAuth를 보기위해 toString을 사용하기 위해 Lombok의 @Data어노테이션을 사용하였습니다.
* 기본 생성자 @NoArgsConstructor 어노테이션, 모든 필드의 생성자 @AllArgsConstructor를 사용하였습니다.
* EmailAuth를 생성할 때 편리하게 사용하기위해 @Builder어노테이션을 사용하였습니다.
* 필드
  * id    
    * primary key @Id어노테이션 사용
    * 자동증가를 위해 @GenaratedValue 어노테이션 사용
      * pk에 대한 전략으로 데이터베이스에 위임 IDENTITY
  * email
    * 회원가입때의 email을 저장
  * authKey
    * 인증을 위한 랜덤 값
  * authenticate
    * 인증이 되었는지 확인하는 필드
* 인증키를 확인하는 메서드

**service**

* 메일보내기
  * UUID의 랜덤값을 만들어 email과 함께 EmailAuth를 생성 후 db에 저장
  * JavaMailSender를 이용하여 email주소에 email과 랜덤 값을 파라미터로 한 파라미터를 받고 확인하는 url을 링크로 보낸다. 
* email로 저장된 EmailAuth를 가져온다.

**controller**

* EmailAuth를 생성하고 인증메일을 보내는 작업은 UserController에서 회원가입 때 실행된다.
* EmailController에서는 메일로 보낸 인증 url을 다룬다.
  * email과 authKey를 받아서 EmailAuth에서 email로 정보를 검색해서 없으면 model에 errorMessage를 담아 회원가입 페이지로 이동
  * 인증키가 맞지 않으면 model에 errorMessage를 담아 회원가입 페이지로 이동
  * 인증키까지 맞다면 emailAuth의 인증 필드를 인증 되었다고 변경 후 저장. 
  * model에 인증되었다는 메세지를 담아 로그인페이지로 이동

***

### Message 설명

* 등록된 회원 email로 메시지를 보낼 수 있다.
* view 페이지의 상단메뉴에서는 확인하지 않은 상위 5개를 볼 수 있고 프로필페이지에서는 보낸 메시지와 받은 메시지를 볼 수 있다.
* 메시지의 삭제 기능은 구현하지 않았다.
* 모든 필드에 getter와 setter, log로 Message를 보기위해 toString을 사용하기 위해 Lombok의 @Data어노테이션을 사용하였습니다.
* 기본 생성자 @NoArgsConstructor 어노테이션, 모든 필드의 생성자 @AllArgsConstructor를 사용하였습니다.
* Message를 생성할 때 편리하게 사용하기위해 @Builder어노테이션을 사용하였습니다.
* 필드
  * id    
    * primary key @Id어노테이션 사용      
    * 자동증가를 위해 @GenaratedValue 어노테이션 사용
      * pk에 대한 전략으로 데이터베이스에 위임 IDENTITY
    * id로 해당 게시물에 접근
  * sender
    * 메시지를 보낸 유저의 정보를 저장
    * 다수의 Message를 유저가 보낼 수 있기 때문에 @ManyToOne 어노테이션 매핑
  * receiver
    * 메시지를 받는 유저의 정보를 저장
    * 다수의 Message를 유저가 받을 수 있기 때문에 @ManyToOne어노테이션 매핑
  * contents
    * 메시지의 내용
    * @Lob 어노테이션 사용
  * confirm
    * 메시지를 읽었는지 읽지않았는지 확인하는 필드
  * className
    * view페이지에서 '읽음',  '읽지않음'으로 변경할 필드
  * sendDate
    * 메시지를 보낸시간 또는 받은 시간 나타낸다.
* 날짜 형식을 변경하는 메서드

**service**

* 메시지 생성
  * 메시지를 보낼 때 메시지를 생성
* 메시지 확인
  * 받은 메시지 view페이지에서 메시지를 클릭하면 메시지 id와 로그인한 유저 정보를 가져와서 메시지의 받은 유저와 로그인 유저를 확인해서 같지 않으면 예외 발생
  * 받은 유저와 로그인 유저가 같으면 메시지의 confirm필드와 className필드를 변경하여 저장 후 정보를 리턴
* 확인하지 않은 받은 메시지
  * User profile view페이지에서 모든 받은 메시지에서 확인하지 않은 메시지의 개수를 나타낸다.
* 확인하지 않은 받은 메시지 중 상위 5개
  * PageRequest로 id의 내림차순을 정렬 후 읽지 않은 메시지 상위 5개를 가져온다.
* 받은 메시지
  * 로그인 유저(receiver) 기준으로 메시지를 페이지 숫자에 들어있는 10개의 메시지를 찾는다.
* 보낸 메시지
  * 로그인 유저(sender) 기준으로 메시지를 페이지 숫자에 들어있는 10개의 메시지를 찾는다. 
* 하나의 메시지
  * id로 id에 해당하는 메시지를 찾는다.

**controller**

* 생성
  * 로그인을 하지 않았으면 예외 발생
  * 메시지를 보낼 email과 메시지 내용을 받는다.
  * 메시지를 보낼 email로 User정보를 찾아서 없으면 예외 발생
  * 보낼 User의 정보가 로그인한 유저 즉 자기 자신이라면 예외발생
  * 예외 상황이 없다면 메시지 생성
* 메시지 읽음 확인
  * user profile view페이지 받은 메시지에서 '읽지않음'을 클릭하면 처리되는 로직
  * 로그인 유저가 없으면 예외 발생
  * 로그인 유저가 받은 메시지 중에 넘겨 받은 id의 해당하는 메시지를 읽음으로 변경
  * 그리고 로그인 유저가 받은 메시지중에 읽지않은 메시지를 가져온다.
  * 로그인 할 때 session에 담은 읽지않은 메시지의 개수를 지우고 다시 변경된 읽지않은 메시지의 개수를 session담는다.
  * 변경된 className과 읽지 않은 메시지의 개수를 Json객체로 리턴
* 메시지 읽음 확인 후 해당 메시지의 보낸 유저의 email로 메시지 창 띄우기
  * 반환 값 빼고 메시지 읽음 확인한 로직과 같다.
  * 반환 값에 보낸 메시지의 유저 email을 담아 Json객체로 리턴

***

### Image 설명

* Image는 자유게시판, 일기게시판, 프로필 사진 정보를 저장한다.
* view페이지에서 게시물을 볼 때와 다음에디터에서 게시물을 수정할 때 사진 정보를 저장된 파일명으로 가져와서 사용한다.
* ajax로 업로드를 하기때문에 이미지 업로드가 완료되면 게시물의 본문 또는 프로필페이지에 바로 볼 수 있다.
* 모든 필드에 getter와 setter, log로 Image를 보기위해 toString을 사용하기 위해 Lombok의 @Data어노테이션을 사용하였습니다.
* 기본 생성자 @NoArgsConstructor 어노테이션, 모든 필드의 생성자 @AllArgsConstructor를 사용하였습니다.
* 필드
  * id    
    * primary key @Id어노테이션 사용      
    * 자동증가를 위해 @GenaratedValue 어노테이션 사용
      * pk에 대한 전략으로 데이터베이스에 위임 IDENTITY
    * id로 해당 게시물에 접근
  * originalFileName
    * 업로드한 파일명을 저장
  * randFileName
    * 혹시나 다른사진임에도 업로드한 파일명이 같을 경우를 생각해서 랜덤값으로 파일을 저장
  * writer
    * 업로드한 User를 저장
    * 한명의 User가 다수의 Image를 업로드 할 수 있어서 @ManyToOne으로 User와 매핑
  * size
    * 파일 크기를 저장
  * createDate
    * Image생성 날짜를 저장

**service**

* 생성
  * Lombok @Builder()를 이용해서 파라미터로 받은 User정보, originalName, randName, size로 Image를 생성 후 db에 저장
* randName으로 Image정보 찾기
* id로 Image정보 삭제
* id로 Image정보 찾기

**controller**

* 이미지 업로드
  * MultipartFile로 받은 파일(이미지)의 크기, 파일명, 확장자를 가져온다.
  * 확장자는 UUID.randomUUID()로 만든 랜덤값에 붙여준다.
  * 로컬환경에서 업로드 할 경로(프로젝트 경로)와 실제 서버에서 업로드할 경로(war파일 실행 경로)를 저장
  * MultipartFIle의 transferTo()메서드를 사용하여 File클래스로 위에 저장한 path를 사용해서 저장할 경로에 이미지파일을 저장
  * db에 Image정보 저장
  * transferTo() 메서드가 OutputStream역할을 하는데 byte를 읽고 쓰는 코드가 필요없어 간단하게 사용할 수 있다.
* 이미지 삭제
  * 게시물 본문에서 이미지를 삭제하면 본문에서만 삭제되고 서버에는 이미지가 남아있다.
  * 게시물 글쓰기 페이지 아래에 첨부박스에서 삭제할 때 서버의 이미지와 db에 저장된 이미지 정보가 삭제된다.
  * randName을 받아서 randName의 Image정보를 찾는다.
  * Image정보가 있으면 db에서 삭제
  * randName의 이미지 파일을 경로에서 File클래스로 가져온다.
  * 파일이 존재하면 삭제 후 성공 Json객체 리턴
  * 삭제를 하지 않았을 경우 "파일이 존재하지 않습니다."를 Json객체로 리턴
* randName으로 db에서 Image정보 찾기
* User 프로필 사진 업로드
  * 이미지 업로드와 비슷하지만 size를 저장하지 않는다.
  * 그리고 randName인 이미지명을 User Entity의 profile에 저장하여 사용
  * 저장 경로도 이미지 업로드와 다른 폴더에 저장
  * 변경된 User 프로필 이미지를 db에 저장 후 User정보를 리턴
  * 프로필 사진의 경우 삭제 기능이 없다.

***

### HomeController 및 Util클래스 설명

**HomeController**

* HomeController는 index페이지를 담당하고 알림의 게시물에 redirect하는 부분을 담당한다.

**HttpSessionUtils**

* HttpSession을 받아서 session에 저장된 정보가 있는지 확인한다.
* HttpSession을 받아서 static필드로 선언한 이름으로 session 정보를 가져온다.
* UserController의 login에서 User정보를 session 담는다.

**LocalDateTimeUtils**

* Entity에 저장된 날짜를 String으로 변환하는 클래스
* LocalDateTime을 받아서 저장된 년도를 현재 시간의 년도를 뺀 값이 0보다 크면 년도가 다르기 때문에 년,월,일을 나타내게한다.
* 년도의 차가 0보다 크지 않으면 현재날짜와 넘어온 LocalDateTime 날짜의 분(minutes)끼리 차이를 구하고 구한 값을 1시간을 분으로 바꾼 60(분)으로 나누어서 나눈 값이 하루 24시간 이내이면서 1시간 내의 LocalDateTime이고 분의 차이가 0이면 "방금전"을 리턴한다.
* 분의 차이가 0보다 크면 분+"분 전"을 리턴하여 몇분전에 등록한 글인지 알 수 있게한다.
* 시간의 차이가 1이상이면 시간+"시간 전"을 리턴한다.
* 위에를 제외하면 같은 년도의 월,일을 리턴한다.

**PageNationsUtil**

* 페이징 처리를 하는 클래스
* 일기게시판과 보낸메시지, 받은메시지의 view페이지의 페이징을 처리한다.
* 페이징을 처리하는 페이지의 이름, 처리할 페이지 숫자, 총 페이지 수와 Model을 받아서 view페이지에 나타낼 페이지 숫자를 정한다.
* 나머지 연산자를 사용해 1 ~ 10의 페이지 숫자가 넘어오면 시작페이지를 1, 끝페이지를 10으로 나오게 한다. (11 ~ 20은 시작페이지 11, 끝페이지 20....)
* 만약 끝페이지가 총페이지 수보다 크면 끝페이지를 총페이지 수로 바꾼다. 
* 등록된 글이 없으면 기본적을 1(페이지)을 나오게 한다.
* 위에서 구한 처음페이지와 끝페이지와 사이의 숫자들을 ArrayList<Integer>에 저장하여 pageName과 "pageNations"를 합친 문자열로 Model에 담아 일기게시판, 보낸메시지, 받은메시지 페이지에서 다르게 사용한다.
* 그리고 이전페이지와 다음페이지는 1페이지씩 옮기는 것이 아니라 10페이지씩 옮기도록 하였다. (1 ~ 10페이지에서는 다음페이지는 11 ~ 20, 11 ~ 20페이지에서 이전페이지는 1 ~ 10페이지)
* 1 ~ 10페이지에서는 이전페이지가 없기 때문에 이전페이지는 시작페이지가 1보다 클때만 나타내게 한다.
* 다음페이지는 총 페이지 수가 끝페이지 수보다 크면 나타나게 한다.

**SecurityUtils**

* SHA-256으로 받은 password를 64자리 문자열로 암호화하는 클래스이다.
* 유저가 입력한 password를 db에 저장할 때 암호화하여 저장한다.
* 로그인을 요청하여 password를 확인할 때도 바뀐 password로 확인한다.
* 인터넷에서 찾아서 사용하였다. (SHA-256 검색 추천)

***

### 다음에디터

* 다음에디터는 검색을 통해 파일을 다운받아 view페이지에서 사용한다.
* 다음에디터의 js파일의 양은 어마어마하다 github의 언어가 javascript로 나와서 bootstrap 을 제외시켰는데도 javascript로 나올정도여서 다음에디터도 제외시켰다.
* diary form과 freeboard form에서 각각 다른 form을 사용하기 위해 다음에디터를 처리하는 js파일도 다르다.
* 이미지 업로드는 IE10 이하에서도 가능하도록 ajaxForm 사용했다.
* daumeditor/pages/trex/image.html에 ajaxForm 추가하여 이미지 업로드 코드 추가했다.
* daumeditor/js/trex/attachment.js(다음에디터의 첨부박스)에 remove function()에 ajax로 이미지 삭제 코드 추가했다.
* 게시물 수정 요청시 본문을 가져올 때 등록된 이미지가 있으면 ajax로 파일명으로 파일정보를 가져온다.

***