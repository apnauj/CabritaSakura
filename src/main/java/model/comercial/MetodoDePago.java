package model.comercial;

import exception.SaldoInsuficienteException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.TipoMetodoDePago;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class MetodoDePago implements Serializable {
    private static Long nextId= 1L;
    private Long id;
    private TipoMetodoDePago tipo;
    private String titular;
    private String numeroEnmascarado;
    private double saldo;

    public MetodoDePago(TipoMetodoDePago tipo, String titular, String numeroEnmascarado, double saldo) {
        this.tipo = tipo;
        this.titular = titular;
        this.numeroEnmascarado = numeroEnmascarado;
        this.saldo = saldo;
        this.id = nextId++;
    }

    public boolean validar(double cantidad) throws SaldoInsuficienteException {
        return saldo >= cantidad;
    }
}
