// package com.likelion.backendplus4.yakplus.index.infrastructure.adapter.persistence;
//
// import com.likelion.backendplus4.yakplus.index.application.port.out.EmbeddingPort;
// import com.likelion.backendplus4.yakplus.index.exception.IndexException;
// import com.likelion.backendplus4.yakplus.index.exception.error.IndexErrorCode;
// import lombok.RequiredArgsConstructor;
// import org.springframework.ai.document.MetadataMode;
// import org.springframework.ai.embedding.EmbeddingResponse;
// import org.springframework.ai.openai.OpenAiEmbeddingModel;
// import org.springframework.ai.openai.OpenAiEmbeddingOptions;
// import org.springframework.ai.openai.api.OpenAiApi;
// import org.springframework.ai.retry.RetryUtils;
// import org.springframework.stereotype.Component;
//
// import java.util.Arrays;
// import java.util.List;
//
// /**
//  * OpenAI 임베딩 API를 호출하여 텍스트에 대한 벡터 임베딩을 생성하는 어댑터 클래스입니다.
//  * EmbeddingPort 인터페이스를 구현하며, 오류 발생 시 SearchException으로 래핑합니다.
//  *
//  * @since 2025-04-22
//  * @modified 2025-04-24
//  */
// @Component
// @RequiredArgsConstructor
// public class OpenAIEmbeddingAdapter implements EmbeddingPort {
//     private final OpenAiApi openAiApi;
//     private static final String EMBEDDING_MODEL = "text-embedding-3-small";
//
//     /**
//      * 주어진 텍스트를 OpenAI 임베딩 모델에 전달하여 벡터 값을 반환합니다.
//      * 내부에서 OpenAiEmbeddingModel을 생성하고 retry 템플릿을 적용합니다.
//      * API 호출 중 예외가 발생하면 SearchException(EMBEDDING_API_ERROR)을 던집니다.
//      *
//      * @param text 벡터화할 입력 텍스트
//      * @return float 배열 형태의 임베딩 벡터
//      * @throws IndexException EMBEDDING_API_ERROR 코드로 래핑하여 발생
//      * @author 정안식
//      * @since 2025-04-22
//      * @modified 2025-04-24
//      */
//     @Override
//     public float[] getEmbedding(String text) {
//         try {
//             OpenAiEmbeddingModel embeddingModel = createEmbeddingModel();
//             EmbeddingResponse response = embeddingModel.embedForResponse(List.of(text));
//             return response.getResults().getFirst().getOutput();
//         } catch (Exception e) {
//             //TODO: LOG ERROR 처리 요망
// //            log(LOGLEVEL.ERROR, "임베딩 API에서 문제가 발생하였습니다., e);
//             throw new IndexException(IndexErrorCode.EMBEDDING_API_ERROR);
//         }
//     }
//
//     /**
//      * OpenAiEmbeddingModel 인스턴스를 생성하여 반환합니다.
//      * MetadataMode와 모델 이름, RetryUtils 설정이 포함됩니다.
//      *
//      * @return 초기화된 OpenAiEmbeddingModel 객체
//      * @author 정안식
//      * @since 2025-04-22
//      * @modified 2025-04-24
//      */
//     private OpenAiEmbeddingModel createEmbeddingModel() {
//         return new OpenAiEmbeddingModel(
//                 openAiApi,
//                 MetadataMode.EMBED,
//                 OpenAiEmbeddingOptions.builder()
//                         .model(EMBEDDING_MODEL)
//                         .build(),
//                 RetryUtils.DEFAULT_RETRY_TEMPLATE
//         );
//     }
// }