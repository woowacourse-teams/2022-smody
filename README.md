# SMODY(스모디)

## 깃 브랜치 전략

- 새로운 기능은 develop 브랜치로 부터 feature 브랜치를 만듭니다.
    - feature 브랜치를 만들기 전 develop 브랜치를 rebase 해서 최신의 상태로 만듭니다.
    - 프론트엔드는 feature/fe/[작업이름] , 백엔드는 feature/be/[작업이름] 으로 생성합니다.
- feature 브랜치를 develop 브랜치로 pull request 를 작성합니다.
- pull request 에 대해 코드 리뷰를 요청합니다.
- 코드 리뷰가 완료되면 github 의 Squash and merge 기능을 사용해서 merge를 진행합니다.
- 새로운 버전 배포시에는 develop 에서 release-v0.0.0같은 브랜치를 생성합니다.
- release 에서 태그 작업, QA후에 release 브랜치에서 main 브랜치에 pull request작성하고 merge합니다.
    - main으로 merge 시킬때는 github의 create a merge commit 기능을 사용합니다.
- main 에서 최신화된 내용을 develop 에도 적용합니다.
