package exception;

import util.FormateadorMoneda;

public class LimiteExtraccionExcedidoException extends Exception {
    private final double montoSolicitado;
    private final double limitePermitido;

    public LimiteExtraccionExcedidoException(double montoSolicitado, double limitePermitido) {
        super(String.format("Límite de extracción excedido. Monto: %s | Límite permitido: %s por operación",
                FormateadorMoneda.formato(montoSolicitado),
                FormateadorMoneda.formato(limitePermitido)));
        this.montoSolicitado = montoSolicitado;
        this.limitePermitido = limitePermitido;
    }

    public double getMontoSolicitado() { return montoSolicitado; }
    public double getLimitePermitido() { return limitePermitido; }
}
