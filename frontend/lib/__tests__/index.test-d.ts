import { MappedKeyToUnion, PickType } from '../dist';
import { Equal } from './test-util';
import { expectType } from 'tsd';

test('MappedKeyToUnion 타입을 테스트한다. `{ a: 1; b: "2" }` 객체에 대하여 MappedKeyToUnion을 통해 기대하는 타입인 `1 | "2"` 이라는 유니온 타입이 반환되는지 확인한다.', () => {
  type MockData = { a: 1; b: '2' };
  type ExpectedType = 1 | '2';
  expectType<Equal<MappedKeyToUnion<MockData>, ExpectedType>>(true);
});

test('PickType 타입을 테스트한다. `type A = { a: string; b: number }` 타입 대하여 PickType<A, "a">을 통해 기대하는 타입인 `string`  타입이 반환되는지 확인한다.', () => {
  type A = { a: string; b: number };
  type ExpectedTypeA = string;
  type ExpectedTypeB = number;

  expectType<Equal<PickType<A, 'a'>, ExpectedTypeA>>(true);
  expectType<Equal<PickType<A, 'b'>, ExpectedTypeB>>(true);
});
