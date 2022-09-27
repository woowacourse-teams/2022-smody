import { MappedKeyToUnion } from '../dist';
import { Expect, Equal } from './test-util';
import { expectType } from 'tsd';

test('foo test', () => {
  const COLOR_1 = { LIGHT_PURPLE: 1, bb: '2' } as const;
  const COLOR_2 = { aaa: 1, bb: '2' } as const;

  expectType<
    Expect<Equal<MappedKeyToUnion<typeof COLOR_1>, MappedKeyToUnion<typeof COLOR_2>>>
  >(true);
});
