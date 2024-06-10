package ex.ops;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisListCommands.Direction;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

@DisplayName("List 타입 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ListTypeTest {

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;
    private ListOperations<String, Integer> listOperations;

    @BeforeEach
    void setUp() {
        listOperations = redisTemplate.opsForList();
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll(); // 매 테스트 시 초기화
    }

    @DisplayName("LPUSH : 왼쪽 push 테스트")
    @Test
    void test_lpush_1() {
        // given
        listOperations.leftPush("a", 1);
        listOperations.leftPush("a", 2);
        listOperations.leftPush("a", 3);

        // when
        // Redis 에서는 LRANGE 만 있음, RRANGE는 없음.
        List<Integer> value = listOperations.range("a", 0, -1);

        // then
        assertThat(value).containsExactly(3, 2, 1);
    }

    @DisplayName("LPUSH : 왼쪽 push 테스트 - 제한된 range")
    @Test
    void test_lpush_2() {
        // given
        listOperations.leftPush("a", 1);
        listOperations.leftPush("a", 2);
        listOperations.leftPush("a", 3);

        // when
        List<Integer> value = listOperations.range("a", 0, 1);

        // then
        assertThat(value).containsExactly(3, 2);
    }

    @DisplayName("RPUSH : 오른쪽 push 테스트")
    @Test
    void test_rpush() {
        // given
        listOperations.rightPush("a", 1);
        listOperations.rightPush("a", 2);
        listOperations.rightPush("a", 3);

        // when
        List<Integer> value = listOperations.range("a", 0, -1);

        // then
        assertThat(value).containsExactly(1, 2, 3);
    }

    @DisplayName("LPOP : 왼쪽 pop 테스트")
    @Test
    void test_lpop() {
        // given
        listOperations.rightPush("a", 1);
        listOperations.rightPush("a", 2);
        listOperations.rightPush("a", 3);

        // when
        Integer value = listOperations.leftPop("a");
        List<Integer> list = listOperations.range("a", 0, -1);

        // then
        assertThat(value).isEqualTo(1);
        assertThat(list).containsExactly(2, 3);
    }

    @DisplayName("RPOP : 오른쪽 pop 테스트")
    @Test
    void test_rpop() {
        // given
        listOperations.rightPush("a", 1);
        listOperations.rightPush("a", 2);
        listOperations.rightPush("a", 3);

        // when
        Integer value = listOperations.rightPop("a");
        List<Integer> list = listOperations.range("a", 0, -1);

        // then
        assertThat(value).isEqualTo(3);
        assertThat(list).containsExactly(1, 2);
    }

    @DisplayName("LTRIM : 지정 범위 밖의 요소들 삭제")
    @Test
    void test_ltrim_1() {
        // given
        listOperations.rightPush("a", 1);
        listOperations.rightPush("a", 2);
        listOperations.rightPush("a", 3);
        listOperations.rightPush("a", 4);

        // when
        listOperations.trim("a", 1, 2);
        List<Integer> list = listOperations.range("a", 0, -1);

        // then
        assertThat(list).containsExactly(2, 3);
    }

    @DisplayName("LTRIM : 지정 범위 밖의 요소들 삭제 - 범위보다 작은 리스트인 경우")
    @Test
    void test_ltrim_2() {
        // given
        listOperations.rightPush("a", 1);
        listOperations.rightPush("a", 2);
        listOperations.rightPush("a", 3);

        // when
        listOperations.trim("a", 1, 100);
        List<Integer> list = listOperations.range("a", 0, -1);

        // then
        assertThat(list).containsExactly(2, 3);
    }

    @DisplayName("LINSERT : 원하는 위치에 데이터 추가")
    @Test
    void test_linsert() {
        // given
        listOperations.rightPush("a", 1);
        listOperations.rightPush("a", 2);
        listOperations.rightPush("a", 3);

        // when
//        listOperations.set("a", 1, 100);
        listOperations.rightPush("a", 2, 100); // LINSERT a AFTER 2 100 - 2 이후에 100 추가
        listOperations.leftPush("a", 2, 50); // LINSERT a BEFORE 2 50  - 2 이전에 50 추가
        List<Integer> list = listOperations.range("a", 1, 3);

        // then
        assertThat(list).containsExactly(50, 2, 100);
    }

    @DisplayName("LSET : 원하는 위치의 값을 변경")
    @Test
    void test_lset() {
        // given
        listOperations.rightPush("a", 1);
        listOperations.rightPush("a", 2);
        listOperations.rightPush("a", 3);

        // when
        listOperations.set("a", 1, 100);
        Integer value = listOperations.index("a", 1);

        // then
        assertThat(value).isEqualTo(100);
    }

    @DisplayName("LINDEX : 원하는 인덱스의 값 확인")
    @Test
    void test_lindex() {
        // given
        listOperations.rightPush("a", 1);
        listOperations.rightPush("a", 2);
        listOperations.rightPush("a", 3);

        // when
        Integer value = listOperations.index("a", 1);

        // then
        assertThat(value).isEqualTo(2);
    }

    @DisplayName("LMOVE : 다른 리스트로 값 옮기기")
    @Test
    void test_lmove() {
        // given
        listOperations.rightPush("a", 1);
        listOperations.rightPush("a", 2);
        listOperations.rightPush("a", 3);

        // when
        // a 리스트의 RIGHT 값을 b 리스트의 LEFT 로 이동 (a의 3을 b에 왼쪽으로 이동)
        listOperations.move("a", Direction.RIGHT, "b", Direction.LEFT);
        // a 리스트의 LEFT 값을 b 리스트의 RIGHT 로 이동 (a의 1을 b에 오른쪽으로 이동)
        listOperations.move("a", Direction.LEFT, "b", Direction.RIGHT);
        List<Integer> list = listOperations.range("b", 0, -1);

        // then
        assertThat(list).containsExactly(3, 1);
    }
}
