## 서비스명 - MyHouse

### 서비스 설명
MyHouse는 한 숙소의 개인용 사이트를 계획으로 만들어지게 되었으며 숙소에 오는 손님이 이 숙소가 어떤지 숙소 소개와 예약을 확인할 수 있으며 손님이 예약을 잡거나 관리자가 숙소 예약한 손님 현황을 알아볼 수 있게 기획하였습니다.
<br>

## 🛠 개발환경
- 프론트엔드: HTML, CSS, JavaScript
- 백엔드: Java Spring Framework
- 데이터베이스: MariaDB
- 보안: HTTPS, OAuth 2.0 (카카오 로그인)
<br>
- 
## ☁️ 흐름도
![flowchart](https://private-user-images.githubusercontent.com/133177283/302850785-112e5658-15ab-4de9-ac8f-9d96b42edcb8.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MDcyNzY2NTAsIm5iZiI6MTcwNzI3NjM1MCwicGF0aCI6Ii8xMzMxNzcyODMvMzAyODUwNzg1LTExMmU1NjU4LTE1YWItNGRlOS1hYzhmLTlkOTZiNDJlZGNiOC5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQwMjA3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MDIwN1QwMzI1NTBaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0wNGMxNWQyMzczZWU4ZTg0YjA1MDAzZTFhMjE1ZTM4MjhkODc5OTE5YTgzNTY0ZGZjNGI4YjIxZjdmOTgwNjhlJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCZhY3Rvcl9pZD0wJmtleV9pZD0wJnJlcG9faWQ9MCJ9.62IvchGiwoez-l3GrOV_BW0NYry5TcnuM4dmQUNOm_8)
<br>

## ☁️ ERD
![ERD](https://private-user-images.githubusercontent.com/133177283/302534458-bfaf2ca5-d933-4818-8be7-c9d0a84a47b3.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MDcyMDQzMDIsIm5iZiI6MTcwNzIwNDAwMiwicGF0aCI6Ii8xMzMxNzcyODMvMzAyNTM0NDU4LWJmYWYyY2E1LWQ5MzMtNDgxOC04YmU3LWM5ZDBhODRhNDdiMy5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQwMjA2JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MDIwNlQwNzIwMDJaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT00Mjg0MTU1ZjE4ODM5NWUxYmZkMDA5ZWUyZDU4NDRmZjFkOGFlYTMyMDkxNDcwZThiZWE3YWE0YTI3Y2IzNDZlJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCZhY3Rvcl9pZD0wJmtleV9pZD0wJnJlcG9faWQ9MCJ9.8PK6r2JeA_59FjTZDOk38h9A150gExD7Pfn8kg4CBRE)
<br>

## 👀 시연영상
#### 누르면 새창으로 이동합니다.
[![movie](https://img.youtube.com/vi//0.jpg)](https://youtu.be/)
## 🔥 트러블 슈팅
### 🚧 이슈 제목
### stackoverflowerror 에러 발생
### 🤔 문제점 설명
- 로그인을 시도하게 되면 stackoverflowerror 에러가 발생하면서 로그인이 안되는 증상이 발생하였다.
  <br>

### 🛑 원인
- 스프링 시큐리티가 로그인 시 사용할 UserSecurityService는 스프링 시큐리티가 제공하는 UserDetailsService 인터페이스를
구현(implements)해야 한다. 스프링 시큐리티의 UserDetailsService는 loadUserByUsername 메서드를 구현하도록 강제하는 인터페이스이다. loadUserByUsername 메서드는 사용자명(username)으로
스프링 시큐리티의 사용자(User) 객체를 조회하여 리턴하는 메서드이다. 그런데 UserSecurityService 파일이
없었기 때문에 무한 재귀가 발생하여 stackoverflowerror 에러가 발생했던 것이다.
  <br>

### 🚥 해결
- UserSecurityService 파일을 도입하여 stackoverflowerror 에러를 해결하였다.