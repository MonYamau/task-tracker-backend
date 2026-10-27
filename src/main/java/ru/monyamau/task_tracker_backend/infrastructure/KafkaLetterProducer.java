package ru.monyamau.task_tracker_backend.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.monyamau.task_tracker_backend.dto.response.LetterMessageDto;
import ru.monyamau.task_tracker_backend.exception.LetterPublisherException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class KafkaLetterProducer implements LetterPublisher {
    private final KafkaTemplate<String, LetterMessageDto> kafkaTemplate;
    private final String topic;

    public KafkaLetterProducer(KafkaTemplate<String, LetterMessageDto> kafkaTemplate, @Value("${kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void publishLetterTask(LetterMessageDto messageDto) {
        try {
            kafkaTemplate.send(topic, messageDto).get(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new LetterPublisherException("Попытка публикации задачи для отправки письма в Kafka была прервана", e);
        } catch (TimeoutException e) {
            throw new LetterPublisherException("Не удалось получить подтверждение от Kafka о получении задачи", e);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            throw new LetterPublisherException("Не удалось опубликовать задачу отправки письма в Kafka", cause);
        } catch (Exception e) {
            throw new LetterPublisherException(
                    "Ошибка при попытке опубликовать задачу для отправки письма в Kafka", e);
        }
    }
}