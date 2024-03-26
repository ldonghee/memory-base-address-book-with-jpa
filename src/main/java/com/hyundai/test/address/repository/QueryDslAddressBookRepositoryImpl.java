package com.hyundai.test.address.repository;

import static com.hyundai.test.address.domain.QAddressBookEntity.addressBookEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;

import com.hyundai.test.address.domain.AddressBookFilterType;
import com.hyundai.test.address.dto.AddressBookSearchCondition;
import com.hyundai.test.address.domain.AddressBookEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class QueryDslAddressBookRepositoryImpl implements QueryDslAddressBookRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<AddressBookEntity> findAllAddressBook(AddressBookSearchCondition addressBookSearchCondition) {
		return jpaQueryFactory.select(addressBookEntity)
							  .from(addressBookEntity)
							  .where(clauseCondition(addressBookSearchCondition))
							  .orderBy(orderCondition(addressBookSearchCondition))
							  .fetch();
	}

	private BooleanBuilder clauseCondition(AddressBookSearchCondition condition) {
		BooleanBuilder builder = new BooleanBuilder();
		AddressBookFilterType filterType = condition.getFilterType();

		switch (filterType) {
			case PHONE_NUMBER -> builder.and(phoneNumberEqual(condition.getFilterValue()));
			case EMAIL -> builder.and(emailEqual(condition.getFilterValue()));
			case ADDRESS -> builder.and(addressLike(condition.getFilterValue()));
			case NAME -> builder.and(nameLike(condition.getFilterValue()));
		}

		return builder;
	}

	private OrderSpecifier<?> orderCondition(AddressBookSearchCondition condition) {
		Order order = Optional.of(Order.valueOf(condition.getSortOrder())).orElse(Order.DESC);
		AddressBookFilterType sortBy = condition.getSortBy();

		return switch (sortBy) {
			case EMAIL -> new OrderSpecifier(order, addressBookEntity.email);
			case ADDRESS -> new OrderSpecifier(order, addressBookEntity.address);
			case NAME -> new OrderSpecifier(order, addressBookEntity.name);
			default -> new OrderSpecifier(order, addressBookEntity.phoneNumber);
		};
	}

	private BooleanExpression phoneNumberEqual(String phoneNumber) {
		return StringUtils.hasText(phoneNumber) ? addressBookEntity.phoneNumber.eq(phoneNumber) : null;
	}

	private BooleanExpression emailEqual(String email) {
		return StringUtils.hasText(email) ? addressBookEntity.email.eq(email) : null;
	}

	private BooleanExpression addressLike(String address) {
		return StringUtils.hasText(address) ? addressBookEntity.address.contains(address) : null;
	}

	private BooleanExpression nameLike(String name) {
		return StringUtils.hasText(name) ? addressBookEntity.name.contains(name) : null;
	}
}
