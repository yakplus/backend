package com.likelion.backendplus4.yakplus.search.application.port.in;

import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.request.IndexRequest;

public interface IndexUseCase {
    void index(IndexRequest request);
}