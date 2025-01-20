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
        Satelite satelite = Satelite.builder().mensaje(new String[]{"a", "b", "c"}).build();
        assertEquals(3, satelite.getLongitudMensaje());
    }

    @Test
    void arreglaDesfasaje() {
        Satelite satelite = Satelite.builder().mensaje(new String[]{"", "", "el", "mensaje"}).build();
        satelite.arreglarDesfasaje(2);
        assertArrayEquals(new String[]{"el", "mensaje"}, satelite.getMensaje());
    }

    @Test
    void errorArreglarDesfasajeNullMensaje() {
        Satelite satelite = Satelite.builder().build();
        assertThrows(IllegalArgumentException.class, () -> satelite.arreglarDesfasaje(2));
    }

    @Test
    void errorCirculosContenidos() {
        Satelite sat1 = Satelite.builder().coordenadas(new int[]{0, 0}).distancia(5).build();
        Satelite sat2 = Satelite.builder().coordenadas(new int[]{0, 0}).distancia(2).build();

        assertThrows(IllegalArgumentException.class, () -> sat1.getInterseccionCon(sat2));
    }
    @Test
    void testRecibirMensaje() {
        Satelite satelite = Satelite.builder().build();
        String[] mensaje = {"este", "", "", "mensaje"};
        satelite.recibirMensaje(100, mensaje);

        assertEquals(100, satelite.getDistancia());
        assertArrayEquals(mensaje, satelite.getMensaje());
    }

    @Test
    void devuelveAmbosPuntosCuandoHayDosIntersecciones() {

        Satelite keno = Satelite.builder().nombre("kenobi").coordenadas(new int[] {0,0}).build();
        Satelite sato = Satelite.builder().nombre("sato").coordenadas(new int[] {1,1}).build();

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
        Satelite keno = Satelite.builder().nombre("kenobi").coordenadas(new int[] {0,0}).build();
        Satelite sato = Satelite.builder().nombre("sato").coordenadas(new int[] {2,1}).build();

        keno.recibirMensaje(1, new String[] {""});
        sato.recibirMensaje(1, new String[] {""});

        assertThrows(IllegalArgumentException.class, () -> keno.getInterseccionCon(sato));
    }

}
