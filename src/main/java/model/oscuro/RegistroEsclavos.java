package model.oscuro;

import lombok.*;
import model.enums.NivelCifrado;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data

//Aplicación del patrón de diseño Singleton
// 1. Constructor privado
// 2. Instancia estatica privada
// 3. Metodo estático público
// Se usa esto porque solo debe haber un registro
// Una sola instancia a la que se añadan los trabajadores

public class RegistroEsclavos implements Serializable {

    @Setter(AccessLevel.NONE)
    @Getter
    private static final RegistroEsclavos instancia = new RegistroEsclavos();
    @Setter(AccessLevel.NONE)
    private static String ultimoAcceso;
    private NivelCifrado nivelCifrado;
    private ArrayList<TrabajadorEsclavizado> trabajadores;

    private RegistroEsclavos() {
        this.trabajadores = new ArrayList<>();
    }

    public void agregarTrabajador(TrabajadorEsclavizado trabajador) {
        if (trabajador != null && !trabajadores.contains(trabajador)) {
            trabajadores.add(trabajador);
            actualizarUltimoAcceso();
        }
    }

    private void actualizarUltimoAcceso() {
        ultimoAcceso = LocalDateTime.now().toString();
    }

}
