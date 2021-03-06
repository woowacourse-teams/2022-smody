package com.woowacourse.smody.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.repository.ChallengeRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    public Challenge search(Long challengeId) {
        return challengeRepository.findById(challengeId)
            .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_CHALLENGE));
    }

    public List<Challenge> searchAll() {
        return challengeRepository.findAll();
    }
}
