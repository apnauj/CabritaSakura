package model.comercial;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@Data
@ToString(exclude = {"categoria"})

public class Producto implements Serializable {
    private static Long nextId= 1L;
    private Long id;
    private String nombre;
    private String descripcion;
    private double precio;
    private Long stock;
    private String fechaLanzamiento;
    private Categoria categoria;

    public Producto(String nombre, String descripcion, double precio,
                    Long stock, Categoria categoria) {
        this.id = nextId++;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.fechaLanzamiento = LocalDate.now().toString();
        this.categoria = categoria;

        if (categoria != null) {
            categoria.getProductos().add(this);
        }

    }



    private void actualizarStock(Long nuevoStock){
        this.stock=nuevoStock;
    }

    public void obtenerDetalles(){
        System.out.println(this.toString());
    }

}
