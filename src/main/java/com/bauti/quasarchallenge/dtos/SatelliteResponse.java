package com.bauti.quasarchallenge.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SatelliteResponse {
    PositionDto position;
    String message;
}
