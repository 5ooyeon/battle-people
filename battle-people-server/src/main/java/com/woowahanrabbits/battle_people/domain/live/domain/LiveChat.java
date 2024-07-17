package com.woowahanrabbits.battle_people.domain.live.domain;

import com.woowahanrabbits.battle_people.domain.battle.domain.BattleBoard;
import com.woowahanrabbits.battle_people.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class LiveChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "battle_board_id")
    private BattleBoard battleBoard;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String chatContext;
    private Date registDate;
    private int selectedOpinion;
    private boolean hasRequest;

    @PrePersist
    protected void onCreate() {
        this.registDate = new Date();
    }
}
