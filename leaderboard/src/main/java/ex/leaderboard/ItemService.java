package ex.leaderboard;

import ex.leaderboard.domain.Item;
import ex.leaderboard.domain.ItemDto;
import ex.leaderboard.domain.ItemOrder;
import ex.leaderboard.repository.ItemRepository;
import ex.leaderboard.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final RedisTemplate<String, ItemDto> rankTemplate;

    public void purchase(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        log.info("item : {}", item);

        orderRepository.save(ItemOrder.builder()
                .item(item)
                .count(1)
                .build());

        ZSetOperations<String, ItemDto> ops = rankTemplate.opsForZSet();
        ops.incrementScore("soldRank", ItemDto.fromEntity(item), 1);
    }

    public List<ItemDto> getMostSold() {
        ZSetOperations<String, ItemDto> ops = rankTemplate.opsForZSet();
        Set<ItemDto> ranks = ops.reverseRange("soldRank", 0, 9);

        if (ranks == null) {
            return Collections.emptyList();
        }

        return ranks.stream().toList();
    }
}
