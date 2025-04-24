package com.likelion.backendplus4.yakplus.search.application.port.in;

import org.springframework.web.bind.annotation.RequestParam;

public record SearchRequest(
        @RequestParam String query,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
}
