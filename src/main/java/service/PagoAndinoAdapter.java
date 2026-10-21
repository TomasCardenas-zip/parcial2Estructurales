package service;

public class PagoAndinoAdapter implements PasarelaPago {
    private final PagoAndinoAPI api;

    public PagoAndinoAdapter(PagoAndinoAPI api) {
        this.api = api;
    }

    @Override
    public ResultadoPago procesarPago(String numeroTarjeta, double valorPesos, String referencia) {
        long valorCentavos = (long) Math.round(valorPesos * 100.0);
        int codigoRespuesta = api.procesarCobro(numeroTarjeta, valorCentavos, referencia);

        if (codigoRespuesta == 0) {
            return new ResultadoPago(true, api.ultimaAutorizacion(), "Pago aprobado exitosamente");
        }

        String motivo;
        switch (codigoRespuesta) {
            case 51 -> motivo = "Fondos insuficientes";
            case 14 -> motivo = "Tarjeta inválida";
            case 91 -> motivo = "Pasarela no disponible";
            default -> motivo = "Error desconocido de pasarela (Código: " + codigoRespuesta + ")";
        }
        return new ResultadoPago(false, null, motivo);
    }
}