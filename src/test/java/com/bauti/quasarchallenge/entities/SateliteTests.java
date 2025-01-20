package com.bauti.quasarchallenge.entities;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SateliteTests {
    
    @Test
    void devuelveLongitudMensaje() {
        Satelite satelite = new Satelite();
        satelite.setMessage(new String[]{"a", "b", "c"});
        assertEquals(3, satelite.getLongitudMensaje());
    }

    @Test
    void arreglaDesfasaje() {
        Satelite satelite = new Satelite();
        satelite.setMessage(new String[]{"", "", "el", "mensaje"});
        satelite.arreglarDesfasaje(2);
        assertArrayEquals(new String[]{"el", "mensaje"}, satelite.getMessage());
    }

    @Test
    void errorArreglarDesfasajeNullMensaje() {
        Satelite satelite = new Satelite();
        assertThrows(IllegalArgumentException.class, () -> satelite.arreglarDesfasaje(2));
    }

    @Test
    void errorCirculosContenidos() {
        Satelite sat1 = new Satelite("kenobi");
        sat1.setDistance(5);
        sat1.setPosition(List.of(0,0));
        Satelite sat2 = new Satelite("sato");
        sat2.setDistance(2);
        sat2.setPosition(List.of(0,0));

        assertThrows(IllegalArgumentException.class, () -> sat1.getInterseccionCon(sat2));
    }
    @Test
    void testRecibirMensaje() {
        Satelite satelite = new Satelite();
        String[] mensaje = {"este", "", "", "mensaje"};
        satelite.recibirMensaje(100, mensaje);

        assertEquals(100, satelite.getDistance());
        assertArrayEquals(mensaje, satelite.getMessage());
    }

    @Test
    void devuelveAmbosPuntosCuandoHayDosIntersecciones() {

        Satelite keno = new Satelite("kenobi");
        keno.setPosition(List.of(0,0));
        Satelite sato = new Satelite("sato");
        sato.setPosition(List.of(1,1));

        keno.recibirMensaje(1, new String[] {""});
        sato.recibirMensaje(1, new String[] {""});

        List<Double[]> intersecc = keno.getInterseccionCon(sato);

        Double[] coordEsperadas1 = { 0.0, 1.0 };
        Double[] coordEsperadas2 = { 1.0, 0.0 };

        assertArrayEquals(intersecc.get(0), coordEsperadas1);
        assertArrayEquals(intersecc.get(1), coordEsperadas2);
    }

    @Test
    void errorNoInterseccion() {
        Satelite keno = new Satelite("kenobi");
        keno.setPosition(List.of(0,0));
        Satelite sato = new Satelite("sato");
        sato.setPosition(List.of(2,1));
        keno.recibirMensaje(1, new String[] {""});
        sato.recibirMensaje(1, new String[] {""});

        assertThrows(IllegalArgumentException.class, () -> keno.getInterseccionCon(sato));
    }

}
