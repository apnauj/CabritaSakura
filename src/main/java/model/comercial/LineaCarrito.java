package model.comercial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor

public class LineaCarrito implements Serializable {
    private Long cantidad;
    private Producto producto;

    public LineaCarrito(Long cantidad, Producto producto){
        this.cantidad = cantidad;
        this.producto = producto;
    }

    public double calcularSubtotal(){
        return this.producto.getPrecio() * Double.parseDouble((this.cantidad).toString());
    }
}
