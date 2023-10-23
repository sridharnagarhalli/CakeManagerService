package com.wracle.cakemanager.service;

import com.wracle.cakemanager.entity.Cake;
import com.wracle.cakemanager.repository.CakeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CakeMgrServiceTest {

    @Mock
    private CakeRepository cakeRepository;

    @InjectMocks
    private CakeMgrService cakeService;

    @Test
    public void testGetAllCakes() {
        Cake cake1 = new Cake();
        cake1.setId(1L);
        cake1.setName("Chocolate Cake");
        Cake cake2 = new Cake();
        cake2.setId(2L);
        cake2.setName("Vanilla Cake");
        List<Cake> cakeList = Arrays.asList(cake1, cake2);
        when(cakeRepository.findAll()).thenReturn(cakeList);
        List<Cake> result = cakeService.getAllCakes();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetCakeById() {
        Cake cake = new Cake();
        cake.setId(1L);
        cake.setName("Chocolate Cake");
        when(cakeRepository.findById(1L)).thenReturn(Optional.of(cake));
        Cake result = cakeService.getCakeById(1L);
        assertEquals(1l, result.getId().longValue());
        assertEquals("Chocolate Cake", result.getName());
    }

    @Test
    public void testSaveCake() {
        Cake cake = new Cake();
        cake.setId(1L);
        cake.setName("Chocolate Cake");
        when(cakeRepository.save(any(Cake.class))).thenReturn(cake);
        Cake result = cakeService.saveCake(cake);
        assertEquals(1l, result.getId().longValue());
        assertEquals("Chocolate Cake", result.getName());
    }

    @Test
    public void testUpdateCake() {
        Cake existingCake = new Cake();
        existingCake.setId(1L);
        existingCake.setName("Chocolate Cake");
        when(cakeRepository.existsById(1L)).thenReturn(true);
        Cake updatedCake = new Cake();
        updatedCake.setName("Updated Chocolate Cake");
        when(cakeRepository.save(any(Cake.class))).thenReturn(updatedCake);
        Cake result = cakeService.updateCake(1L, updatedCake);
        assertEquals(1L, result.getId().longValue());
        assertEquals("Updated Chocolate Cake", result.getName());
    }

    @Test
    public void testDeleteCake() {
        cakeService.deleteCake(1L);
        Mockito.verify(cakeRepository).deleteById(1L);
    }
}

