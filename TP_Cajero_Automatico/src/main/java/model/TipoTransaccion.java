package model;

public enum TipoTransaccion {
    DEPOSITO("Depósito"),
    EXTRACCION("Extracción"),
    TRANSFERENCIA("Transferencia"),
    CONSULTA("Consulta");

    private final String descripcion;

    TipoTransaccion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
