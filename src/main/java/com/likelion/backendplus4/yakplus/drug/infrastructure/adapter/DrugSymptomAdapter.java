package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.document.DrugSymptomDocument;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.elasticsearch.DrugSymptomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DrugSymptomAdapter {

	private final DrugSymptomRepository symptomRepository;

	@Transactional
	public void saveAll(List<DrugSymptomDocument> docs) {
		symptomRepository.saveAll(docs);
	}
}

