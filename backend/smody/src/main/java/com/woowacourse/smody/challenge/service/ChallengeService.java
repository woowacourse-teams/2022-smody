package com.woowacourse.smody.challenge.service;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.challenge.dto.ChallengeRequest;
import com.woowacourse.smody.challenge.repository.ChallengeRepository;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import java.util.List;
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

    public List<Challenge> searchAll(String name, Long cursorId, Integer size) {
        return challengeRepository.searchAll(name, cursorId, size);
    }

    @Transactional
    public Long create(ChallengeRequest challengeRequest) {
        validateDuplicatedName(challengeRequest.getChallengeName());
        validateNameFormat(challengeRequest.getChallengeName());
        Challenge challenge = challengeRepository.save(new Challenge(
                challengeRequest.getChallengeName(), challengeRequest.getDescription(),
                challengeRequest.getEmojiIndex(), challengeRequest.getColorIndex()));
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
