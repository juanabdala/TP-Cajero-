package util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FormateadorMoneda {

    private static final DecimalFormat FORMATO;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("es", "AR"));
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        FORMATO = new DecimalFormat("$#,##0.00", symbols);
    }

    public static String formato(double monto) {
        return FORMATO.format(monto);
    }

    private FormateadorMoneda() {}
}
