package com.likelion.backendplus4.yakplus.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

/**
 * DrugImageInfo는 의약품 이미지 정보를 나타내는 도메인 객체이다.
 *
 * @since 2025-04-14 최초 작성
 * @author 정안식
 */
@Entity
@Table(name = "drug_image_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugImageInfo {

    @Id
    @Column(name = "item_seq", nullable = false, length = 50)
    private String itemSeq; // 품목기준코드

    @Column(name = "big_prdt_img_url", columnDefinition = "TEXT")
    private String bigPrdtImgUrl; // 이미지 URL
}
