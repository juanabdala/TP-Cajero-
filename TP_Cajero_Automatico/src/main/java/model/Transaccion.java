package model;

import java.time.LocalDateTime;

public class Transaccion {
    private final TipoTransaccion tipo;
    private final double monto;
    private final LocalDateTime fechaHora;
    private final String descripcion;

    public Transaccion(TipoTransaccion tipo, double monto, LocalDateTime fechaHora, String descripcion) {
        this.tipo = tipo;
        this.monto = monto;
        this.fechaHora = fechaHora;
        this.descripcion = descripcion;
    }

    public TipoTransaccion getTipo() { return tipo; }
    public double getMonto() { return monto; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public String getDescripcion() { return descripcion; }

    @Override
    public String toString() {
        return descripcion;
    }
}
