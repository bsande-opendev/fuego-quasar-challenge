package com.bauti.quasarchallenge.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MensajeRequest {
    
    int distance;
    @NonNull
    List<String> message;
}
