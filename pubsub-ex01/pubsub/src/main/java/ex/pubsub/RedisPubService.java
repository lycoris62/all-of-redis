package ex.pubsub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisPubService {

    private final RedisPublisher redisPublisher;
    private final RedisMessageListenerContainer redisMessageListenerContainer;

    // 각 Channel 별 Listener
    private final RedisSubscribeListener redisSubscribeListener;


    /**
     * Channel 별 Message 전송
     */
    public void pubMsgChannel(String channel, MessageDto message) {

        //1. 요청한 Channel 을 구독.
        redisMessageListenerContainer.addMessageListener(redisSubscribeListener, ChannelTopic.of(channel));

        //2. Message 전송
        redisPublisher.publish(ChannelTopic.of(channel), message);
    }

    /**
     * Channel 구독 취소
     */
    public void cancelSubChannel(String channel) {
        redisMessageListenerContainer.removeMessageListener(redisSubscribeListener);
    }
}