package com.bauti.quasarchallenge.entities;

import java.util.Arrays;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    List<String> msg;

    int distance;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "serie_id")
    MensajeSeries mensajeSerie;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "satelite_id")
    Satelite satelite;

    public void arreglarDesfasaje(int longRealMsg) {

        if (this.msg != null && longRealMsg >= 0 && longRealMsg <= this.getLongitudMensaje()) {
            this.msg = this.msg.subList(getLongitudMensaje() - longRealMsg, getLongitudMensaje());
        } else {
            throw new IllegalArgumentException("El mensaje es null o la longitud real es invalida");
        }

    }
    public int getLongitudMensaje(){
        return this.getMsg().size();
    }
}
