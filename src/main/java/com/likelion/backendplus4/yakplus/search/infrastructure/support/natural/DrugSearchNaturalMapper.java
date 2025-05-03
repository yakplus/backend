package com.likelion.backendplus4.yakplus.search.infrastructure.support.natural;

import com.fasterxml.jackson.databind.JsonNode;
import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchDomainNatural;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DrugSearchNaturalMapper {
    public static DrugSearchDomainNatural toDomainNatural(JsonNode src) {
        long id = src.path("drugId").asLong();
        String name = src.path("drugName").asText();
        String company = src.path("company").asText();
        List<String> efficacy = StreamSupport.stream(src.path("efficacy").spliterator(), false)
                .map(JsonNode::asText)
                .collect(Collectors.toList());
        String imageUrl = src.path("imageUrl").asText(null);

        return DrugSearchDomainNatural.builder()
                .drugId(id)
                .drugName(name)
                .company(company)
                .efficacy(efficacy)
                .imageUrl(imageUrl)
                .build();
    }
}
