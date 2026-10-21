package service;


public class PagoAndinoAdapter implements PasarelaPago {
    private final PagoAndinoAPI apiExterna;

    public PagoAndinoAdapter(PagoAndinoAPI apiExterna) {
        this.apiExterna = apiExterna;
    }

    @Override
    public ResultadoPago procesarPago(double monto, String numeroTarjeta) {
        long montoCentavos = Math.round(monto * 100);
        // Llamada con exactamente 2 argumentos:
        int codigoRespuesta = apiExterna.procesarCobro(montoCentavos, numeroTarjeta);

        if (codigoRespuesta == 0) {
            return new ResultadoPago(true, "Pago aprobado por PagoAndino.");
        } else {
            return new ResultadoPago(false, "Transacción rechazada por la pasarela.");
        }
    }
}