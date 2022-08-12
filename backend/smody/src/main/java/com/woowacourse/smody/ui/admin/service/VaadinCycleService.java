package com.woowacourse.smody.ui.admin.service;

import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.repository.CycleRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VaadinCycleService implements SmodyVaddinService<Cycle> {

    private final CycleRepository cycleRepository;
    @Override
    @Transactional
    public void deleteById(Long id) {
        cycleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Cycle save(Cycle cycle) {
        return cycleRepository.save(cycle);
    }

    @Override
    public List<Cycle> findAll() {
        return cycleRepository.findAll();
    }

    @Override
    public Optional<Cycle> findById(Long id) {
        return cycleRepository.findById(id);
    }
}
