package model.produccion;

import lombok.*;
import model.enums.NivelAutomatizacion;
import model.oscuro.TrabajadorEsclavizado;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Fabrica implements Serializable {
    private long id;
    private String pais;
    private String ciudad;
    private long capacidad;
    private NivelAutomatizacion nivelAutomatizacion;
    private List<TrabajadorEsclavizado> trabajadores;

    public void asignarTrabajador(TrabajadorEsclavizado trabajador) {
        if (trabajador != null && !trabajadores.contains(trabajador)) {
            trabajadores.add(trabajador);
            trabajador.setAsignadoA(this);
        }
    }
}
