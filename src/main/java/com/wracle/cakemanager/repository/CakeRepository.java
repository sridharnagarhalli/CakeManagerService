package com.wracle.cakemanager.repository;

import com.wracle.cakemanager.entity.Cake;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CakeRepository extends JpaRepository<Cake, Long> {

}
