package com.msomodi.beersapi.repository;

import com.msomodi.beersapi.entity.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerRepository extends JpaRepository<Beer, Integer> {
}

