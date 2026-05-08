package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void info(String mensaje) {
        System.out.println("  " + mensaje);
    }

    public static void error(String mensaje) {
        System.out.println("  ⚠  ERROR: " + mensaje);
    }

    public static void titulo(String texto) {
        String linea = "═".repeat(60);
        System.out.println("\n" + linea);
        System.out.println("  " + texto.toUpperCase());
        System.out.println(linea);
    }

    public static void separador() {
        System.out.println("  " + "─".repeat(58));
    }

    public static String formatearFecha(LocalDateTime fechaHora) {
        return fechaHora.format(FORMATTER);
    }

    private Logger() {}
}
