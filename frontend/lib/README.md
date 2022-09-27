<p align="middle" >
  <img src="https://techcourse-storage.s3.ap-northeast-2.amazonaws.com/49031e8eef91405f824a0438ac1b0059" width="600">
</p>

## 🚀 smody util library

- 소개

  - 스모디 프로젝트에서 사용되는 범용 타입 모듈입니다.
  - `MappedKeyToUnion` 타입을 제공합니다.

- 설치

```bash
npm install smody-library --save-dev
```

- 실행

```tsx
import { MappedKeyToUnion } from 'smody-library';

const COLOR = {
  LIGHT_PURPLE: '#F5F3FF',
  PURPLE: '#7B61FF',
  DARK_PURPLE: '#7054FE',
} as const;

type AvailablePickedColor = MappedKeyToUnion<typeof COLOR>;
// => type AvailablePickedColor = "#F5F3FF" | "#7B61FF" | "#7054FE";
```

- [smody 유틸 npm 라이브러리 주소](https://www.npmjs.com/package/smody-library)
- [smody 프로젝트 구경가기](https://www.smody.co.kr)
- [smody 프로젝트 git 저장소](https://github.com/woowacourse-teams/2022-smody)
