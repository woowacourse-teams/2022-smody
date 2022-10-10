package com.woowacourse.smody.challenge.service;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.challenge.repository.ChallengeRepository;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
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

    public List<Challenge> searchAll(PagingParams pagingParams) {
        String searchWord = pagingParams.getFilter();
        validateSearchWord(searchWord);
        return challengeRepository.searchAll(pagingParams);
    }

    private void validateSearchWord(String searchWord) {
        if (searchWord != null && (searchWord.isBlank() || searchWord.isEmpty())) {
            throw new BusinessException(ExceptionData.INVALID_SEARCH_NAME);
        }
    }

    @Transactional
    public Long create(String challengeName, String description, Integer emojiIndex, Integer colorIndex) {
        validateDuplicatedName(challengeName);
        validateNameFormat(challengeName);
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

    private void validateNameFormat(String name) {
        if (name.isBlank() || name.isEmpty()) {
            throw new BusinessException(ExceptionData.INVALID_CHALLENGE_NAME);
        }
        if (name.length() > 30) {
            throw new BusinessException(ExceptionData.INVALID_CHALLENGE_NAME);
        }
        if (name.trim().length() != name.length()) {
            throw new BusinessException(ExceptionData.INVALID_CHALLENGE_NAME);
        }
    }
}
