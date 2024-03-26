package com.hyundai.test.address.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.hyundai.test.address.repository.QueryDslAddressBookRepository;
import com.hyundai.test.address.repository.QueryDslAddressBookRepositoryImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@TestConfiguration
public class TestQueryDslConfig {
	@PersistenceContext
	private EntityManager entityManager;

	@Bean
	public JPAQueryFactory jpaQueryFactory() {
		return new JPAQueryFactory(entityManager);
	}

	@Bean
	public QueryDslAddressBookRepository adminRepository() {
		return new QueryDslAddressBookRepositoryImpl(jpaQueryFactory());
	}
}
