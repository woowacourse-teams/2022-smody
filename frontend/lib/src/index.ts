// eslint-disable-next-line @typescript-eslint/no-namespace
namespace SmodyLib {
  /**
   * Object 타입의 value를 Union 타입으로 변환해주는 메타 타입
   *
   * @typeParam T key의 타입이 string인 Object
   *
   * const COLOR = {LIGHT_PURPLE: "#F5F3FF", PURPLE: "#7B61FF", DARK_PURPLE: "#7054FE"}
   * type AvailablePickedColor = MappedKeyToUnion<typeof COLOR>;
   * // => "#F5F3FF" | "#7B61FF" | "#7054FE"
   */
  export type MappedKeyToUnion<T> = T extends { [key: string]: infer K } ? K : never;

  /**
   * Object 타입의 특정 key의 type을 가져오는 메타 타입
   *
   * @typeParam T key의 타입이 string인 Object
   * @typeParam U T에서 타입을 가져올 key
   *
   */
  export type PickType<T extends { [key: string]: unknown }, U extends keyof T> = Pick<
    T,
    U
  >[U];
}

export = SmodyLib;
