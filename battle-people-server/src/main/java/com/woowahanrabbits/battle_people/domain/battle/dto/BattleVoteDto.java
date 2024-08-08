package com.woowahanrabbits.battle_people.domain.battle.dto;

import java.util.List;

import com.woowahanrabbits.battle_people.domain.battle.domain.BattleBoard;
import com.woowahanrabbits.battle_people.domain.vote.domain.VoteInfo;
import com.woowahanrabbits.battle_people.domain.vote.domain.VoteOpinion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BattleVoteDto {
	private BattleBoard battleBoard;
	private VoteInfo voteInfo;
	private List<VoteOpinion> voteOpinions;
}
