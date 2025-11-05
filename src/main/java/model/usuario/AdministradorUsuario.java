package model.usuario;

import lombok.*;
import model.enums.EstadoCuenta;
import model.enums.NivelAcceso;
import model.enums.RolUsuario;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data

public class AdministradorUsuario extends Usuario {
    private NivelAcceso  nivelAcceso;

    public AdministradorUsuario(String nombre, String email, String passwordHash, RolUsuario rolUsuario, EstadoCuenta estadoCuenta, NivelAcceso nivelAcceso) {
        super(nombre,email,passwordHash,rolUsuario,estadoCuenta);
        this.nivelAcceso = nivelAcceso;
    }

    public void reactivarUsuario(Usuario usuario) {
        if (usuario != null) {
            usuario.setEstadoCuenta(EstadoCuenta.ACTIVA);
        }
    }

    public void suspenderUsuario(Usuario usuario) {
        if (usuario != null) {
            usuario.setEstadoCuenta(EstadoCuenta.SUSPENDIDA);
        }
    }
}
