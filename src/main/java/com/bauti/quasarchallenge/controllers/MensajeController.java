package com.bauti.quasarchallenge.controllers;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bauti.quasarchallenge.dtos.MensajeRequest;
import com.bauti.quasarchallenge.services.MensajeService;

@RestController
public class MensajeController {

    @Autowired
    MensajeService msjService;

    @PostMapping("/topsecret_split/{satelite_name}")
    public ResponseEntity receiveMessage(@PathVariable String satelite_name, @RequestBody MensajeRequest body){
        try {
            msjService.saveMensaje(satelite_name, body);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (NoSuchElementException | IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }  catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/topsecret_split")
    public ResponseEntity getResults(){
        try {
            return new ResponseEntity<>(msjService.getDecodificacion(), HttpStatus.OK);
        } catch (NoSuchElementException | IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
