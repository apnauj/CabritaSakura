package model.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import model.enums.EstadoCuenta;
import model.enums.RolUsuario;

import java.time.LocalDate;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data

public class Duena extends Usuario {
    private String claveMaestra;
    private String fechaCoronacion;
    private final Cliente cliente = new Cliente();
    private final AdministradorUsuario administradorUsuario = new AdministradorUsuario();
    private final AdministradorContenido administradorContenido = new AdministradorContenido();
    private final DesarrolladorProducto desarrolladorProducto = new DesarrolladorProducto();

    public Duena(String nombre, String email, String passwordHash,
                   RolUsuario rol,
                   EstadoCuenta estadoCuenta,
                   String direccionEnvio, String telefono) {

        super(nombre, email, passwordHash, rol, estadoCuenta);
        this.claveMaestra = "Sakura";
        this.fechaCoronacion = LocalDate.now().toString();
    }

}
