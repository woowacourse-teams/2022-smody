package com.woowacourse.smody.ui.admin.service;

import com.woowacourse.smody.domain.CycleDetail;
import com.woowacourse.smody.repository.CycleDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VaadinCycleDetailService implements SmodyVaddinService<CycleDetail> {

    private final CycleDetailRepository cycleDetailRepository;
    @Override
    public void deleteById(Long id) {
        cycleDetailRepository.deleteById(id);
    }

    @Override
    public CycleDetail save(CycleDetail cycleDetail) {
        return cycleDetailRepository.save(cycleDetail);
    }

    @Override
    public List<CycleDetail> findAll() {
        return cycleDetailRepository.findAll();
    }
}
