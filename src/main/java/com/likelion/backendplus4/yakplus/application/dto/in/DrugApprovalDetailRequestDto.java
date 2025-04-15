package com.likelion.backendplus4.yakplus.application.dto.in;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DrugApprovalDetailRequestDto {
	private Integer pageNo;
	private Integer numOfRows;
	private String type;

	private String itemName;
	private String entpName;
	private String itemPermitDate;
	private String entpNo;
	private String barCode;
	private String itemSeq;
	private String startChangeDate;
	private String endChangeDate;
	private String ediCode;
	private String atcCode;
	private String bizrno;
	private String rareDrugYn;

}
