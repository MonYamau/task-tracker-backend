package ru.monyamau.task_tracker_backend.api;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.monyamau.task_tracker_backend.dto.response.TaskInternalResponseDto;

import java.time.OffsetDateTime;
import java.util.List;

@Hidden
@RequestMapping("/internal")
public interface InternalApi {
    @GetMapping("/period-tasks")
    ResponseEntity<List<TaskInternalResponseDto>> show(
            @RequestParam("from")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @NotNull(message = "Поле времени начала не может отсутствовать")
            @PastOrPresent(message = "Некорректное значение: начало периода не может быть в будущем")
            OffsetDateTime from,
            @RequestParam("to")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @NotNull(message = "Поле времени окончания не может отсутствовать")
            @PastOrPresent(message = "Некорректное значение: окончание периода не может быть в будущем")
            OffsetDateTime to);
}