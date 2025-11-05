package model.usuario;

import exception.CarritoVacioException;
import exception.SaldoInsuficienteException;
import exception.SinMetodoDePagoException;
import exception.StockInsuficienteException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import model.comercial.*;
import model.enums.EstadoCompra;
import model.enums.EstadoCuenta;
import model.enums.RolUsuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data

public class Cliente extends Usuario {
    private String direccionEnvio;
    private String telefono;
    private Map<Long,MetodoDePago> metodosDePago = new HashMap<>();
    private Carrito carrito = new Carrito();
    private ArrayList<Compra> historialCompras = new ArrayList<>();

    public void anadirMetodoDePago(Long id,MetodoDePago metodoDePago) {
        this.metodosDePago.put(id, metodoDePago);
    }

    public Cliente(String nombre, String email, String passwordHash,
                   RolUsuario rol,
                   EstadoCuenta estadoCuenta,
                   String direccionEnvio, String telefono) {

        super(nombre, email, passwordHash, rol, estadoCuenta);
        this.direccionEnvio = direccionEnvio;
        this.telefono = telefono;
    }

    public void agregarAlCarrito(Producto producto, Long cantidad) throws StockInsuficienteException{
        if (cantidad > producto.getStock()){
            throw new CarritoVacioException("No hay stock suficiente");
        } else {
            this.carrito.getLineaCarrito().add(new LineaCarrito(cantidad, producto));
            System.out.println("\n✅Producto añadidio al carrito correctamente\n");
        }
    }

    public void realizarCompra(Long idMetodoDePago)
            throws CarritoVacioException,
            SinMetodoDePagoException,
            SaldoInsuficienteException
    {
        if (this.carrito.getLineaCarrito().isEmpty()) {
            throw new CarritoVacioException("El carrito está vacío. No se puede realizar la compra.");
        } else {
            if (metodosDePago.containsKey(idMetodoDePago)) {
                MetodoDePago metodoDePago = metodosDePago.get(idMetodoDePago);
                if (metodoDePago.validar(carrito.calcularTotal())){
                    metodoDePago.setSaldo(metodoDePago.getSaldo() - carrito.calcularTotal());
                    Compra nuevaCompra = new Compra();

                    for (LineaCarrito lineaCarrito : this.carrito.getLineaCarrito()) {
                        LineaCompra lineaCompra = new LineaCompra(
                                lineaCarrito.getCantidad(),
                                lineaCarrito.getProducto()
                        );
                        nuevaCompra.agregarLinea(lineaCompra);

                    }
                    nuevaCompra.setEstado(EstadoCompra.FINALIZADA);
                    this.historialCompras.add(nuevaCompra);
                    this.carrito.vaciarCarrito();
                    System.out.println("Compra realizada exitosamente!");

                } else {
                    throw new SaldoInsuficienteException("Saldo insuficiente");
                }
            } else {
                throw new SinMetodoDePagoException("No existe el metodo de pago seleccionado");
            }
        }
    }


    public void verHistorialDeCompras(){
        if (this.historialCompras == null || this.historialCompras.isEmpty()) {
            System.out.println("No hay compras en el historial.");
            return;
        }
        for (Compra compra : this.historialCompras) {
            System.out.println("\nID: "+compra.getId()+" Fecha: "+compra.getFecha());
            if (compra.getLineaCompra() != null) {
                for (LineaCompra lineaCompra : compra.getLineaCompra()) {
                    if (lineaCompra != null && lineaCompra.getProducto() != null) {
                        System.out.println("  Producto: "+lineaCompra.getProducto().getNombre()+" | Cantidad: "+lineaCompra.getCantidad()+" | Subtotal: "+lineaCompra.getProducto().getPrecio()*lineaCompra.getCantidad());
                    }
                }
            }
            System.out.println("Total: "+compra.getTotal()+"\n");
        }
    }

}
