package exception;

import util.FormateadorMoneda;

public class SaldoInsuficienteException extends Exception {
    private final double saldoActual;
    private final double montoSolicitado;

    public SaldoInsuficienteException(double saldoActual, double montoSolicitado) {
        super(String.format("Saldo insuficiente. Saldo disponible: %s | Monto solicitado: %s",
                FormateadorMoneda.formato(saldoActual),
                FormateadorMoneda.formato(montoSolicitado)));
        this.saldoActual = saldoActual;
        this.montoSolicitado = montoSolicitado;
    }

    public double getSaldoActual() { return saldoActual; }
    public double getMontoSolicitado() { return montoSolicitado; }
}
