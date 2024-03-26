# 과제 수행 방법
* 코드를 완성 하세요.
* 주어진 코드가 문제가 있거나 개선의 필요성이 있을 시에는 적절하게 수정하세요.
* 어플리케이션이 동작하도록 완성하세요.
* DTO 등의 데이터 모델 객체를 자유롭게 활용해주세요.

# 요구사항
* (기본 기능) 다음 기능을 구현하세요.
  * 조회
    * 파라메터
      * 필터: 전화번호, 이메일, 주소, 이름
      * 정렬: (전화번호 | 이메일 | 주소 | 이름), (오름차순 | 내림차순)
    * 응답: 고객 정보 목록 (josn)
  * 수정
    * 파라메터: 전화번호
    * 응답: 고객 정보 (json)
  * 삭제
    * 파라메터: 전화번호
    * 응답: 삭제된 고객 정보 (json)
* 데이터를 로딩할 때 형식(아래 항목)이 맞는지 확인해 주세요.
  * 데이터 형식
    * 전화번호는 010 으로 시작해야 합니다.
    * 전화번호 형식은 0101231234 또는 010-123-1234 또는 010-1234-1234 이어야 합니다.
    * 이메일 형식은 아이디@도메인 이어야 합니다.
  * 데이터의 키는 전화번호 입니다.
  * 이메일은 중복될 수 없습니다. 
  * 메모리에 데이터를 로딩할 때 형식을 만족하지 못하는 경우 Warning 로그를 작성한 뒤 skip 하고 이후 데이터를 읽어 주세요.
  * 사용자가 호출하는 API 인 경우 오류는 stack trace 를 출력하는 것과 별개로 사용자에게 노출될 메세지를 API 응답으로 출력해주세요. 
* 주소/이름 검색의 경우 양방향 like 검색을 구현해 주세요.
* 모든 코드를 동시성 문제와 성능을 고려하여 작성해 주세요.
* 테스트 케이스를 작성하세요.
  * 각 클래스의 단위 테스트를 작성하세요.
    * 초기 로딩 검증
    * 기본 기능에 대한 검증
    * 프로그램 종료 시 최종 파일 반영 프로세스 검증
  * Controller 단위 테스트의 경우 MockMvc 를 이용한 테스트로 작성하세요.
    * org.springframework.test.web.servlet.MockMvc

# 추가 요구사항
* 데이터의 조회 결과에 따라 발생하는 에러 및 응답 코드는 http status code 기반으로 작성해 주세요
* 메모리/검색 성능 등을 잘 조율하여 개발해 주세요.
* 어플리케이션을 중지할 때 기존 파일은 백업하고 메모리에 로딩되어 있는 내용으로 저장되게 해주세요.