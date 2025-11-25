package com.example.backend.infra.google.drive;

import com.example.backend.infra.google.dto.GoogleFormCreateResponseDto;
import com.google.auth.oauth2.AccessToken;

import java.io.IOException;
import java.util.List;

public interface GoogleDriveService {
    String getValidAccessToken(String username) throws IOException;
    AccessToken refreshAccessToken(String refreshToken) throws IOException;
    GoogleFormCreateResponseDto createFormInDrive(String username, String formName, List<String> searchColumns, String refreshToken);
}
