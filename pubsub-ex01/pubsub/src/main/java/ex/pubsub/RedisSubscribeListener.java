package ex.pubsub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscribeListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> template;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = template.getStringSerializer().deserialize(message.getBody());

            MessageDto messageDto = objectMapper.readValue(publishMessage, MessageDto.class);

            log.info("Redis Message Id : {}", messageDto.getMessageId());
            log.info("Redis SUB Message : {}", publishMessage);

        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}