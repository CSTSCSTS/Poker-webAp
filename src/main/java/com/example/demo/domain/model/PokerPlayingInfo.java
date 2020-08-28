package com.example.demo.domain.model;

import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.dto.PokerPlayingInfoDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PokerPlayingInfo {

	public List<Card> deck;
	public List<Card> playerHands;
	public List<Card> computerHands;
	public Role playerRole;
	public Role computerRole;
	private Winner winner;
	private boolean isFinishedChange;

	public enum Winner {
		PLAYER, CPU, NOTHING;
	}

//	public static PokerPlayingInfo ConvertToDomain(PokerPlayingInfoDto dto) {
//		return PokerPlayingInfo.builder()
//						.deck(dto.getDeck().stream().map(d -> Card.ConvertToDomain(d)).collect(Collectors.toList()))
//						.playerHands(dto.getPlayerHands().stream().map(p -> Card.ConvertToDomain(p)).collect(Collectors.toList()))
//						.computerHands(dto.getComputerHands().stream().map(c -> Card.ConvertToDomain(c)).collect(Collectors.toList()))
//						.playerRole(dto.getPlayerRole().map(r -> Role.ConvertToDomain(r)).orElse(null))
//						.computerRole(dto.getComputerRole().map(r -> Role.ConvertToDomain(r)).orElse(null))
//						.winner(dto.getWinner().map(w -> Winner.valueOf(w)).orElse(null))
//						.isFinishedChange(dto.isFinishedChange())
//						.build();
//	}

	public static PokerPlayingInfo ConvertToDomainPrepare(PokerPlayingInfoDto dto) {
			return PokerPlayingInfo.builder()
							.deck(dto.getDeck().stream().map(d -> Card.ConvertToDomain(d)).collect(Collectors.toList()))
							.playerHands(dto.getPlayerHands().stream().map(p -> Card.ConvertToDomain(p)).collect(Collectors.toList()))
							.computerHands(dto.getComputerHands().stream().map(c -> Card.ConvertToDomain(c)).collect(Collectors.toList()))
							.playerRole(null)
							.computerRole(null)
							.winner(null)
							.isFinishedChange(dto.isFinishedChange())
							.build();
		}

	public static PokerPlayingInfo ConvertToDomainHandChange(PokerPlayingInfoDto dto) {
			return PokerPlayingInfo.builder()
							.deck(dto.getDeck().stream().map(d -> Card.ConvertToDomain(d)).collect(Collectors.toList()))
							.playerHands(dto.getPlayerHands().stream().map(p -> Card.ConvertToDomain(p)).collect(Collectors.toList()))
							.computerHands(dto.getComputerHands().stream().map(c -> Card.ConvertToDomain(c)).collect(Collectors.toList()))
							.playerRole(Role.ConvertToDomain(dto.getPlayerRole()))
							.computerRole(Role.ConvertToDomain(dto.getComputerRole()))
							.winner(Winner.valueOf(dto.getWinner()))
							.isFinishedChange(dto.isFinishedChange())
							.build();
		}

}
