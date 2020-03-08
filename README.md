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
    * 등록한 날짜를 기록하는 필드(User의 createDate와 같이 변경 필요)
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


**service**

* 자유게시판의 모든 게시물을 가져온다.
* 로그인 한 유저의 정보와 입력받은 자유게시판 정보를 Lombok의 builder()를 사용해서 db에 저장 후 저장된 자유게시판 게시물을 timeline에 @OneToOne관계로 저장
* 자유게시판 id를 가지고 id에 해당하는 게시물을 가져온다.
* 자유게시판 id를 가지고 id에 해당하는 게시물을 가져와 조회수를 증가한다.
* 입력받은 자유게시판 정보로 update한다.
* timeline과 관계매핑을 하지 않아서 삭제할 게시물의 타임라인을 삭제하고 해당 자유게시판 게시물에 달린 댓글들의 타임라인과 알림을 삭제하고 자유게시판의 게시물을 삭제한다.
* User의 정보로 User가 등록한 게시물을 가져온다.


**controller**
* 자유게시판의 모든 게시물을 가져오기
  * 현재 시간을 기준으로 24시간내의 등록된 글이면 badge필드에 "New"를 넣는다. 그리고 게시물을 model에 담아 freeboard페이지에서 사용한다.
* 자유게시판 글쓰기 페이지
  * 로그인을 하지 않았으면 로그인페이지로 이동한다.
* 게시물 생성
  * 로그인 한 유저와 넘겨받은 자유게시판 정보로 db에 저장하면 자유게시판 리스트페이지로 이동한다.
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
    * 무한루프에 빠지지 않기위해 @JsonIgnoreProperties와  @JsonManagedReference 어노테이션을 설정(검색으로 찾아서 확실하게 알아봐야함)
  * contents
    * 댓글의 내용
    * 댓글의 내용이 많을 수도 있기 때문에 @Lob어노테이션 사용
  * createDate
    * 등록한 날짜를 기록하는 필드(변경 필요)
* 날짜 형식을 변경하는 메서드
* 작성자와 넘어오는 User가 같은지 확인하는 메서드
* 댓글을 수정하는 메서드


**service**
* 댓글 생성
  * 로그인 유저(작성자), 댓글을 작성하는 자유게시판 게시글의 id, 댓글의 내용을 받아서 FreeBoardAnswer의 builer()를 사용하여 FreeBoardAnswer를 생성
  * 댓글의 내용은 html로 보여지기 때문에 String의 줄바꿈을 html 줄바꿈 태그로 변경
  * db에 저장 후 timeline생성
  * 댓글을 단 자유게시판 작성자와 댓글의 작성자가 다르면 알림에 저장
* 댓글 삭제
  * 넘어온 댓글의 id로 댓글을 db에서 가져온다.
  * 댓글의 작성자와 로그인 유저(삭제를 요청한 유저)가 같은지 확인한다.
  * 작성자와 로그인유저가 같다면 타임라인에서 댓글을 삭제
  * 댓글의 작성자와 로그인 유저가 같으면 알림의 댓글을 삭제
  * 댓글을 삭제
  * 댓글을 삭제한 자유게시판의 게시물을 가져와서 게시물의 댓글에서 지우는 댓글을 삭제 후 자유게시판의 댓글의 개수를 리턴 (댓글이 지워지면 자유게시판의 게시물과 매핑을 했기 때문에 자유게시판의 게시물의 댓글도 지워지는데 처리가 안되는지 로직이 완료되지 않아서 인지 바뀐 댓글의 수가 리턴되지 않아 게시물의 댓글을 삭제하는 메서드를 넣음. 넣지 않을경우 ajax로는 이전 개수가 나오고 페이지를 새로고침하면 제대로 반영된다. 이부분에 대해서 공부할 필요가 있음.)
  * 로그인유저(댓글 작성자)가 쓴 모든 자유게시판 게시물의 댓글을 가져온다.

**controller**
* 생성
  * 로그인을 하지 않았다면 Exception 발생
  * 댓글을 작성하는 자유게시판 게시물의 id와 댓글의 내용을 받아 댓글 생성
* 삭제
  * 로그인을 하지 않았다면 Exception 발생
  * 삭제하는 댓글의 id를 받아서 삭제
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
***
### DiaryAnswer 설명
**Entity**
* DiaryAnswer는 Entity이므로 @Entity 어노테이션을 사용하였습니다.
* 모든 필드에 getter와 setter, 서로 다른 DiaryAnswer인지 비교하기위해  EqualsAndHashCode, log로 DiaryAnswer를 보기위해 toString을 사용하기 위해 Lombok의 @Data어노테이션을 사용하였습니다.
* 기본 생성자 @NoArgsConstructor 어노테이션, 모든 필드의 생성자 @AllArgsConstructor를 사용하였습니다.
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
    * 등록한 날짜를 기록하는 필드(변경 필요)
* 날짜 형식을 변경하는 메서드
* 작성자와 넘어오는 User가 같은지 확인하는 메서드

**service**
* 댓글 생성
  * 로그인 유저(작성자), 댓글을 작성하는 일기게시판 게시글의 id, 댓글의 내용을 받아서 DiaryAnswer의 builer()를 사용하여 DiaryAnswer를 생성
  * 댓글의 내용은 html로 보여지기 때문에 String의 줄바꿈을 html 줄바꿈 태그로 변경
  * db에 저장 후 timeline생성 및 저장
  * 댓글을 단 일기게시판 작성자와 댓글의 작성자가 다르면 알림에 저장
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
  * 로그인을 하지 않았다면 Exception 발생
  * 댓글을 작성하는 일기게시판 게시물의 id와 댓글의 내용을 받아 댓글 생성
* 삭제
  * 로그인을 하지 않았다면 Exception 발생
  * 삭제하는 댓글의 id를 받아서 삭제 후 댓글의 개수 리턴
***