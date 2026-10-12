package ru.monyamau.task_tracker_backend.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.monyamau.task_tracker_backend.dto.response.LetterMessageDto;
import ru.monyamau.task_tracker_backend.exception.LetterPublisherException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class KafkaLetterProducer implements LetterPublisher {
    private final KafkaTemplate<String, LetterMessageDto> kafkaTemplate;
    private final String topic;

    public KafkaLetterProducer(KafkaTemplate<String, LetterMessageDto> kafkaTemplate, @Value("${kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void sendLetter(LetterMessageDto messageDto) {
        try {
            kafkaTemplate.send(topic, messageDto).get(5, TimeUnit.SECONDS);
            log.info("Запрос на отправку письма зарегистрированному пользователю {} направлен успешно", messageDto.recipient());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new LetterPublisherException("Возникла ошибка потока при попытке направить запрос", e);
        } catch (TimeoutException e) {
            throw new LetterPublisherException("Не удалось получить подтверждение от Kafka о получении", e);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            throw new LetterPublisherException("Не удалось направить запрос на отправку письма", cause);
        } catch (Exception e) {
            throw new LetterPublisherException(
                    "Ошибка при отправке запроса в Kafka", e);
        }
    }
}