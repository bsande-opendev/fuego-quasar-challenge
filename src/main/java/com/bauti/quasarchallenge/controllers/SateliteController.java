package com.bauti.quasarchallenge.controllers;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bauti.quasarchallenge.dtos.PositionDto;
import com.bauti.quasarchallenge.dtos.SatelliteRequest;
import com.bauti.quasarchallenge.dtos.SatelliteResponse;
import com.bauti.quasarchallenge.services.SateliteService;

@Controller
public class SateliteController {
    
    @Autowired
    SateliteService satService;

    @PostMapping("/topsecret")
    public ResponseEntity receiveSatellites(@RequestBody SatelliteRequest satelliteRequest) {
        try {
            SatelliteResponse response = new SatelliteResponse();
            response.setMessage(satService.decodificarMensaje(satelliteRequest.getSatellites()));
            int[] coords = satService.encontrarCoordenadas(satelliteRequest.getSatellites());
            PositionDto positionDto = PositionDto.builder().x(coords[0]).y(coords[1]).build();
            response.setPosition(positionDto);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
