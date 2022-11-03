package com.enfint.deal.repository;

import com.enfint.deal.model.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRepository  extends JpaRepository<Credit,Long> {
}
