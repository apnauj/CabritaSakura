package model.usuario;

import lombok.*;
import model.enums.EstadoCuenta;
import model.enums.RolUsuario;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data

public abstract class Usuario implements Serializable {
    private static Long nextId= 1L;
    private Long id;
    private String nombre;
    private String email;
    private String passwordHash;
    private RolUsuario rol;
    private String fechaRegistro;
    private EstadoCuenta estadoCuenta;

    public Usuario(String nombre, String email, String passwordHash,
                   RolUsuario rol,
                   EstadoCuenta estadoCuenta){
        this.id = nextId++;
        this.nombre = nombre;
        this.email = email;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.fechaRegistro = LocalDate.now().toString();
        this.estadoCuenta = estadoCuenta;
    }

}
