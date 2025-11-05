package model.oscuro;

import lombok.Data;
import lombok.Getter;
import model.usuario.Usuario;

import java.io.Serializable;
import java.util.ArrayList;

@Data

public class ConsejoSombrio implements Serializable {
    @Getter
    private static final ConsejoSombrio instancia =  new ConsejoSombrio();
    private static String nombreClave = "Consejo Legal de Glow Up";
    private ArrayList<Usuario> miembros;

    private ConsejoSombrio() {
        this.miembros = new ArrayList<>();
    }


    public void agregarMiembro(Usuario miembro){
        if (miembro != null && !miembros.contains(miembro)) {
            miembros.add(miembro);
            System.out.println("Miembro añadido con exito");
        } else {
            System.out.println("El miembro ya existia o era nulo");
        }
    }

    public void eliminarMiembro(Usuario miembro){
        if (miembros.contains(miembro)) {
            miembros.remove(miembro);
            System.out.println("Miembro eliminado con exito");
        } else {
            System.out.println("El usuario no pertenecia o era nulo");
        }
    }
}
