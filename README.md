# SMODY(스모디) 

<img src="https://user-images.githubusercontent.com/59413128/185350531-1ecaaf78-e295-4bf5-b14a-4c5208e84c65.png" width="100"> <img src="https://user-images.githubusercontent.com/59413128/185350386-6b9d9f8b-8f86-4f63-84c1-9b52dd9fbf19.png" alt="PWA" width="300">

https://www.smody.co.kr

## 프로젝트 설명

원하는 목표나 계획을 쉽게 이루기 위한 동기부여 서비스

목표 주기를 3회로 정하여 작은 성공을 반복하도록 유도한다.

## 멤버

### Frontend

|                                                마르코                                                 |                                                  빅터                                                  |                                                   우연                                                   |
| :---------------------------------------------------------------------------------------------------: | :----------------------------------------------------------------------------------------------------: | :------------------------------------------------------------------------------------------------------: |
| <img src="https://avatars.githubusercontent.com/u/59413128?v=4" alt="marco" width="100" height="100"> | <img src="https://avatars.githubusercontent.com/u/52148907?v=4" alt="victor" width="100" height="100"> | <img src="https://avatars.githubusercontent.com/u/70249108?v=4" alt="woo_yeon" width="100" height="100"> |
|                                  [wonsss](https://github.com/wonsss)                                  |                                 [woose28](https://github.com/woose28)                                  |                                    [ronci](https://github.com/ronci)                                     |

### Backend

|                                                 토닉                                                  |                                                 알파                                                  |                                                 더즈                                                 |                                                  조조그린                                                  |
| :---------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------------------------: |
| <img src="https://avatars.githubusercontent.com/u/59171113?v=4" alt="tonic" width="100" height="100"> | <img src="https://avatars.githubusercontent.com/u/50986686?v=4" alt="alpha" width="100" height="100"> | <img src="https://avatars.githubusercontent.com/u/78652144?v=4" alt="does" width="100" height="100"> | <img src="https://avatars.githubusercontent.com/u/82805588?v=4" alt="jojo_green" width="100" height="100"> |
|                                [tonic523](https://github.com/tonic523)                                |                                 [bcc0830](https://github.com/bcc0830)                                 |                              [ldk980130](https://github.com/ldk980130)                               |                               [jojogreen91](https://github.com/jojogreen91)                                |

## 기술스택

### Frontend

<img width="528" alt="image" src="https://user-images.githubusercontent.com/59413128/185350085-f0dfdfc8-f28e-41a2-a637-9426e5217f78.png">

- [Figma 링크](https://www.figma.com/file/HQinpzR8FuXUFxTZzzzCpf/Smody-Design-System)
- [Storybook 링크](https://woowacourse-teams.github.io/2022-smody)

### Backend

<img width="529" alt="image" src="https://user-images.githubusercontent.com/59413128/196944126-2cd8cb03-b261-4555-828f-29d9f0173d98.png">


### Infra

<img width="528" alt="image" src="https://user-images.githubusercontent.com/59413128/185350044-159d30d9-751a-495e-94b5-3f61a433f5e9.png">

## 프로젝트 로컬 실행 방법
### 백엔드 로컬

- 자바 버전 openJDK는 11이어야 한다.
- application.properties 파일 코드를 추가해야 한다.

```bash
cd backend/smody

./gradlew bootRun --args='--spring.profiles.active=local --batch.schedule.enabled=true'
```

### 프론트엔드 로컬

- node 버전은 16.1.0이어야 한다.

```bash
cd frontend

# 패키지 설치
yarn

# 로컬 모드 구동(백엔드 로컬 8080과 연결)
yarn run start:local

# 개발 모드 구동(msw)
yarn run start:dev
```
