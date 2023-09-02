package com.msomodi.beersapi.controller;

import com.msomodi.beersapi.entity.Beer;
import com.msomodi.beersapi.service.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BeerController {
    @Autowired
    private BeerService service;
    @Autowired
    private JmsTemplate jmsTemplate;
    @PostMapping("/addBeer")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Beer> addBeer(@RequestBody Beer beer){
        jmsTemplate.convertAndSend("Saving new beer to database: " + beer.getName());
        try {
            return new ResponseEntity<>(service.saveBeer(beer), HttpStatus.OK);
        } catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/addBeers")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Beer>> addBeers(@RequestBody List<Beer> beers){
        jmsTemplate.convertAndSend("Saving new beers to database");
        try {
            return new ResponseEntity<>(service.saveBeers(beers), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/beers")
    @ResponseStatus(HttpStatus.OK)
    public List<Beer> findAllBeers(){
        jmsTemplate.convertAndSend("Fetching all beers from database");
        return service.getBeers();
    }
    @GetMapping("/beer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Beer> findBeerById(@PathVariable int id){
        jmsTemplate.convertAndSend("Fetching beer with id " + id + " from database");
        Optional<Beer> updatedBeer = service.getBeerById(id);
        if(updatedBeer.isPresent()){
            return new ResponseEntity<>(updatedBeer.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/updateBeer")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Beer> updateBeer(@RequestBody Beer beer){
        jmsTemplate.convertAndSend("Updating beer with id " + beer.getId());
        try {
            return new ResponseEntity<>(service.updateBeer(beer),HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteBeer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteBeer(@PathVariable int id){
        jmsTemplate.convertAndSend("Deleting beer with id " + id);
        return service.deleteBeer(id);
    }
}
