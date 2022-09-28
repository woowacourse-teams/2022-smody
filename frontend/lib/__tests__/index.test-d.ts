import { MappedKeyToUnion } from '../dist';
import { Equal } from './test-util';
import { expectType } from 'tsd';

test('MappedKeyToUnion 타입을 테스트한다. `{ a: 1; b: "2" }` 객체에 대하여 MappedKeyToUnion을 통해 기대하는 타입인 `1 | "2"` 이라는 유니온 타입이 반환되는지 확인한다.', () => {
  type MockData = { a: 1; b: '2' };
  type ExpectedType = 1 | '2';
  expectType<Equal<MappedKeyToUnion<MockData>, ExpectedType>>(true);
});
