package com.bauti.quasarchallenge.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bauti.quasarchallenge.dtos.DecodificacionResponse;
import com.bauti.quasarchallenge.dtos.MensajeRequest;
import com.bauti.quasarchallenge.dtos.PositionDto;
import com.bauti.quasarchallenge.entities.Mensaje;
import com.bauti.quasarchallenge.entities.MensajeSeries;
import com.bauti.quasarchallenge.entities.Satelite;
import com.bauti.quasarchallenge.repositories.MensajeRepository;
import com.bauti.quasarchallenge.repositories.SateliteRepository;

@Service
public class MensajeService {

    @Autowired
    SateliteRepository satRepo;

    @Autowired
    SateliteService satService;

    @Autowired
    MensajeRepository msjRepo;

    @Autowired
    MensajeSeriesService msjSerieService;

    public void saveMensaje(String satName, MensajeRequest body) {
        Satelite sat = satRepo.findByNameIgnoreCase(satName)
                .orElseThrow(() -> new NoSuchElementException("No existe un satelite con ese nombre"));

        // Reviso que si la serie no esta completa, no tenga ya un mensaje de este
        // satelite
        MensajeSeries lastSerie = msjSerieService.getLastSerie();
        if (lastSerie.isCompleted()){
            lastSerie = new MensajeSeries();
        }

        if (!lastSerie.isCompleted() && lastSerie.getMensajes() != null && lastSerie.getMensajes().stream()
                .anyMatch(msjSerie -> msjSerie.getSatelite().getName().equalsIgnoreCase(satName))) {
            throw new IllegalArgumentException("Ya existe un mensaje para este satelite y aún no fue decodificado");
        }

        Mensaje msj = new Mensaje();
        msj.setDistance(body.getDistance());
        msj.setMsg(body.getMessage());
        // Si la serie ya esta completa, inicio una nueva
        msj.setMensajeSerie(lastSerie);
        msj.setSatelite(sat);

        msjRepo.saveAndFlush(msj);
    }

    public DecodificacionResponse getDecodificacion() {
        List<Satelite> flota = new ArrayList<>();
        MensajeSeries latestSerie = msjSerieService.getLastSerie();
        DecodificacionResponse response = new DecodificacionResponse();

        // Cargo la distancia a cada satelite
        latestSerie.getMensajes().forEach(
                msj -> {
                    Satelite sat = msj.getSatelite();
                    sat.recibirMensaje(msj.getDistance(), msj.getMsg().toArray(new String[0]));
                    flota.add(msj.getSatelite());
                });
        int[] coords = satService.encontrarCoordenadas(flota);
        PositionDto positionDto = PositionDto.builder().x(coords[0]).y(coords[1]).build();
        response.setPosition(positionDto);
        response.setMessage(satService.decodificarMensaje(flota));

        // Si no ocurrio ningun error, el mensaje fue decodificado y marco la serie como
        // completada y actualizo db
        latestSerie.setCompleted(true);
        msjSerieService.saveOrUpdate(latestSerie);

        return response;
    }
}
