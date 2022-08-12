package com.woowacourse.smody.ui.admin.service;

import com.woowacourse.smody.domain.CycleDetail;
import com.woowacourse.smody.repository.FeedRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VaadinCycleDetailService implements SmodyVaddinService<CycleDetail> {

    private final FeedRepository feedRepository;
    @Override
    @Transactional
    public void deleteById(Long id) {
        feedRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CycleDetail save(CycleDetail cycleDetail) {
        return feedRepository.save(cycleDetail);
    }

    @Override
    public List<CycleDetail> findAll() {
        return feedRepository.findAll();
    }
}
