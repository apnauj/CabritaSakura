package model.usuario;

import model.comercial.Producto;

import java.util.ArrayList;

public class DesarrolladorProducto extends Usuario {
    public String especialidad;

    public void desarrollarProducto(Producto producto, ArrayList<Producto> productos) {
        productos.add(producto);
    }

    public void listarProductos(ArrayList<Producto> productos) {
        for (Producto producto : productos) {
            System.out.println(producto);
        }
    }

    public void eliminarProducto(Producto producto, ArrayList<Producto> productos) {
        productos.remove(producto);
    }

    public void actualizarProducto(Producto producto, ArrayList<Producto> productos) {
        int index = productos.indexOf(producto);
        productos.set(index, producto);
    }
}
