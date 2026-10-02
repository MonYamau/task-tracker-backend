package ru.monyamau.task_tracker_backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.monyamau.task_tracker_backend.dto.response.TaskResponseDto;
import ru.monyamau.task_tracker_backend.entity.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(source = "ready", target = "isReady")
    TaskResponseDto toDto(Task task);
}