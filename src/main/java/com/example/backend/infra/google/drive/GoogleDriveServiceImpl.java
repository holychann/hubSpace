package com.example.backend.infra.google.drive;

import com.example.backend.domain.user.entity.UserEntity;
import com.example.backend.domain.user.service.UserService;
import com.example.backend.infra.google.dto.GoogleFormCreateResponseDto;
import com.example.backend.infra.google.dto.GoogleFormQuestionsIdsResponseDto;
import com.example.backend.infra.google.dto.GoogleFormResponseDto;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.UserCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.api.services.forms.v1.Forms;
import com.google.api.services.forms.v1.model.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleDriveServiceImpl implements GoogleDriveService{

    private final String APPLICATION_NAME = "HubSpace";

    private final UserService userService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    /**
     * 유효한 Access Token 을 반환합니다.
     * @param username
     * @return
     * @throws IOException
     */
    @Override
    public String getValidAccessToken(String username) throws IOException {

        UserEntity user = userService.findUserByUsername(username);

        if(user.getGoogleAccessToken() != null &&
                user.getAccessTokenExpiresAt().isAfter(LocalDateTime.now().plusMinutes(5))){

            return user.getGoogleAccessToken();
        }

        AccessToken accessToken = refreshAccessToken(user.getGoogleRefreshToken());
        Date expirationTime = accessToken.getExpirationTime();
        LocalDateTime expiresAt = LocalDateTime.ofInstant(
                expirationTime.toInstant(),
                ZoneId.systemDefault()
        );

        userService.updateAccessToken(username, accessToken.getTokenValue(), expiresAt);

        return accessToken.getTokenValue();
    }

    /**
     * Access Token 만료 시 재발급
     * @param refreshToken
     * @return
     * @throws IOException
     */
    @Override
    public AccessToken refreshAccessToken(String refreshToken) throws IOException {

        log.info("🌐[GOOGLE][ACCESS TOKEN][START] 발급 시작 | refreshToken: {}", refreshToken);

        UserCredentials credentials = UserCredentials.newBuilder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRefreshToken(refreshToken)
                .build();

        credentials.refreshIfExpired();

        AccessToken accessToken = credentials.getAccessToken();

        log.info("🌐[GOOGLE][ACCESS TOKEN][END] 발급 완료 | accessToken: {}", accessToken.getTokenValue());

        return accessToken;
    }

    /**
     * Google Form 파일 생성
     * @param username 사용자 이름(예시: GOOGLE_1235235131)
     * @param formTitle 파일명
     * @param searchColumns 검색할 컬럼명 목록
     * @param refreshToken Refresh Token
     * @return Google Form 파일 생성 결과(url, id)
     */
    @Override
    public GoogleFormCreateResponseDto createFormInDrive(String username, String formTitle, List<String> searchColumns, String refreshToken) {
        try {

            log.info("📋[GOOGLE][FORM][START] Google Form 파일 생성 시작 | username: {}, formName: {}", username, formTitle);
            String accessToken = getValidAccessToken(username);

            Drive driveService = createDriveServiceInstance(accessToken);

            // List[0]: 파일 ID, List[1]: 파일 URL
            List<String> formdata = createGoogleFormFile(driveService, formTitle);
            String formId = formdata.get(0);
            String formUrl = formdata.get(1);
            log.info("📋[GOOGLE][FORM][END] Google Form 파일 생성 완료 | formId: {}", formId);

            // 질문 추가
            GoogleFormQuestionsIdsResponseDto googleFormQuestionsIdsResponseDto = addQuestionsToForm(formId, searchColumns, accessToken);

            // 응답 목록 반환
            return GoogleFormCreateResponseDto.of(formId, formUrl, googleFormQuestionsIdsResponseDto);

        } catch (Exception e) {
            log.error("구글 드라이브 작업 중 오류 발생", e);
            throw new RuntimeException("구글 드라이브 파일 생성 실패", e);
        }
    }

    /**
     * Drive Service 인스턴스 생성
     * @param accessToken Valid Access Token
     * @return Drive Service 인스턴스
     */
    private Drive createDriveServiceInstance(String accessToken) throws GeneralSecurityException, IOException {

        GoogleCredentials credentials = GoogleCredentials.create(new AccessToken(accessToken, null));

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Google Form 파일 생성
     * @param drive Drive Service 인스턴스
     * @param formTitle 파일명
     * @return List[파일 ID, 파일 URL]
     */
    private List<String> createGoogleFormFile(Drive drive, String formTitle) throws Exception {
        File fileMetadata = new File();
        fileMetadata.setName(formTitle);
        fileMetadata.setMimeType("application/vnd.google-apps.form");

        File file = drive.files().create(fileMetadata)
                .setFields("id, webViewLink")
                .execute();

        List<String> metadata = new ArrayList<>();
        metadata.add(file.getId());
        metadata.add(file.getWebViewLink());

        return metadata;
    }

    /**
     * Google Form Service 인스턴스 생성
     * @param accessToken Valid Access Token
     * @return Forms Service 인스턴스
     */
    private Forms createFormsService(String accessToken) throws GeneralSecurityException, IOException {
        GoogleCredentials credentials = GoogleCredentials.create(new AccessToken(accessToken, null));

        return new Forms.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Google Form에 질문 추가
     * @param formId Google Form ID
     * @param searchColumns 검색용 컬럼명 목록
     * @param accessToken Valid Access Token
     */
    private GoogleFormQuestionsIdsResponseDto addQuestionsToForm(String formId, List<String> searchColumns, String accessToken) throws Exception {

        Forms formsService = createFormsService(accessToken);

        List<Request> requests = new ArrayList<>();

        // 배열 순서대로 질문 추가 요청 만들기
        for (int i = 0; i < searchColumns.size(); i++) {
            String colName = searchColumns.get(i);

            // "주관식 단답형(TextQuestion)" 질문 생성
            Request request = new Request()
                    .setCreateItem(new CreateItemRequest()
                            .setItem(new Item()
                                    .setTitle(colName) // 질문 제목 (예: 이름, 학번)
                                    .setQuestionItem(new QuestionItem()
                                            .setQuestion(new Question()
                                                    .setRequired(true) // 필수 응답 여부
                                                    .setTextQuestion(new TextQuestion())) // 텍스트 입력형
                                    )
                            )
                            .setLocation(new Location().setIndex(i)) // 순서 지정
                    );
            requests.add(request);
        }

        BatchUpdateFormRequest batchRequest = new BatchUpdateFormRequest().setRequests(requests);
        BatchUpdateFormResponse response = formsService.forms().batchUpdate(formId, batchRequest).execute();

        Map<String, String> columnIdMap = new HashMap<>();
        List<Response> replies = response.getReplies();

        if (replies != null) {
            for (int i = 0; i < searchColumns.size(); i++) {
                // 요청했던 컬럼명
                String colName = searchColumns.get(i);

                // 그에 해당하는 응답 (순서가 보장됨)
                Response reply = replies.get(i);

                // 계층 구조를 타고 내려가서 ID 추출
                // Response -> CreateItemResponse -> Item -> QuestionItem -> Question -> QuestionId
                CreateItemResponse createItemResponse = reply.getCreateItem();

                List<String> questionIds = createItemResponse.getQuestionId();
                if (questionIds != null && !questionIds.isEmpty()) {
                    String questionId = questionIds.get(0);
                    columnIdMap.put(colName, questionId);
                }
            }
        }

        return GoogleFormQuestionsIdsResponseDto.of(columnIdMap);

    }

    /**
     * Google Form에 답변을 저장한 응답 목록을 반환합니다.
     * @param formId Google Form ID
     * @param accessToken Valid Access Token
     * @param searchColumnIds 검색용 컬럼 ID 목록
     * @return 응답 목록 List
     */
    public List<GoogleFormResponseDto> getFormResponses(String formId, String accessToken, List<String> searchColumnIds) throws IOException {

        try {
            Forms formsService = createFormsService(accessToken);

            // 응답 목록 조회 요청
            ListFormResponsesResponse rawResponses = formsService.forms().responses().list(formId)
                    .setPageSize(500)
                    .execute();
            List<FormResponse> responses = rawResponses.getResponses();
            List<GoogleFormResponseDto> responseDtoList = new ArrayList<>();

            if (rawResponses == null || rawResponses.isEmpty()) {
                log.info("📭 아직 응답이 없습니다. | formId: {}", formId);

                // 비어있다면 깡통 List 반환
                return responseDtoList;
            }

            for (FormResponse raw : responses) {
                Map<String, String> parsedAnswers = new HashMap<>();

                // 답변이 있는 경우에만 처리
                if (raw.getAnswers() != null) {
                    raw.getAnswers().forEach((questionId, answerObj) -> {

                        // 조회용 컬럼만 빼오기!
                        if (searchColumnIds.contains(questionId)) {
                            String textValue = extractTextValue(answerObj);
                            parsedAnswers.put(questionId, textValue);
                        }
                    });
                }

                responseDtoList.add(GoogleFormResponseDto.of(
                        raw.getResponseId(),
                        raw.getCreateTime(),
                        parsedAnswers
                ));
            }

            log.info("📭 응답 목록 조회 완료 | formId: {}, responseCount: {}", formId, responseDtoList.size());

            return responseDtoList;

        } catch (Exception e) {
            log.error("구글 폼 데이터 받아오기 작업 중 오류 발생", e);
            throw new RuntimeException(e);
        }

    }

    /**
     * Answer 객체에서 텍스트 값만 추출하는 헬퍼 메소드
     * @param answer Answer 객체
     * @return 추출된 텍스트 값
     */
    private String extractTextValue(Answer answer) {
        // 텍스트 답변이 있는 경우 (주관식, 객관식 등)
        if (answer.getTextAnswers() != null && answer.getTextAnswers().getAnswers() != null) {
            List<String> values = new ArrayList<>();
            for (TextAnswer textAnswer : answer.getTextAnswers().getAnswers()) {
                values.add(textAnswer.getValue());
            }
            // 값이 여러 개면 콤마(,)로 연결, 하나면 그냥 반환
            return String.join(", ", values);
        }
        // TODO: 그리드형, 날짜형 등 다른 타입의 답변 처리 로직 추가 가능
        return "";
    }

}
