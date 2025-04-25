package com.likelion.backendplus4.yakplus.index.application.port.out;

import com.likelion.backendplus4.yakplus.index.domain.model.Drug;

import java.util.List;

public interface DrugIndexRepositoryPort {
    void saveAll(List<Drug> drugs);
}