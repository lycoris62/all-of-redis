package ex.ops;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@DisplayName("String 타입 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StringTypeTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    private ValueOperations<String, String> valueOperations;
    private ValueOperations<String, Integer> intValueOperations;

    @BeforeEach
    void setUp() {
        valueOperations = stringRedisTemplate.opsForValue();
        intValueOperations = redisTemplate.opsForValue();
        stringRedisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll(); // 매 테스트 시 초기화
    }

    @DisplayName("set, get : 테스트")
    @Test
    void test_set_get() {
        // when
        valueOperations.set("a", "b");

        String value = valueOperations.get("a");

        // then
        assertThat(value).isEqualTo("b");
    }

    @DisplayName("set nx : 키가 존재하지 않으면 갱신 테스트")
    @Test
    void test_set_nx() {
        // given
        valueOperations.set("a", "b");

        // when
        valueOperations.setIfAbsent("a", "bb"); // set NX

        String value = valueOperations.get("a");

        //then
        assertThat(value).isEqualTo("b");
    }

    @DisplayName("set xx : 키가 존재하면 갱신 테스트")
    @Test
    void test_set_xx() {
        // given
        valueOperations.set("a", "b");

        // when
        valueOperations.setIfPresent("a", "bb"); // set XX

        String value = valueOperations.get("a");

        // then
        assertThat(value).isEqualTo("bb");
    }

    @DisplayName("mset, mget : 다중 스트링 테스트")
    @Test
    void test_mset_mget() {
        // given
        Map<String, String> multiString = Map.of(
            "a", "b",
            "aa", "bb"
        );

        // when
        valueOperations.multiSet(multiString);

        List<String> multiValue = valueOperations.multiGet(multiString.keySet());

        // then
        assertThat(multiValue).contains("b", "bb");
    }

    @DisplayName("incr : 아토믹 숫자 증가 테스트")
    @Test
    void test_incr() {
        // given
        intValueOperations.set("a", 1);

        // when
        Long value = intValueOperations.increment("a");

        // then
        assertThat(value).isEqualTo(2);
    }
}
