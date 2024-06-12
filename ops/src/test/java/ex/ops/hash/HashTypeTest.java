package ex.ops.hash;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

@DisplayName("Hash 타입 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HashTypeTest {

    @Autowired
    RedisTemplate<String, HashClass> redisTemplate;
    HashOperations<String, String, HashClass> hashOperations;

    @BeforeEach
    void setUp() {
        hashOperations = redisTemplate.opsForHash();
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll(); // 매 테스트 시 초기화
    }

    @DisplayName("HSET, HGET, HMGET, HGETALL : 해시 아이템 다루기")
    @Test
    void test_hset_hget() {
        // given
        HashClass hashClass = new HashClass("key01", "value01", 1004, 600L);

        // when
        hashOperations.put("a", "key01", hashClass);

        HashClass findHashClass = hashOperations.get("a", "key01"); // HMGET or HGETALL

        // then
        assertThat(findHashClass.getId()).isEqualTo("key01");
        assertThat(findHashClass.getValue()).isEqualTo("value01");
        assertThat(findHashClass.getNumber()).isEqualTo(1004L);
    }

    @DisplayName("HEXISTS : 존재 여부 테스트")
    @Test
    void test_hexists() {
        // given
        HashClass hashClass = new HashClass("key01", "value01", 1004, 600L);
        hashOperations.put("a", "key01", hashClass);

        // when
        Boolean hasKey = hashOperations.hasKey("a", "key01");
        Boolean hasNotKey = hashOperations.hasKey("a", "key02");

        // then
        assertThat(hasKey).isTrue();
        assertThat(hasNotKey).isFalse();
    }
}
