package com.example.backend.domain.response.service;

import com.example.backend.domain.response.dto.ResponseQueryDto;
import com.example.backend.domain.response.entity.ResponseEntity;

import java.util.Map;

public interface ResponseQueryService {

    public ResponseEntity getResponse(ResponseQueryDto responseQueryDto);

}
