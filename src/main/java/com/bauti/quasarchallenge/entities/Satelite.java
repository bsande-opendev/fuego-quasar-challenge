package com.bauti.quasarchallenge.entities;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor 
@Entity
public class Satelite {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Transient
    private int distance;

    @Transient
    private String[] message;
    
    private List<Integer> position;

    @OneToMany(mappedBy = "satelite", cascade = CascadeType.ALL)
    private List<Mensaje> mensajes;

    public Satelite(String name, List<Integer> position) {
        this.name = name;
        this.position = position;
    }

    public Satelite(String name) {
        this.name = name;
    }

    public void recibirMensaje(int distancia, String[] mensaje){
        this.distance = distancia;
        this.message = mensaje;
    }

    public int getLongitudMensaje(){
        return this.getMessage().length;
    }

    public void arreglarDesfasaje(int longRealMsg){

        if (this.message != null && longRealMsg >= 0 && longRealMsg <= this.getLongitudMensaje()) {
            this.message = Arrays.copyOfRange(this.message, getLongitudMensaje() - longRealMsg, getLongitudMensaje());
        } else {
            throw new IllegalArgumentException("El mensaje es null o la longitud real es invalida");
        }

    }

    public List<Double[]> getInterseccionCon(Satelite sat){
        return this.calcularInterseccion(this.position, this.distance, sat.getPosition(), sat.getDistance());
    }

    private List<Double[]> calcularInterseccion(List<Integer> p0, int r0, List<Integer> p1, int r1){
        MathContext mc = new MathContext(1000, RoundingMode.HALF_UP);
        
        int x0 = p0.get(0);
        int y0 = p0.get(1);
        int x1 = p1.get(0);
        int y1 = p1.get(1);

        int dx = x1 - x0;
        int dy = y1 - y0;

        //Si la distancia es menor a la diferencia de radios entoneces hay uno adentro del otro
        if(Math.hypot(dx, dy) < Math.abs(r0 - r1)){
            throw new IllegalArgumentException("No se puede calcular la interseccion, un circulo contiene totalmente al otro");
        }

        //Si la distancia entre los centros es mayor a la suma de los dos radios, no se pueden intersectar nunca
        if(Math.hypot(dx, dy) > (r0 + r1)){
            throw new IllegalArgumentException("No se puede calcular la interseccion, los radios no se interseccionan");
        }

        //Si no hay distancia entre puntos, y coinciden los radios, son el mismo circulo
        if(Math.hypot(dx, dy) == 0 && r1 == r0){
            throw new IllegalArgumentException("No se puede calcular la interseccion, los circulos son iguales");
        }

        BigDecimal d = hipotenusa(BigDecimal.valueOf(dx), BigDecimal.valueOf(dy));

        //Distancia al punto que esta en la interseccion de la linea entre los centros de los circulos 
        // y la linea entre los puntos de interseccion de los circulos
        BigDecimal dxBD = BigDecimal.valueOf(dx);
        BigDecimal dyBD = BigDecimal.valueOf(dy);
        BigDecimal x0BD = BigDecimal.valueOf(x0);
        BigDecimal y0BD = BigDecimal.valueOf(y0);

        BigDecimal r0b = BigDecimal.valueOf(r0);
        BigDecimal r1b = BigDecimal.valueOf(r1);

        BigDecimal r0r0 = r0b.multiply(r0b, mc);
        BigDecimal r1r1 = r1b.multiply(r1b, mc);
        BigDecimal dd = d.multiply(d, mc);

        BigDecimal arriba = r0r0.subtract(r1r1, mc).add(dd, mc);
        BigDecimal abajo = d.multiply(BigDecimal.valueOf(2L), mc);

        BigDecimal a = arriba.divide(abajo, mc);


        BigDecimal dxasobred = dxBD.multiply(a, mc).divide(d, mc);
        BigDecimal dyasobred = dyBD.multiply(a, mc).divide(d, mc);

        BigDecimal x2 = x0BD.add(dxasobred, mc);
        BigDecimal y2 = y0BD.add(dyasobred, mc);

        //Distancia del punto T a cualquiera de los dos puntos de interseccion de los circulos (G)
        BigDecimal aa = a.multiply(a, mc);
        BigDecimal r0r0menosaa = r0r0.subtract(aa, mc);

        BigDecimal h = r0r0menosaa.sqrt(mc);

        BigDecimal hsobred = h.divide(d, mc);
        BigDecimal rx = dyBD.negate().multiply(hsobred, mc);
        BigDecimal ry = dxBD.multiply(hsobred, mc);

        //Coordenadas finales de los puntos de interseccion
        Double xi = getDosDecimales(x2.doubleValue() + rx.doubleValue());
        Double xiprime = getDosDecimales(x2.doubleValue() - rx.doubleValue());
        Double yi = getDosDecimales(y2.doubleValue() + ry.doubleValue());
        Double yiprime = getDosDecimales(y2.doubleValue() - ry.doubleValue());
        
        return List.of(new Double[]{xi, yi}, new Double[]{xiprime, yiprime});
    }

    private BigDecimal hipotenusa(BigDecimal a, BigDecimal b){

        BigDecimal a2 = a.multiply(a);
        BigDecimal b2 = b.multiply(b);
        BigDecimal ab = a2.add(b2);

        BigDecimal h = ab.sqrt(new MathContext(1000, RoundingMode.HALF_UP));

        return h;
    }

    private Double getDosDecimales(Double num) {
        return Math.round(num * 100.0) / 100.0;
    }

    public Satelite(String string, double d, String[] strings) {
        //TODO Auto-generated constructor stub
    }
    
}
