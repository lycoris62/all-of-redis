package ex.leaderboard;

import ex.leaderboard.domain.ItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/{id}/purchase")
    public ResponseEntity<Void> purchase(@PathVariable Long id) {
        itemService.purchase(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ranks")
    public List<ItemDto> getRanks() {
        return itemService.getMostSold();
    }
}
