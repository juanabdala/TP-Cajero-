import exception.CuentaInactivaException;
import exception.LimiteExtraccionExcedidoException;
import exception.SaldoInsuficienteException;
import model.CuentaBancaria;
import service.BancoService;
import ui.CajeroUI;
import util.Logger;

public class Main {

    public static void main(String[] args) {

        BancoService banco = new BancoService();

        // ── Crear 3 cuentas ──────────────────────────────────────────────────────
        CuentaBancaria cta001 = new CuentaBancaria("001-12345", "María García",    50000.0);
        CuentaBancaria cta002 = new CuentaBancaria("002-67890", "Carlos López",    12000.0);
        CuentaBancaria cta003 = new CuentaBancaria("003-11111", "Ana Rodríguez",    3000.0);

        banco.agregarCuenta(cta001);
        banco.agregarCuenta(cta002);
        banco.agregarCuenta(cta003);

        // ════════════════════════════════════════════════════════════════════════
        //  SIMULACIÓN DE UN DÍA DE OPERACIONES (15 transacciones)
        // ════════════════════════════════════════════════════════════════════════
        Logger.titulo("SIMULACIÓN AUTOMÁTICA — DÍA DE OPERACIONES");

        // 1. Depósito María
        operacion(banco, "001 — Depósito María $5.000", () ->
                banco.depositar("001-12345", 5000.0));

        // 2. Extracción Carlos
        operacion(banco, "002 — Extracción Carlos $3.000", () ->
                banco.extraer("002-67890", 3000.0));

        // 3. Transferencia María → Ana
        operacion(banco, "003 — Transferencia María → Ana $8.000", () ->
                banco.transferir("001-12345", "003-11111", 8000.0));

        // 4. Consulta saldo Ana
        operacion(banco, "004 — Consulta saldo Ana", () ->
                banco.consultarSaldo("003-11111"));

        // 5. Depósito Carlos
        operacion(banco, "005 — Depósito Carlos $20.000", () ->
                banco.depositar("002-67890", 20000.0));

        // 6. ⚠ EXCEPCIÓN: Límite extracción excedido (>$10.000)
        operacion(banco, "006 — ⚠ Extracción $15.000 (excede límite)", () ->
                banco.extraer("002-67890", 15000.0));

        // 7. Extracción válida Carlos
        operacion(banco, "007 — Extracción Carlos $9.500", () ->
                banco.extraer("002-67890", 9500.0));

        // 8. Transferencia Ana → Carlos
        operacion(banco, "008 — Transferencia Ana → Carlos $2.000", () ->
                banco.transferir("003-11111", "002-67890", 2000.0));

        // 9. ⚠ EXCEPCIÓN: Saldo insuficiente Ana
        operacion(banco, "009 — ⚠ Extracción Ana $9.000 (saldo insuf.)", () ->
                banco.extraer("003-11111", 9000.0));

        // 10. Depósito Ana
        operacion(banco, "010 — Depósito Ana $1.500", () ->
                banco.depositar("003-11111", 1500.0));

        // 11. Transferencia Carlos → María
        operacion(banco, "011 — Transferencia Carlos → María $5.000", () ->
                banco.transferir("002-67890", "001-12345", 5000.0));

        // 12. Desactivar cuenta Ana
        Logger.titulo("012 — Desactivar cuenta de Ana");
        banco.desactivarCuenta("003-11111");

        // 13. ⚠ EXCEPCIÓN: Operación en cuenta inactiva
        operacion(banco, "013 — ⚠ Depósito en cuenta inactiva (Ana)", () ->
                banco.depositar("003-11111", 100.0));

        // 14. Extracción María
        operacion(banco, "014 — Extracción María $10.000", () ->
                banco.extraer("001-12345", 10000.0));

        // 15. Consulta final saldo Carlos
        operacion(banco, "015 — Consulta saldo final Carlos", () ->
                banco.consultarSaldo("002-67890"));

        // ── Historial de cada cuenta ─────────────────────────────────────────────
        banco.mostrarHistorial("001-12345");
        banco.mostrarHistorial("002-67890");
        banco.mostrarHistorial("003-11111");

        // ── Resumen general ──────────────────────────────────────────────────────
        banco.mostrarResumenCuentas();

        // ════════════════════════════════════════════════════════════════════════
        //  MODO INTERACTIVO
        // ════════════════════════════════════════════════════════════════════════
        System.out.println("\n¿Desea usar el cajero interactivo? (s/n)");
        java.util.Scanner sc = new java.util.Scanner(System.in);
        String resp = sc.nextLine().trim().toLowerCase();
        if (resp.equals("s")) {
            CajeroUI ui = new CajeroUI(banco);
            ui.iniciar();
        }
    }

    // ── Helper para manejar excepciones en la simulación ──────────────────────
    @FunctionalInterface
    interface Operacion { void ejecutar() throws Exception; }

    private static void operacion(BancoService banco, String descripcion, Operacion op) {
        Logger.titulo(descripcion);
        try {
            op.ejecutar();
            System.out.println("  ✔ OK");
        } catch (SaldoInsuficienteException | LimiteExtraccionExcedidoException |
                 CuentaInactivaException | IllegalArgumentException e) {
            Logger.error(e.getMessage());
        } catch (Exception e) {
            Logger.error("Error inesperado: " + e.getMessage());
        }
    }
}
