package com.example.fxdeal.repository;

import com.example.fxdeal.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DealRepository extends JpaRepository<Deal, String> {

    boolean existsByDealId(String dealId);

    Optional<Deal> findByDealId(String dealId);
}
