package com.woowacourse.smody.challenge.sorthandler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChallengeSortHandlerMapping {

    private final List<ChallengeSortHandler> challengeSortHandlers;
    private final DefaultChallengeSortHandler defaultChallengeSortHandler;

    public ChallengeSortHandler getHandler(String pagingParamsSortValue) {
        return challengeSortHandlers.stream()
                .filter(challengeSortHandlerHandler -> challengeSortHandlerHandler.getSortValue()
                        .equals(pagingParamsSortValue))
                .findAny()
                .orElse(defaultChallengeSortHandler);
    }
}
