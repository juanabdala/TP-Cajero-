package exception;

public class PinInvalidoException extends Exception {
    public PinInvalidoException() {
        super("PIN inválido. Acceso denegado.");
    }
}
