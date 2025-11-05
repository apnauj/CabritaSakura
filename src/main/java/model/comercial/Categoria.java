package model.comercial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;

@NoArgsConstructor
@Data

public class Categoria implements Serializable {
    private static Long nextId= 1L;
    private Long id;
    private String nombre;
    private String descripcion;
    private ArrayList<Producto> productos;

    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.id = nextId++;
        this.descripcion = descripcion;
    }

    public void listarProductos(){
        for (Producto producto : productos) {
            producto.obtenerDetalles();
        }
    }
}
