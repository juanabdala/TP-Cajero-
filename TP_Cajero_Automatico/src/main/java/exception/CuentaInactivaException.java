package exception;

public class CuentaInactivaException extends Exception {
    private final String numeroCuenta;

    public CuentaInactivaException(String numeroCuenta) {
        super("La cuenta [" + numeroCuenta + "] está inactiva. No se pueden realizar operaciones.");
        this.numeroCuenta = numeroCuenta;
    }

    public String getNumeroCuenta() { return numeroCuenta; }
}
