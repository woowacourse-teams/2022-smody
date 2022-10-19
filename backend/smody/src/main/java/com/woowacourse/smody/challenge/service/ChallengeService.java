package com.woowacourse.smody.challenge.service;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.challenge.repository.ChallengeRepository;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    public Challenge search(Long challengeId) {
        return challengeRepository.findById(challengeId)
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_CHALLENGE));
    }

    public Optional<Challenge> findById(Long challengeId) {
        return challengeRepository.findById(challengeId);
    }

    public List<Challenge> findAllByFilter(PagingParams pagingParams) {
        String searchWord = pagingParams.getFilter();
        validateSearchWord(searchWord);
        return challengeRepository.findAllByFilter(pagingParams);
    }

    private void validateSearchWord(String searchWord) {
        if (searchWord != null && (searchWord.isBlank() || searchWord.isEmpty())) {
            throw new BusinessException(ExceptionData.INVALID_SEARCH_NAME);
        }
    }

    @Transactional
    public Long create(String challengeName, String description, Integer emojiIndex, Integer colorIndex) {
        validateDuplicatedName(challengeName);
        Challenge challenge = challengeRepository.save(
                new Challenge(challengeName, description, emojiIndex, colorIndex)
        );
        return challenge.getId();
    }

    private void validateDuplicatedName(String name) {
        if (challengeRepository.findByName(name).isPresent()) {
            throw new BusinessException(ExceptionData.DUPLICATE_NAME);
        }
    }

    public List<Challenge> findRandomChallenges(Integer size) {
        List<Long> allIds = challengeRepository.findAllIds();
        validateSize(allIds, size);
        Collections.shuffle(allIds);
        List<Long> randomIds = allIds.subList(0, size);
        return challengeRepository.findAllByIdIn(randomIds);
    }

    private void validateSize(List<Long> allIds, Integer size) {
        if (allIds.size() < size) {
            throw new BusinessException(ExceptionData.OUT_OF_BOUND_RANDOM_SIZE);
        }
    }
}
