package ex.pubsub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/redis/pubsub")
public class RedisPubSubController {

    private final RedisPubService redisSubscribeService;

    @PostMapping("/send")
    public void sendMessage(@RequestParam String channel, @RequestBody MessageDto message) {
        log.info("Redis Pub MSG Channel = {}", channel);
        redisSubscribeService.pubMsgChannel(channel, message);
    }

    @PostMapping("/cancel")
    public void cancelSubChannel(@RequestParam String channel) {
        redisSubscribeService.cancelSubChannel(channel);
    }
}