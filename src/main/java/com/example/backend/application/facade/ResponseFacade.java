package com.example.backend.application.facade;


import com.example.backend.domain.response.dto.ResponseQueryDto;

public interface ResponseFacade {
    ResponseQueryDto getResponse(ResponseQueryDto responseQueryDto);
}
