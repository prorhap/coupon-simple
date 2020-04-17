# Simple Coupon System

## 사용 라이브러리 및 프레임워크
* SpringBoot, SpringData, Hibernate 

## 개발 방향 및 참고
* Coupon, User 도메인 객체를 구성하고 행위 및 속성을 정의했어요. 
* 문제 설명 제일 앞 부분에 "할인, 선물등 쿠폰" 이라는 언급이 있어서 할인, 선물 타입의 쿠폰 클래스를 정의해서 SingleTable로 매핑합니다.
* Entity 객체가 생성되면 생성 날짜 정보가 자동으로 생기고, 변경될 때 마다 변경일이 자동 갱신 됩니다.
* 테스트는 연동되는 부분은 mocking해서 단위 테스트로 작성했어요.
* 날짜 표현 및 쿠폰 포매팅은 Json Serializer를 구현해서 적용했습니다.
* 날짜 정보는 대부분 new Date() 하지 않고 CouponDateUtils를 통해 생성합니다. 날짜를 기준으로 하는 비즈니스를 테스트 할 때 유용해요.  
* DB는 어플리케이션을 구동되면서 H2 DB를 띄워서 사용합니다.

## 빌드 및 실행
```bash
./gradlew clean bootRun
```
* H2 Console: http://localhost:8080/h2 
