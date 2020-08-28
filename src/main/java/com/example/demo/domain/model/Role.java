package com.example.demo.domain.model;

import java.util.Arrays;
import java.util.List;

import com.example.demo.dto.RoleDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class Role {

	private RoleName roleName;
	private int roleNumber;

	public static Role getFiveCard() {
		return new Role(RoleName.FIVE_CARD, 10);
	}

	public static Role getRoyelStraightFlush() {
		return new Role(RoleName.ROYEL_STRAIGHT_FLUSH, 9);
	}

	public static Role getStraightFlush() {
		return new Role(RoleName.STRAIGHT_FLUSH, 8);
	}

	public static Role getFourCard() {
		return new Role(RoleName.FOUR_CARD, 7);
	}

	public static Role getFullHouse() {
		return new Role(RoleName.FULL_HOUSE, 6);
	}

	public static Role getFlush() {
		return new Role(RoleName.FLUSH, 5);
	}

	public static Role getStraight() {
		return new Role(RoleName.STRAIGHT, 4);
	}

	public static Role getThreeCard() {
		return new Role(RoleName.THREE_CARD, 3);
	}

	public static Role getTwoPair() {
		return new Role(RoleName.TWO_PAIR, 2);
	}

	public static Role getOnePair() {
		return new Role(RoleName.ONE_PAIR, 1);
	}

	public static Role getHighCard() {
		return new Role(RoleName.HIGH_CARD, 0);
	}

	public static List<Role> noChangeRoles() {
		return (List<Role>) Arrays.asList(
				getFiveCard(),
				getRoyelStraightFlush(),
				getStraightFlush(),
				getFullHouse(),
				getFlush(),
				getStraight());
	}

	public static Role ConvertToDomain(RoleDto dto) {
   return Role.builder()
   				.roleName(RoleName.valueOf(dto.getRoleName()))
   				.roleNumber(dto.getRoleNumber())
   				.build();
	}

}
