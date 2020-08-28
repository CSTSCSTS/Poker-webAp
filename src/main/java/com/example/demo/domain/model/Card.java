package com.example.demo.domain.model;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.dto.CardDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Card {

	public CardType type;
	public Integer number;
	public Boolean isChange;

	public static List<Card> makeDeck(boolean jokerIncluded) {

		List<Card> deck = new ArrayList<>();

		setAceToKing(CardType.SPADE, deck);
		setAceToKing(CardType.CLUB, deck);
		setAceToKing(CardType.HEART, deck);
		setAceToKing(CardType.DIAMOND, deck);
      if(jokerIncluded) {
        deck.add(Card.builder().type(CardType.JOKER).number(0).isChange(false).build());
      }
		return deck;

	}

	private static List<Card> setAceToKing(CardType type, List<Card> deck) {
		for(int number = 1; number<=13; number++) {
          deck.add(Card.builder().type(type).number(number).isChange(false).build());
	    }
			return deck;
		}

	public static Card ConvertToDomain(CardDto dto) {
   return Card.builder()
   				.type(CardType.valueOf(dto.getType()))
   				.number(dto.getNumber())
   				.isChange(dto.isChange())
   				.build();
	}

}
