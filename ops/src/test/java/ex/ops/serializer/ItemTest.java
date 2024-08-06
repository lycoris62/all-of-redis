package ex.ops.serializer;

import ex.ops.config.Item;
import ex.ops.config.Product;
import ex.ops.config.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

/**
 * 이 테스트를 사용할 땐 test 디렉토리의 설정을 주석처리 후 main 디렉토리의 설정으로 적용
 */
@SpringBootTest
public class ItemTest {

    @Autowired
    RedisTemplate<String, Item> template;

    @Autowired
    ProductRepository productRepository;

    @Test
    void itemSerializerTest() {
        ValueOperations<String, Item> itemOperations = template.opsForValue();

        itemOperations.set("my:keyboard", Item.builder()
            .name("keyboard")
            .description("expensive")
            .build());

        System.out.println(itemOperations.get("my:keyboard"));
    }

    @Test
    void itemSerializerTest2() {
        HashOperations<String, String, Item> ops = template.opsForHash();

        ops.put("my", "piano", Item.builder()
            .name("piano")
            .description("fun")
            .build());

        Item item = ops.get("my", "piano");
        System.out.println("item = " + item);
    }

    @Test
    void repositoryTest01() {

        Product product = new Product("key2", "value2");

        Product savedProduct = productRepository.save(product);

        System.out.println("savedItem2 = " + savedProduct);
    }

    @Test
    void zSetTest01() {
        ZSetOperations<String, Item> ops = template.opsForZSet();

        Item item = Item.builder()
            .name("one")
            .description("no")
            .views(0L)
            .build();

        ops.incrementScore("article", item, 1);

        Double score = ops.score("article", item);

        System.out.println("score = " + score);

        Item item2 = Item.builder()
            .name("two")
            .description("yes")
            .views(0L)
            .build();

        Double score2 = ops.score("article", item2);
        System.out.println("score2 = " + score2);
    }
}
