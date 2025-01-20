package com.bauti.quasarchallenge.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import com.bauti.quasarchallenge.dtos.DecodificacionResponse;
import com.bauti.quasarchallenge.dtos.MensajeRequest;
import com.bauti.quasarchallenge.dtos.PositionDto;
import com.bauti.quasarchallenge.entities.Mensaje;
import com.bauti.quasarchallenge.entities.MensajeSeries;
import com.bauti.quasarchallenge.entities.Satelite;
import com.bauti.quasarchallenge.repositories.MensajeRepository;
import com.bauti.quasarchallenge.repositories.SateliteRepository;

@SpringBootTest
class MensajeServiceTest {

    @Mock
    private SateliteRepository satRepo;

    @Mock
    private SateliteService satService;

    @Mock
    private MensajeRepository msjRepo;

    @Mock
    private MensajeSeriesService msjSerieService;

    @InjectMocks
    private MensajeService mensajeService;

    private Satelite satelite;
    private MensajeRequest mensajeRequest;
    private MensajeSeries mensajeSeries;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        satelite = new Satelite();
        satelite.setName("Kenobi");

        mensajeRequest = new MensajeRequest(150, List.of("mensaje", "de", "prueba"));

        mensajeSeries = new MensajeSeries();
        mensajeSeries.setCompleted(false);
    }

    @Test
    void errorSateliteNoEncontrado() {
        when(satRepo.findByNameIgnoreCase("Kenobi")).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            mensajeService.saveMensaje("Kenobi", mensajeRequest);
        });

        assertEquals("No existe un satelite con ese nombre", exception.getMessage());
    }

    @Test
    void errorSateliteYaTieneMensajePendiente() {
        when(satRepo.findByNameIgnoreCase("Kenobi")).thenReturn(Optional.of(satelite));
        when(msjSerieService.getLastSerie()).thenReturn(mensajeSeries);

        Mensaje existingMensaje = new Mensaje();
        existingMensaje.setSatelite(satelite);
        mensajeSeries.setMensajes(List.of(existingMensaje));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mensajeService.saveMensaje("Kenobi", mensajeRequest);
        });

        assertEquals("Ya existe un mensaje para este satelite y aún no fue decodificado", exception.getMessage());
    }

    @Test
    void testGuardarMensajeCorrecto() {
        when(satRepo.findByNameIgnoreCase("Kenobi")).thenReturn(Optional.of(satelite));
        when(msjSerieService.getLastSerie()).thenReturn(mensajeSeries);
        when(msjRepo.saveAndFlush(any(Mensaje.class))).thenReturn(new Mensaje());

        mensajeService.saveMensaje("Kenobi", mensajeRequest);

        verify(msjRepo, times(1)).saveAndFlush(any(Mensaje.class));
    }

    @Test
    void testDecodificarMensajeCorrecto() {

        when(msjSerieService.getLastSerie()).thenReturn(mensajeSeries);
        when(satService.encontrarCoordenadas(anyList())).thenReturn(new int[]{10, 20});
        when(satService.decodificarMensaje(anyList())).thenReturn("Mensaje Decodificado");

        Mensaje mensaje1 = new Mensaje();
        mensaje1.setSatelite(satelite);
        mensaje1.setMsg(List.of("Mensaje", ""));
        Mensaje mensaje2 = new Mensaje();
        mensaje2.setSatelite(satelite);
        mensaje2.setMsg(List.of("", "Decodificado"));
        mensajeSeries.setMensajes(List.of(mensaje1, mensaje2));

        DecodificacionResponse response = mensajeService.getDecodificacion();

        assertNotNull(response);
        assertEquals(10, response.getPosition().getX());
        assertEquals(20, response.getPosition().getY());
        assertEquals("Mensaje Decodificado", response.getMessage());

        verify(msjSerieService, times(1)).saveOrUpdate(any(MensajeSeries.class));
    }
}
