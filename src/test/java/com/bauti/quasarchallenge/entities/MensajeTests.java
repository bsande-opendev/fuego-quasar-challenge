package com.bauti.quasarchallenge.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MensajeTests {

    Mensaje mensaje = new Mensaje();

    @BeforeEach
    void setUp() {
        mensaje.setId(1L);
        mensaje.setMsg(Arrays.asList("Este", "es", "un", "mensaje", "de", "prueba"));
        mensaje.setDistance(100);
        mensaje.setMensajeSerie(new MensajeSeries());
        mensaje.setSatelite(new Satelite());
    }

    @Test
    void longitudMensajeCorrecta() {
        int longitud = mensaje.getLongitudMensaje();
        
        assertEquals(6, longitud);
    }

    @Test
    void arreglarDesfasajeCorrecto() {
        mensaje.arreglarDesfasaje(3);
        List<String> expectedMsg = Arrays.asList("mensaje", "de", "prueba");
        assertEquals(expectedMsg, mensaje.getMsg());
    }

    @Test
    void arreglarDesfasajeLargoInvalido() {
        assertThrows(IllegalArgumentException.class, () -> mensaje.arreglarDesfasaje(10));
    }

    @Test
    void arreglarDesfasajeNull() {
        mensaje.setMsg(null);
        assertThrows(IllegalArgumentException.class, () -> mensaje.arreglarDesfasaje(3));
    }

}
