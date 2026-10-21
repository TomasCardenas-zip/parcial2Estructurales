package service;

public class ValidadorLuhn {
    public static boolean esTarjetaValida(String numeroTarjeta) {
        if (numeroTarjeta == null) return false;
        String limpios = numeroTarjeta.replaceAll("\\s+", "");
        if (!limpios.matches("\\d{13,19}")) return false;

        int suma = 0;
        boolean alternar = false;

        for (int i = limpios.length() - 1; i >= 0; i--) {
            int digito = Integer.parseInt(limpios.substring(i, i + 1));
            if (alternar) {
                digito *= 2;
                if (digito > 9) {
                    digito -= 9;
                }
            }
            suma += digito;
            alternar = !alternar;
        }

        return (suma % 10 == 0);
    }
}
