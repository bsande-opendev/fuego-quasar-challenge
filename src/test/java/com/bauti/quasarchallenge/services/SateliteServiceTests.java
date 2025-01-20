package com.bauti.quasarchallenge.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.bauti.quasarchallenge.entities.Satelite;

@SpringBootTest
class SateliteServiceTest {

    @InjectMocks
    private SateliteService sateliteService;

    @Mock
    private Satelite satelite1;

    @Mock
    private Satelite satelite2;

    @Mock
    private Satelite satelite3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void decodificarMensaje() {
        when(satelite1.getLongitudMensaje()).thenReturn(3);
        when(satelite2.getLongitudMensaje()).thenReturn(3);
        when(satelite3.getLongitudMensaje()).thenReturn(3);

        when(satelite1.getMensaje()).thenReturn(new String[] { "este", "", "mensaje" });
        when(satelite2.getMensaje()).thenReturn(new String[] { "", "es", "" });
        when(satelite3.getMensaje()).thenReturn(new String[] { "este", "es", "mensaje" });

        List<Satelite> flota = Arrays.asList(satelite1, satelite2, satelite3);

        String resultado = sateliteService.decodificarMensaje(flota);

        assertEquals("este es mensaje", resultado);
        verify(satelite1).arreglarDesfasaje(3);
        verify(satelite2).arreglarDesfasaje(3);
        verify(satelite3).arreglarDesfasaje(3);
    }

    @Test
    void errorDecodificarMensajeNoSatelites() {
        List<Satelite> flota = Arrays.asList();

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> sateliteService.decodificarMensaje(flota));

        assertEquals("No hay satelites", exception.getMessage());
    }

    @Test
    void encontrarCoordsTresSatelites() {
        when(satelite1.getInterseccionCon(satelite2)).thenReturn(Arrays.asList(new Double[] { 10.0, 20.0 }, new Double[] { 15.0, 25.0 }));
        when(satelite1.getInterseccionCon(satelite3)).thenReturn(Arrays.asList(new Double[] { 10.0, 20.0 }, new Double[] { 15.0, 25.0 }));
        when(satelite2.getInterseccionCon(satelite3)).thenReturn(Arrays.asList(new Double[] { 10.0, 20.0 }, new Double[] { 15.0, 25.0 }));

        List<Satelite> flota = Arrays.asList(satelite1, satelite2, satelite3);

        int[] coordenadas = sateliteService.encontrarCoordenadas(flota);

        assertArrayEquals(new int[] { 15, 25 }, coordenadas);
    }

    @Test
    void encontrarCoordsDosSatelites() {
        when(satelite1.getInterseccionCon(satelite2))
            .thenReturn(Arrays.asList(new Double[] { 12.0, 18.0 }, new Double[] { 12.0, 18.0 }));
    
        List<Satelite> flota = Arrays.asList(satelite1, satelite2);
        
        int[] coordenadas = sateliteService.encontrarCoordenadas(flota);
    
        assertArrayEquals(new int[] { 12, 18 }, coordenadas);
    }    

    @Test
    void testEncontrarCoordenadas_NotEnoughSatelites() {
        List<Satelite> flota = Arrays.asList(satelite1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> sateliteService.encontrarCoordenadas(flota));

        assertEquals("No hay suficientes satelites activos para determinar la posicion", exception.getMessage());
    }

}
