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

        // 구독자가 특정 토픽에 구독할 때 사용. 주석 풀면 RedisSubscribeListener 에 의해 로그로 메시지 볼 수 있음
//        redisMessageListenerContainer.addMessageListener(redisSubscribeListener, ChannelTopic.of(channel));

        // 구독자에게 메시지 전송
        redisPublisher.publish(ChannelTopic.of(channel), message);
    }

    /**
     * Channel 구독 취소
     */
    public void cancelSubChannel(String channel) {
        redisMessageListenerContainer.removeMessageListener(redisSubscribeListener);
    }
}