package com.enfint.deal.repository;

import com.enfint.deal.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application,Long> {


    @Override
    Optional<Application> findById(Long applicationId);
}
