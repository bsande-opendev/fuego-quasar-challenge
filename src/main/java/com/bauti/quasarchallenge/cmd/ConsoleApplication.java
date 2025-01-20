package com.bauti.quasarchallenge.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bauti.quasarchallenge.entities.Satelite;
import com.bauti.quasarchallenge.services.SateliteService;

@SpringBootApplication
public class ConsoleApplication implements CommandLineRunner{

    static final int[] KENOBI_COORDS = {-500, -200};
    static final int[] SKYWALKER_COORDS = {100, -100};
    static final int[] SATO_COORDS = {500, 100};

    @Autowired
    private SateliteService satService;


    @Override
    public void run(String... args) throws Exception {

        String[][] msg = {{"", "este", "", "un", ""}, {"este", "", "un", "mensaje"}, {"", "", "es", "", "mensaje"}};
        System.out.println("\n\n");
        System.out.println("Mensaje recibido: ");
        System.out.println(Arrays.deepToString(msg));
        System.out.println("\n\n");
        System.out.println("Decodificando...");
        System.out.println("Mensaje decodificado: " + getMessage(msg));

        int[] distancias = {300, 633, 1000};
        System.out.println("Distancias recibidas: ");
        System.out.println(Arrays.toString(distancias));
        System.out.println("Triangulando...");
        System.out.println("Posición: " + Arrays.toString(getLocation(distancias[0], distancias[1], distancias[2])));
    }
    
    private int[] getLocation(int distKenobi, int distSkywalker, int distSato){

        String[] emptymsg = {""};

        Satelite kenobi = Satelite.builder().nombre("kenobi").coordenadas(KENOBI_COORDS).build();
        kenobi.recibirMensaje(distKenobi, emptymsg);

        Satelite skywalker = Satelite.builder().nombre("skywalker").coordenadas(SKYWALKER_COORDS).build();
        skywalker.recibirMensaje(distSkywalker, emptymsg);

        Satelite sato = Satelite.builder().nombre("sato").coordenadas(SATO_COORDS).build();
        sato.recibirMensaje(distSato, emptymsg);

        List<Satelite> flotaSatelital = new ArrayList<>();
        flotaSatelital.add(kenobi);
        flotaSatelital.add(skywalker);
        flotaSatelital.add(sato);

        return satService.encontrarCoordenadas(flotaSatelital);
    }

    private String getMessage(String[][] mensajes){

        Satelite kenobi = Satelite.builder().nombre("kenobi").coordenadas(KENOBI_COORDS).build();
        kenobi.recibirMensaje(0, mensajes[0]);

        Satelite skywalker = Satelite.builder().nombre("skywalker").coordenadas(SKYWALKER_COORDS).build();
        skywalker.recibirMensaje(0, mensajes[1]);

        Satelite sato = Satelite.builder().nombre("sato").coordenadas(SATO_COORDS).build();
        sato.recibirMensaje(0, mensajes[2]);

        List<Satelite> flotaSatelital = new ArrayList<>();
        flotaSatelital.add(kenobi);
        flotaSatelital.add(skywalker);
        flotaSatelital.add(sato);

        return satService.decodificarMensaje(flotaSatelital);
      
    }
}
