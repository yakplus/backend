package com.likelion.backendplus4.yakplus.els.core;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drugs")
@RequiredArgsConstructor
public class DrugController {
    private final DrugService svc;

    @PostMapping("/index-all")
    public void indexAll() {
        svc.indexAll();
    }

    @GetMapping("/search-all")
    public List<Drug> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return svc.search(q, page, size);
    }
}