package github.dwstanle.tickets.model;

import java.util.UUID;

public interface Data {
    static Long generateId() {
        return UUID.randomUUID().getLeastSignificantBits();
    }
}
