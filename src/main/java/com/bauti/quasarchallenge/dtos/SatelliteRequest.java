package com.bauti.quasarchallenge.dtos;

import java.util.List;

import com.bauti.quasarchallenge.entities.Satelite;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class SatelliteRequest {
    List<Satelite> satellites;
}
