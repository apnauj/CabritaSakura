package model.comercial;

import lombok.Data;
import model.enums.EstadoCompra;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data

public class Compra implements Serializable {
    private static Long nextId= 1L;
    private Long id;
    private String fecha;
    private double total;
    private EstadoCompra estado;
    private ArrayList<LineaCompra> lineaCompra = new ArrayList<>();

    public Compra() {
        this.id = nextId++;
        this.fecha = LocalDateTime.now().toString();
        this.estado = EstadoCompra.PENDIENTE;
    }

    public void agregarLinea(LineaCompra linea) {
        this.lineaCompra.add(linea);
        this.total += linea.calcularSubtotal();
    }

    public void actualizarEstado(EstadoCompra estado){
        this.estado = estado;
    }
}
