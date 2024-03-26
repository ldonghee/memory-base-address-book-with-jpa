package com.hyundai.test.address.dto;

import java.util.Objects;

import lombok.Getter;

import com.hyundai.test.address.domain.AddressBookFilterOrder;
import com.hyundai.test.address.domain.AddressBookFilterType;

@Getter
public class AddressBookSearchCondition {
	private final AddressBookFilterType filterType;
	private final String filterValue;
	private final AddressBookFilterType sortBy;
	private final AddressBookFilterOrder sortOrder;

	public AddressBookSearchCondition(String filterType, String filterValue, String sortBy, String sortOrder) {
		this.filterType = AddressBookFilterType.of(filterType);
		this.filterValue = filterValue;
		this.sortBy = AddressBookFilterType.of(sortBy);
		this.sortOrder = AddressBookFilterOrder.of(sortOrder);
	}

	public AddressBookSearchCondition(String filterType, String filterValue) {
		this.filterType = AddressBookFilterType.of(filterType);
		this.filterValue = filterValue;
		this.sortBy = AddressBookFilterType.ADDRESS;
		this.sortOrder = AddressBookFilterOrder.ASC;
	}

	public String getSortOrder() {
		return sortOrder.getOrder();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AddressBookSearchCondition condition = (AddressBookSearchCondition) o;
		return filterType == condition.filterType && Objects.equals(filterValue, condition.filterValue) && sortBy == condition.sortBy
			   && sortOrder == condition.sortOrder;
	}

	@Override
	public int hashCode() {
		return Objects.hash(filterType, filterValue, sortBy, sortOrder);
	}
}
