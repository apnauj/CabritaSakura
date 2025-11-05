package model.oscuro;

import lombok.*;
import model.enums.NivelSalud;
import model.produccion.Fabrica;

import java.io.Serializable;

@NoArgsConstructor
@Data

public class TrabajadorEsclavizado implements Serializable {
    private static Long nextId= 1L;
    private Long  id;
    private String nombre;
    private String paisOrigen;
    private int edad;
    private String fechaCaptura;
    private NivelSalud salud;
    private Fabrica asignadoA;

    public TrabajadorEsclavizado(String nombre, String paisOrigen, int edad,
                                 String fechaCaptura, NivelSalud salud, Fabrica asignadoA) {
        this.id = nextId++;
        this.nombre = nombre;
        this.paisOrigen = paisOrigen;
        this.edad = edad;
        this.fechaCaptura = fechaCaptura;
        this.salud = salud;
        this.asignadoA = asignadoA;

        //Si hay fabrica asginada añadirlo a esta sino dejarlo null
        if (asignadoA != null) {
            asignadoA.asignarTrabajador(this);
        }

        //Llamar al registro de esclavos (que es único)
        RegistroEsclavos registro = RegistroEsclavos.getInstancia();
        //Añadir el trabajador al registro de esclavos
        registro.agregarTrabajador(this);

    }

    public void reasignarFabrica(Fabrica fabricaDestino) {
        if (asignadoA == null) {
            asignadoA = fabricaDestino;
            fabricaDestino.asignarTrabajador(this);
        } else {
            RegistroEsclavos registro = RegistroEsclavos.getInstancia();
            registro.getTrabajadores().remove(this);
            if (asignadoA != null && asignadoA.getTrabajadores() != null) {
                asignadoA.getTrabajadores().remove(this);
            }
            asignadoA = fabricaDestino;
            fabricaDestino.asignarTrabajador(this);
            registro.getTrabajadores().add(this);
        }
    }
}
