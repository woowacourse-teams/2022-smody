package com.woowacourse.smody.service;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.dto.ChallengeRequest;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
