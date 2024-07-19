package com.woowahanrabbits.battle_people.domain.BalanceGame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.woowahanrabbits.battle_people.domain.BalanceGame.service.BalanceGameService;
import com.woowahanrabbits.battle_people.domain.battle.domain.BattleBoard;
import com.woowahanrabbits.battle_people.domain.battle.dto.BattleReturnDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/balance-game")
@Tag(name = "BalanceGameController", description = "밸런스게임 컨트롤러")
public class BalanceGameController {

	@Autowired
	private BalanceGameService balanceGameService;

	@PostMapping("")
	@Operation(summary = "[점화] 밸런스 게임을 생성한다.")
	public ResponseEntity<?> registBalanceGame(@RequestBody BattleReturnDto battleReturnDto) {

		balanceGameService.addBalanceGame(battleReturnDto);

		System.out.println(battleReturnDto.getBattleBoard().toString());
		System.out.println(battleReturnDto.getBattleBoard().getRegistUser().toString());
		System.out.println(battleReturnDto.getBattleBoard().getOppositeUser().toString());
		System.out.println(battleReturnDto.getOpinionList().toString());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
