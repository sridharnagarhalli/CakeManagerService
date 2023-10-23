package com.wracle.cakemanager.service;

import com.wracle.cakemanager.entity.Cake;
import com.wracle.cakemanager.repository.CakeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CakeMgrService {
    private final CakeRepository cakeRepository;

    public List<Cake> getAllCakes() {
        return cakeRepository.findAll();
    }

    public Cake getCakeById(final Long id) {
        return cakeRepository.findById(id).orElse(null);
    }

    public Cake saveCake(final Cake cake) {
        return cakeRepository.save(cake);
    }

    public Cake updateCake(final Long id, final Cake updatedCake) {
        if (cakeRepository.existsById(id)) {
            updatedCake.setId(id);
            return cakeRepository.save(updatedCake);
        }
        return null;
    }

    public void deleteCake(final Long id) {
        cakeRepository.deleteById(id);
    }
}
