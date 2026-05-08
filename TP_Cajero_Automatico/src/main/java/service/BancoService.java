package service;

import exception.CuentaInactivaException;
import exception.LimiteExtraccionExcedidoException;
import exception.SaldoInsuficienteException;
import model.CuentaBancaria;
import model.Transaccion;
import util.FormateadorMoneda;
import util.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BancoService {

    private final Map<String, CuentaBancaria> cuentas = new HashMap<>();

    // ─── GESTIÓN DE CUENTAS ─────────────────────────────────────────────────────

    public void agregarCuenta(CuentaBancaria cuenta) {
        cuentas.put(cuenta.getNumeroCuenta(), cuenta);
    }

    public CuentaBancaria buscarCuenta(String numeroCuenta) {
        return cuentas.get(numeroCuenta);
    }

    public boolean existeCuenta(String numeroCuenta) {
        return cuentas.containsKey(numeroCuenta);
    }

    // ─── OPERACIONES ────────────────────────────────────────────────────────────

    public void depositar(String numeroCuenta, double monto)
            throws CuentaInactivaException {
        CuentaBancaria cuenta = obtenerCuentaOException(numeroCuenta);
        cuenta.depositar(monto);
    }

    public void extraer(String numeroCuenta, double monto)
            throws CuentaInactivaException, SaldoInsuficienteException, LimiteExtraccionExcedidoException {
        CuentaBancaria cuenta = obtenerCuentaOException(numeroCuenta);
        cuenta.extraer(monto);
    }

    public void transferir(String cuentaOrigenNro, String cuentaDestinoNro, double monto)
            throws CuentaInactivaException, SaldoInsuficienteException, LimiteExtraccionExcedidoException {
        CuentaBancaria origen = obtenerCuentaOException(cuentaOrigenNro);
        CuentaBancaria destino = obtenerCuentaOException(cuentaDestinoNro);
        origen.transferirA(destino, monto);
    }

    public double consultarSaldo(String numeroCuenta) throws CuentaInactivaException {
        CuentaBancaria cuenta = obtenerCuentaOException(numeroCuenta);
        return cuenta.consultarSaldo();
    }

    public void mostrarHistorial(String numeroCuenta) {
        CuentaBancaria cuenta = obtenerCuentaOException(numeroCuenta);
        List<Transaccion> historial = cuenta.obtenerUltimasTransacciones();

        Logger.titulo("Historial - Cuenta [" + numeroCuenta + "] | " + cuenta.getTitular());
        if (historial.isEmpty()) {
            System.out.println("  Sin movimientos registrados.");
        } else {
            historial.forEach(t -> System.out.println("  " + t.getDescripcion()));
        }
        Logger.separador();
    }

    public void mostrarResumenCuentas() {
        Logger.titulo("Resumen de todas las cuentas");
        cuentas.values().forEach(c -> System.out.println("  " + c));
        Logger.separador();
    }

    public void desactivarCuenta(String numeroCuenta) {
        CuentaBancaria cuenta = obtenerCuentaOException(numeroCuenta);
        cuenta.desactivar();
    }

    // ─── HELPER ─────────────────────────────────────────────────────────────────

    private CuentaBancaria obtenerCuentaOException(String numeroCuenta) {
        CuentaBancaria cuenta = cuentas.get(numeroCuenta);
        if (cuenta == null) {
            throw new IllegalArgumentException("Cuenta no encontrada: " + numeroCuenta);
        }
        return cuenta;
    }
}
