package model.comercial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;

@NoArgsConstructor
@Getter
@Data

public class Carrito implements Serializable {
    private static Long nextId= 1L;
    private Long id;
    private String fechaDeCreacion;
    private ArrayList<LineaCarrito> lineaCarrito = new ArrayList<>();

    public Carrito(String fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
        this.id = nextId++;
    }

    public double calcularTotal() {
        double total = 0;
        for (LineaCarrito lineaCarrito : this.lineaCarrito) {
            total+=lineaCarrito.calcularSubtotal();
        }
        return total;
    }

    public void vaciarCarrito() {
        this.lineaCarrito.clear();
    }
}
