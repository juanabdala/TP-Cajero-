package ui;

import exception.CuentaInactivaException;
import exception.LimiteExtraccionExcedidoException;
import exception.SaldoInsuficienteException;
import service.BancoService;
import util.FormateadorMoneda;
import util.Logger;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CajeroUI {

    private final BancoService bancoService;
    private final Scanner scanner;

    public CajeroUI(BancoService bancoService) {
        this.bancoService = bancoService;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        Logger.titulo("Bienvenido al Cajero Automático - Banco DACEFyN");

        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            int opcion = leerEntero("Seleccione una opción: ");

            continuar = switch (opcion) {
                case 1 -> { ejecutarDeposito();    yield true; }
                case 2 -> { ejecutarExtraccion();  yield true; }
                case 3 -> { ejecutarTransferencia(); yield true; }
                case 4 -> { ejecutarConsultaSaldo(); yield true; }
                case 5 -> { ejecutarHistorial();   yield true; }
                case 6 -> { bancoService.mostrarResumenCuentas(); yield true; }
                case 0 -> { Logger.titulo("Sesión finalizada. ¡Hasta pronto!"); yield false; }
                default -> { Logger.error("Opción inválida. Intente nuevamente."); yield true; }
            };
        }
    }

    // ─── MENÚ ────────────────────────────────────────────────────────────────────

    private void mostrarMenu() {
        System.out.println("\n  ┌─────────────────────────────────┐");
        System.out.println("  │        MENÚ PRINCIPAL           │");
        System.out.println("  ├─────────────────────────────────┤");
        System.out.println("  │  1. Depositar                   │");
        System.out.println("  │  2. Extraer                     │");
        System.out.println("  │  3. Transferir                  │");
        System.out.println("  │  4. Consultar saldo             │");
        System.out.println("  │  5. Ver historial               │");
        System.out.println("  │  6. Ver resumen de cuentas      │");
        System.out.println("  │  0. Salir                       │");
        System.out.println("  └─────────────────────────────────┘");
    }

    // ─── OPERACIONES ─────────────────────────────────────────────────────────────

    private void ejecutarDeposito() {
        String nro = leerTexto("Número de cuenta: ");
        double monto = leerDouble("Monto a depositar: $");
        try {
            bancoService.depositar(nro, monto);
            System.out.println("  ✔ Depósito realizado correctamente.");
        } catch (CuentaInactivaException | IllegalArgumentException e) {
            Logger.error(e.getMessage());
        }
    }

    private void ejecutarExtraccion() {
        String nro = leerTexto("Número de cuenta: ");
        double monto = leerDouble("Monto a extraer: $");
        try {
            bancoService.extraer(nro, monto);
            System.out.println("  ✔ Extracción realizada correctamente.");
        } catch (CuentaInactivaException | SaldoInsuficienteException |
                 LimiteExtraccionExcedidoException | IllegalArgumentException e) {
            Logger.error(e.getMessage());
        }
    }

    private void ejecutarTransferencia() {
        String origen = leerTexto("Cuenta origen: ");
        String destino = leerTexto("Cuenta destino: ");
        double monto = leerDouble("Monto a transferir: $");
        try {
            bancoService.transferir(origen, destino, monto);
            System.out.println("  ✔ Transferencia realizada correctamente.");
        } catch (CuentaInactivaException | SaldoInsuficienteException |
                 LimiteExtraccionExcedidoException | IllegalArgumentException e) {
            Logger.error(e.getMessage());
        }
    }

    private void ejecutarConsultaSaldo() {
        String nro = leerTexto("Número de cuenta: ");
        try {
            double saldo = bancoService.consultarSaldo(nro);
            System.out.println("  ✔ Saldo disponible: " + FormateadorMoneda.formato(saldo));
        } catch (CuentaInactivaException | IllegalArgumentException e) {
            Logger.error(e.getMessage());
        }
    }

    private void ejecutarHistorial() {
        String nro = leerTexto("Número de cuenta: ");
        try {
            bancoService.mostrarHistorial(nro);
        } catch (IllegalArgumentException e) {
            Logger.error(e.getMessage());
        }
    }

    // ─── HELPERS DE ENTRADA ──────────────────────────────────────────────────────

    private int leerEntero(String prompt) {
        while (true) {
            try {
                System.out.print("\n  " + prompt);
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                Logger.error("Ingrese un número entero válido.");
                scanner.nextLine();
            }
        }
    }

    private double leerDouble(String prompt) {
        while (true) {
            try {
                System.out.print("  " + prompt);
                double valor = scanner.nextDouble();
                scanner.nextLine();
                return valor;
            } catch (InputMismatchException e) {
                Logger.error("Ingrese un monto numérico válido.");
                scanner.nextLine();
            }
        }
    }

    private String leerTexto(String prompt) {
        System.out.print("  " + prompt);
        return scanner.next().trim();
    }
}
