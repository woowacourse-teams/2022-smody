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
}

export = SmodyLib;
