package com.wracle.cakemanager.controller;

import com.wracle.cakemanager.exception.CakeNotFoundException;
import com.wracle.cakemanager.exception.InvalidRequestException;
import com.wracle.cakemanager.entity.Cake;
import com.wracle.cakemanager.service.CakeMgrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(CakeMgrController.BASE_URL_CAKES)
public class CakeMgrController {

    public static final String BASE_URL_CAKES = "/api/cakes";
    private final CakeMgrService cakeMgrService;

    @Autowired
    public CakeMgrController(final CakeMgrService cakeMgrService) {
        this.cakeMgrService = cakeMgrService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Cake> getAllCakes() {
        log.info("Getting all cakes.");
        return this.cakeMgrService.getAllCakes();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Cake getCakeById(@PathVariable final Long id) {
        log.info("Getting cakes {}", id);
        return Optional.ofNullable(cakeMgrService.getCakeById(id))
                .orElseThrow(() -> new CakeNotFoundException("Cake with ID " + id + " not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cake saveCake(@RequestBody final Cake cake) {
        log.info("Saving cakes {}", cake);
        if (cake.getId() != null) {
            throw new InvalidRequestException("New cakes cannot have an ID.");
        }
        return cakeMgrService.saveCake(cake);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Cake updateCake(@PathVariable final Long id, @RequestBody final Cake updatedCake) {
        log.info("Updating cake {} with id {}", updatedCake, id);
        return cakeMgrService.updateCake(id, updatedCake);
    }

    @DeleteMapping("/{id}")
    public void deleteCake(@PathVariable final Long id) {
        log.info("Deleting cake with id {}", id);
        this.cakeMgrService.deleteCake(id);
    }
}
