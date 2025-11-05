package model.comercial;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor

public class LineaCompra implements Serializable {
    private Long cantidad;
    private Producto producto;

    public LineaCompra(Long cantidad, Producto producto) {
        this.cantidad = cantidad;
        this.producto = producto;
    }

    public double calcularSubtotal(){
        return this.producto.getPrecio() * Double.parseDouble((this.cantidad).toString());
    }

    public void obtenerDetalleDeCompra(){

    }
}
