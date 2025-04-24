package com.likelion.backendplus4.yakplus.temp.dao;


import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.likelion.backendplus4.yakplus.temp.entity.document.EEDocDocument;

public interface EEDocRepository extends ElasticsearchRepository<EEDocDocument, String> {
}
