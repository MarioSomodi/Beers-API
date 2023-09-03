package com.msomodi.beersapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msomodi.beersapi.BeerApiException;
import com.msomodi.beersapi.entity.Beer;
import com.msomodi.beersapi.repository.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BeerService {
    @Autowired
    private BeerRepository repository;

    public static final String INVALID_DATA = "Invalid beer data";

    private final ObjectMapper objectMapper = new ObjectMapper();
    public Beer saveBeer(Beer beer) {
        //Validate input, so we do not get buffer overflow if user tried to send enormous amounts of data
        if (!isValidBeer(beer)) {
            throw new BeerApiException(INVALID_DATA);
        }
        try {
            //Read beer object as json and back as class and escape the invalid characters solves Cross Site Scripting Weakness (Persistent in JSON Response)
            //ex. will not allow <script>alert(1);</script> because it will escape the "<>" characters
            String json = objectMapper.writeValueAsString(beer);
            json = cleanJsonFromHtml(json);
            Beer cleanedBeer = objectMapper.readValue(json, Beer.class);
            return repository.save(cleanedBeer);
        } catch (JsonProcessingException e) {
            throw new BeerApiException("Error serializing Beer object to JSON");
        }
    }



    public List<Beer> saveBeers(List<Beer> beers) {
        try {
            for (int i = 0; i < beers.size(); i++) {
                //Validate input, so we do not get buffer overflow if user tried to send enormous amounts of data
                if (!isValidBeer(beers.get(i))) {
                    throw new BeerApiException(INVALID_DATA);
                }
                //Read beer object as json and back as class and escape the invalid characters solves Cross Site Scripting Weakness (Persistent in JSON Response)
                //ex. will not allow <script>alert(1);</script> because it will escape the "<>" characters
                String json = objectMapper.writeValueAsString(beers.get(i));
                json = cleanJsonFromHtml(json);
                Beer cleanedBeer = objectMapper.readValue(json, Beer.class);
                beers.set(i, cleanedBeer);
            }
            return repository.saveAll(beers);
        } catch (JsonProcessingException e) {
            throw new BeerApiException("Error serializing Beer objects to JSON");
        }
    }

    public List<Beer> getBeers() {
        return repository.findAll();
    }

    public Optional<Beer> getBeerById(int id) {
        return repository.findById(id);
    }

    public String deleteBeer(int id) {
        Optional<Beer> beerToDelete = repository.findById(id);
        if (beerToDelete.isPresent()) {
            repository.deleteById(id);
            return "Beer removed: " + beerToDelete.get().getName();
        } else {
            return "Beer not found for ID: " + id;
        }
    }

    public Beer updateBeer(Beer beer) {
        //Validate input, so we do not get buffer overflow if user tried to send enormous amounts of data
        if (!isValidBeer(beer)) {
            throw new BeerApiException(INVALID_DATA);
        }
        try {
            //Check if beer exists
            Optional<Beer> existingBeerOptional = repository.findById(beer.getId());
            if (existingBeerOptional.isPresent()) {
                //Read beer object as json and back as class and escape the invalid characters solves Cross Site Scripting Weakness (Persistent in JSON Response)
                //ex. will not allow <script>alert(1);</script> because it will escape the "<>" characters
                String json = objectMapper.writeValueAsString(beer);
                json = cleanJsonFromHtml(json);
                Beer cleanedBeer = objectMapper.readValue(json, Beer.class);

                Beer existingBeer = existingBeerOptional.get();
                existingBeer.setName(cleanedBeer.getName());
                existingBeer.setDescription(cleanedBeer.getDescription());
                existingBeer.setAlcoholPercentage(cleanedBeer.getAlcoholPercentage());
                return repository.save(existingBeer);
            }else {
                throw new BeerApiException("Beer not found for ID: " + beer.getId());
            }
        } catch (JsonProcessingException e) {
            throw new BeerApiException("Error serializing Beer object to JSON");
        }
    }

    public static String cleanJsonFromHtml(String input) {
        // Replace < with &lt;
        input = input.replaceAll("<", "&lt;");

        // Replace > with &gt;
        input = input.replaceAll(">", "&gt;");

        return input;
    }

    private boolean isValidBeer(Beer beer) {
        //Validate content and its length
        return beer != null && beer.getName() != null && !beer.getName().isEmpty() && !(beer.getName().length() > 150) &&
                beer.getDescription() != null && !(beer.getDescription().length() > 1000) && beer.getAlcoholPercentage() >= 0;
    }
}
