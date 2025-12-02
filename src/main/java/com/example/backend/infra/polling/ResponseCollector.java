package com.example.backend.infra.polling;

import com.example.backend.infra.google.drive.GoogleDriveService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ResponseCollector {

    private final GoogleDriveService googleDriveService;

}
