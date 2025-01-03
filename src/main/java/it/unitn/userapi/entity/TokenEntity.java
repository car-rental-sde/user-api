package it.unitn.userapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "token")
public class TokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "TEXT")
    private String tokenValue;

    private LocalDateTime created;

    public TokenEntity(String tokenValue) {
        this.tokenValue = tokenValue;
        created = LocalDateTime.now();
    }
}
