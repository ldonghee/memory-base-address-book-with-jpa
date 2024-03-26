package com.hyundai.test.address.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hyundai.test.address.domain.AddressBookEntity;

public interface AddressBookJpaRepository extends JpaRepository<AddressBookEntity, Long>, QueryDslAddressBookRepository {
	void deleteByPhoneNumber(String phoneNumber);

	Optional<AddressBookEntity> findByPhoneNumber(String phoneNumber);

	List<AddressBookEntity> findByNameContaining(String name);
}
