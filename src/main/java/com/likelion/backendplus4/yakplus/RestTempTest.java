package com.likelion.backendplus4.yakplus;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class RestTempTest {

	@GetMapping("/sibar")
	public void tester(){
		RestTempTest.main();
	}
	public static void main() {
		try {
			// URI uri = UriComponentsBuilder.newInstance()
			// 	.scheme("https")
			// 	.host("apis.data.go.kr")
			// 	.port(443)
			// 	.path("/1471000/DrugPrdtPrmsnInfoService06/getDrugPrdtPrmsnDtlInq05")
			// 	.queryParam("serviceKey",
			// 		"kHctorEOW58GMVU768tVFqRHC6ytY0HrNiZxXY10hMb7UkEkZJKzmW+1uSx5bgM/wi9h94UoZ31oKDh+xKjQGQ==")
			// 	.queryParam("type", "json")
			// 	.queryParam("numOfRows", 1)
			// 	.build(true)
			// 	.toUri();

			RestTemplate template = new RestTemplate();
			String url = "https://apis.data.go.kr/1471000/DrugPrdtPrmsnInfoService06/getDrugPrdtPrmsnDtlInq05?type=json&numOfRows=5&serviceKey=kHctorEOW58GMVU768tVFqRHC6ytY0HrNiZxXY10hMb7UkEkZJKzmW+1uSx5bgM/wi9h94UoZ31oKDh+xKjQGQ==";
			String response = template.getForObject(url, String.class);
			System.out.println("response = " + response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
