package com.bauti.quasarchallenge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bauti.quasarchallenge.entities.MensajeSeries;
import com.bauti.quasarchallenge.repositories.MensajeSeriesRepository;

@Service
public class MensajeSeriesService {

    @Autowired
    MensajeSeriesRepository msjSerieRepo;
    
    public void saveOrUpdate(MensajeSeries mensajeSeries){
        msjSerieRepo.saveAndFlush(mensajeSeries);
    }

    public Long getLastSerieId(){
        return msjSerieRepo.findFirstByOrderByIdDesc().getId();
    }

    public MensajeSeries getLastSerie(){
        return msjSerieRepo.findFirstByOrderByIdDesc();
    }


}
