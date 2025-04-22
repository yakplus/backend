package com.likelion.backendplus4.yakplus.search.application.port.in;

public record IndexRequest(
        Long lastSeq,
        int limit) {
}