package com.bauti.quasarchallenge.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bauti.quasarchallenge.entities.Satelite;

@Service
public class SateliteService {

    public String decodificarMensaje(List<Satelite> flotaSatelital){
        
        int longRelMsg = 
        flotaSatelital
            .stream()
            .mapToInt(Satelite::getLongitudMensaje).min()
            .orElseThrow(() -> new IllegalStateException("No hay satelites"));
            
        flotaSatelital.forEach(s -> s.arreglarDesfasaje(longRelMsg));
        
        Satelite primerSat = flotaSatelital.get(0);
        List<Satelite> otrosSatelites = flotaSatelital.subList(1, flotaSatelital.size());

        List<String> msgDecodificado = new ArrayList<>();

        for (int i = 0; i < longRelMsg; i++) {
            String palabra = primerSat.getMensaje()[i];

            if(isPalabraValida(palabra)){
                msgDecodificado.add(palabra);
            } else {
                msgDecodificado.add(buscarPalabraFaltante(otrosSatelites, i));
            }

        }
        
        return String.join(" ", msgDecodificado);
    }

    private String buscarPalabraFaltante(List<Satelite> satelites, int i){
        String palabraPerdida = "";
        for (Satelite satelite: satelites){
            String palabra = satelite.getMensaje()[i];
            if(isPalabraValida(palabra)){
                palabraPerdida = palabra;
            }
        }

        return palabraPerdida;
    }

    private boolean isPalabraValida(String palabra){
        if (!palabra.isBlank() || !palabra.isBlank()) return true;
        
        return false;
    }

    public int[] encontrarCoordenadas(List<Satelite> flotaSatelital){
        if(flotaSatelital.size() == 1) throw new IllegalArgumentException("No hay suficientes satelites activos para determinar la posicion");

        if(flotaSatelital.size() == 2) {
            Satelite sat1 = flotaSatelital.get(0);
            Satelite sat2 = flotaSatelital.get(1);
            List<Double[]> intersecciones = sat1.getInterseccionCon(sat2);

            Double[] interseccion1 = intersecciones.get(0);
            Double[] interseccion2 = intersecciones.get(1);

            if(interseccion1[0].equals(interseccion2[0]) && interseccion1[1].equals(interseccion2[1])) {
                return Arrays.stream(interseccion1).mapToInt(Double::intValue).toArray();
            }

            throw new IllegalArgumentException("No hay suficientes satelitos activos para determinar la posicion");
        }

        //Si son 3 o mas..
        List<Satelite> primerosTres = flotaSatelital.subList(0, 3);
        Satelite kenobi = primerosTres.get(0);
        Satelite skywalker = primerosTres.get(1);
        Satelite sato = primerosTres.get(2);

        List<Double[]> intKenSky = kenobi.getInterseccionCon(skywalker);
        List<Double[]> intKenSato = kenobi.getInterseccionCon(sato);
        List<Double[]> intSkySato = skywalker.getInterseccionCon(sato);

        Double[] coordenadas = null;

        for (Double[] intersecc : intKenSky) {
            boolean isInKenSato = intKenSato.stream().anyMatch(i -> sonIguales(i, intersecc));
            boolean isInSkySato = intSkySato.stream().anyMatch(i -> sonIguales(i, intersecc));

            if (isInKenSato && isInSkySato) {
                coordenadas = intersecc;
            }
        }

        if (coordenadas == null) {
            throw new IllegalArgumentException("La posicion no pudo ser determinada");
        }
        
        return Arrays.stream(coordenadas).mapToInt(Double::intValue).toArray();
    }

    private Boolean sonIguales(Double[] a, Double[] b){
        return this.compararConError(a[0], b[0])  && this.compararConError(a[1], b[1]);
    }

    private Boolean compararConError(double a, double b){
        if (Math.abs(Math.abs(a) - Math.abs(b)) <= 0.9){
            return true;
        } else {
            if (Math.abs(Math.abs(a) - Math.abs(b)) <= 5.0) {
                return true;
            } else {
                return false;
            }
        }

    }
}
