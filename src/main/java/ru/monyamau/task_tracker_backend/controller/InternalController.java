package ru.monyamau.task_tracker_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.monyamau.task_tracker_backend.api.InternalApi;
import ru.monyamau.task_tracker_backend.dto.response.TaskInternalResponseDto;
import ru.monyamau.task_tracker_backend.service.InternalService;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
public class InternalController implements InternalApi {
    private final InternalService internalService;

    public InternalController(InternalService internalService) {
        this.internalService = internalService;
    }

    @Override
    public ResponseEntity<List<TaskInternalResponseDto>> show(OffsetDateTime from, OffsetDateTime to) {
        List<TaskInternalResponseDto> responseDto = internalService.findAll(from, to);
        return ResponseEntity.ok(responseDto);
    }
}