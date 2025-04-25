package com.likelion.backendplus4.yakplus.index.application.port.in;

import com.likelion.backendplus4.yakplus.index.presentation.controller.dto.request.IndexRequest;

public interface IndexUseCase {
    void index(IndexRequest request);
}