package ex.pubsub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String messageId; // 메세지 아이디
    private String sender; // 메세지 발신자
    private String message; // 전송할 메세지 내용
}