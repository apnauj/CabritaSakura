package model.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import model.enums.EstadoCuenta;
import model.enums.NivelAcceso;
import model.enums.PermisosEdicion;
import model.enums.RolUsuario;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data

public class AdministradorContenido extends Usuario {
    private PermisosEdicion permisosEdicion;

    public AdministradorContenido(String nombre, String email, String passwordHash, RolUsuario rolUsuario, EstadoCuenta estadoCuenta, PermisosEdicion permisosEdicion) {
        super(nombre,email,passwordHash,rolUsuario,estadoCuenta);
        this.permisosEdicion = permisosEdicion;
    }

}
