package model;

import exception.CuentaInactivaException;
import exception.LimiteExtraccionExcedidoException;
import exception.SaldoInsuficienteException;
import util.FormateadorMoneda;
import util.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CuentaBancaria {

    private static final double LIMITE_EXTRACCION = 10000.0;
    private static final int MAX_HISTORIAL = 10;

    private final String numeroCuenta;   // inmutable
    private double saldo;
    private final String titular;
    private boolean activa;
    private final ArrayList<Transaccion> historialTransacciones;

    public CuentaBancaria(String numeroCuenta, String titular, double saldoInicial) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = saldoInicial;
        this.activa = true;
        this.historialTransacciones = new ArrayList<>();
        registrarTransaccion(TipoTransaccion.DEPOSITO, saldoInicial, "Apertura de cuenta con saldo inicial");
    }

    // ─── DEPÓSITO ───────────────────────────────────────────────────────────────
    public void depositar(double monto) throws CuentaInactivaException, IllegalArgumentException {
        validarCuentaActiva();
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto del depósito debe ser positivo.");
        }
        saldo += monto;
        String log = registrarTransaccion(TipoTransaccion.DEPOSITO, monto,
                "Depósito de " + FormateadorMoneda.formato(monto));
        Logger.info(log);
    }

    // ─── EXTRACCIÓN ─────────────────────────────────────────────────────────────
    public void extraer(double monto)
            throws CuentaInactivaException, SaldoInsuficienteException, LimiteExtraccionExcedidoException {
        validarCuentaActiva();
        if (monto <= 0) throw new IllegalArgumentException("El monto de extracción debe ser positivo.");
        if (monto > LIMITE_EXTRACCION)
            throw new LimiteExtraccionExcedidoException(monto, LIMITE_EXTRACCION);
        if (monto > saldo)
            throw new SaldoInsuficienteException(saldo, monto);

        saldo -= monto;
        String log = registrarTransaccion(TipoTransaccion.EXTRACCION, monto,
                "Extracción de " + FormateadorMoneda.formato(monto));
        Logger.info(log);
    }

    // ─── TRANSFERENCIA (débito de esta cuenta) ──────────────────────────────────
    public void transferirA(CuentaBancaria destino, double monto)
            throws CuentaInactivaException, SaldoInsuficienteException, LimiteExtraccionExcedidoException {
        validarCuentaActiva();
        destino.validarCuentaActiva();
        if (monto <= 0) throw new IllegalArgumentException("El monto de transferencia debe ser positivo.");
        if (monto > LIMITE_EXTRACCION)
            throw new LimiteExtraccionExcedidoException(monto, LIMITE_EXTRACCION);
        if (monto > saldo)
            throw new SaldoInsuficienteException(saldo, monto);

        // Operación atómica
        saldo -= monto;
        destino.saldo += monto;

        String logOrigen = registrarTransaccion(TipoTransaccion.TRANSFERENCIA, monto,
                "Transferencia enviada a [" + destino.getNumeroCuenta() + "] por " + FormateadorMoneda.formato(monto));
        String logDestino = destino.registrarTransaccion(TipoTransaccion.TRANSFERENCIA, monto,
                "Transferencia recibida de [" + this.numeroCuenta + "] por " + FormateadorMoneda.formato(monto));

        Logger.info(logOrigen);
        Logger.info(logDestino);
    }

    // ─── CONSULTA SALDO ─────────────────────────────────────────────────────────
    public double consultarSaldo() throws CuentaInactivaException {
        validarCuentaActiva();
        String log = registrarTransaccion(TipoTransaccion.CONSULTA, 0,
                "Consulta de saldo: " + FormateadorMoneda.formato(saldo));
        Logger.info(log);
        return saldo;
    }

    // ─── HISTORIAL ──────────────────────────────────────────────────────────────
    public List<Transaccion> obtenerUltimasTransacciones() {
        int total = historialTransacciones.size();
        int desde = Math.max(0, total - MAX_HISTORIAL);
        return historialTransacciones.subList(desde, total);
    }

    // ─── DESACTIVAR ─────────────────────────────────────────────────────────────
    public void desactivar() {
        this.activa = false;
        Logger.info("Cuenta [" + numeroCuenta + "] desactivada.");
    }

    // ─── HELPERS INTERNOS ───────────────────────────────────────────────────────
    void validarCuentaActiva() throws CuentaInactivaException {
        if (!activa) throw new CuentaInactivaException(numeroCuenta);
    }

    private String registrarTransaccion(TipoTransaccion tipo, double monto, String desc) {
        LocalDateTime ahora = LocalDateTime.now();
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(Logger.formatearFecha(ahora)).append("] ")
          .append(tipo.getDescripcion().toUpperCase()).append(": ");
        if (tipo != TipoTransaccion.CONSULTA) {
            sb.append(FormateadorMoneda.formato(monto)).append(" | ");
        }
        sb.append("Saldo: ").append(FormateadorMoneda.formato(saldo));

        Transaccion t = new Transaccion(tipo, monto, ahora, sb.toString());
        historialTransacciones.add(t);
        return sb.toString();
    }

    // ─── GETTERS ────────────────────────────────────────────────────────────────
    public String getNumeroCuenta() { return numeroCuenta; }
    public String getTitular() { return titular; }
    public boolean isActiva() { return activa; }
    public double getSaldo() { return saldo; }

    @Override
    public String toString() {
        return String.format("Cuenta [%s] | Titular: %-20s | Saldo: %s | %s",
                numeroCuenta, titular, FormateadorMoneda.formato(saldo),
                activa ? "ACTIVA" : "INACTIVA");
    }
}
