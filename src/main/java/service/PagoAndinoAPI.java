package service;

public class PagoAndinoAPI {
    private int consecutivo = 1000;

    public int procesarCobro(long montoCentavos, String numeroTarjeta) {
        if (numeroTarjeta != null && !numeroTarjeta.isEmpty()) {
            return 0; // Aprobado
        }
        return 1; // Rechazado
    }
}