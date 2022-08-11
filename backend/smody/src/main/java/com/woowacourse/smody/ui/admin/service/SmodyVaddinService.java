package com.woowacourse.smody.ui.admin.service;

import com.woowacourse.smody.domain.Challenge;

import java.util.List;
import java.util.Optional;

public interface SmodyVaddinService<T> {

    void deleteById(Long id);

    T save(T t);

    default Optional<T> findById(Long id) {
        return Optional.empty();
    }

    List<T> findAll();
}
