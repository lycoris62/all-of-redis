package ex.sessionstore;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/session")
    public ResponseEntity<String> getSession(HttpServletRequest request) {

        HttpSession session = request.getSession();
        String attribute = (String) session.getAttribute("username");
        log.info("sessionAttr:username : {}", attribute);

        return ResponseEntity.ok(attribute);
    }

    @PostMapping("/dummy")
    public ResponseEntity<Void> login(@RequestBody LoginReq dto, HttpServletRequest request) {

        HttpSession session = request.getSession();
        session.setAttribute("username", dto.username());
        session.setMaxInactiveInterval(3600);

        return ResponseEntity.ok().build();
    }
}
