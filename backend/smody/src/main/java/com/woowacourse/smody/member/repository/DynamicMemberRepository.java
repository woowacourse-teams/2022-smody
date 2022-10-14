package com.woowacourse.smody.member.repository;

import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.member.domain.Member;
import java.util.List;

public interface DynamicMemberRepository {

    List<Member> findAllByFilter(PagingParams pagingParams);
}
