package ex.pubsub;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> template;

    /**
     * Object publish
     */
    public void publish(ChannelTopic topic, MessageDto dto) {
        template.convertAndSend(topic.getTopic(), dto);
    }
}