package com.likelion.backendplus4.yakplus.service;

import com.likelion.backendplus4.yakplus.domain.DrugCombinedInfo;
import com.likelion.backendplus4.yakplus.domain.DrugImageInfo;
import com.likelion.backendplus4.yakplus.domain.DrugPermitInfo;
import com.likelion.backendplus4.yakplus.port.DrugCombinedInfoPort;
import com.likelion.backendplus4.yakplus.port.DrugImageInfoPort;
import com.likelion.backendplus4.yakplus.port.DrugPermitInfoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * DrugDataMergeService는 DrugPermitInfo와 DrugImageInfo를 ITEM_SEQ 기준으로 병합하여
 * DrugCombinedInfo 도메인에 저장하는 역할을 수행한다.
 *
 * [동작 원리]
 * 1. DrugPermitInfoPort를 통해 저장된 모든 item_seq를 조회한다.
 * 2. 각 item_seq에 대해 DrugPermitInfo와 DrugImageInfo를 조회한다.
 * 3. 두 정보를 결합한 DrugCombinedInfo 객체를 생성한 후, DrugCombinedInfoPort로 저장한다.
 *
 * @since 2025-04-14 최초 작성
 * @author 정안식
 */
@Service
@RequiredArgsConstructor
public class DrugDataMergeService {

    private final DrugPermitInfoPort permitPort;
    private final DrugImageInfoPort imagePort;
    private final DrugCombinedInfoPort combinedPort;

    /**
     * DrugPermitInfo와 DrugImageInfo를 병합하여 DrugCombinedInfo 정보를 생성 후 저장한다.
     *
     * 프로세스 순서:
     * 1. DrugPermitInfoPort에서 모든 item_seq를 조회한다.
     * 2. 각 item_seq별로 DrugPermitInfo와 DrugImageInfo를 조회한다.
     * 3. DrugPermitInfo의 데이터와 DrugImageInfo의 이미지 URL을 결합한 DrugCombinedInfo 객체를 생성한다.
     * 4. 생성된 DrugCombinedInfo 객체를 DrugCombinedInfoPort를 통해 저장한다.
     */
    // TODO: @Transactional 처리 해야함, 전역 예외 처리 필요
    public void mergeDrugData() {
        List<String> itemSeqList = permitPort.findAllItemSeqs();
        for (String itemSeq : itemSeqList) {
            DrugPermitInfo permit = permitPort.findById(itemSeq).orElse(null);
            DrugImageInfo image = imagePort.findById(itemSeq).orElse(null);
            if (permit != null) {
                DrugCombinedInfo combined = DrugCombinedInfo.builder()
                        .itemSeq(permit.getItemSeq())
                        .itemName(permit.getItemName())
                        .entpName(permit.getEntpName())
                        .itemPermitDate(permit.getItemPermitDate())
                        .etcOtcCode(permit.getEtcOtcCode())
                        .chart(permit.getChart())
                        .materialName(permit.getMaterialName())
                        .storageMethod(permit.getStorageMethod())
                        .validTerm(permit.getValidTerm())
                        .packUnit(permit.getPackUnit())
                        .makeMaterialFlag(permit.getMakeMaterialFlag())
                        .cancelName(permit.getCancelName())
                        .totalContent(permit.getTotalContent())
                        .eeDocData(permit.getEeDocData())
                        .udDocData(permit.getUdDocData())
                        .nbDocData(permit.getNbDocData())
                        .pnDocData(permit.getPnDocData())
                        .ingrName(permit.getIngrName())
                        .rareDrugYn(permit.getRareDrugYn())
                        .bigPrdtImgUrl(image != null ? image.getBigPrdtImgUrl() : null)
                        .build();
                combinedPort.save(combined);
            }
        }
    }
}
