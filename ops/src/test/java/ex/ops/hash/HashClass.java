package ex.ops.hash;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash(timeToLive = 1L)
public class HashClass {

    @Id
    private String id;

    private String value;

    private Integer number;

    @TimeToLive
    private Long ttl;

    public HashClass() {
    }

    public HashClass(String id, String value, Integer number, Long ttl) {
        this.id = id;
        this.value = value;
        this.number = number;
        this.ttl = ttl;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public Integer getNumber() {
        return number;
    }

    public Long getTtl() {
        return ttl;
    }
}
