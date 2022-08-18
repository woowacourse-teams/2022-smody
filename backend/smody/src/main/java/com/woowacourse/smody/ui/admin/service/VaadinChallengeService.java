package com.woowacourse.smody.ui.admin.service;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.challenge.repository.ChallengeRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VaadinChallengeService implements SmodyVaddinService<Challenge> {

    private final ChallengeRepository challengeRepository;

    @Override
    @Transactional
    public void deleteById(Long id) {
        challengeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Challenge save(Challenge challenge) {
        return challengeRepository.save(challenge);
    }

    @Override
    public Optional<Challenge> findById(Long id) {
        return challengeRepository.findById(id);
    }

    @Override
    public List<Challenge> findAll() {
        return challengeRepository.findAll();
    }
}
