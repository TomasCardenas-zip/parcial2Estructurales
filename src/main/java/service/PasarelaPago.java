package service;

public interface PasarelaPago {
    ResultadoPago procesarPago(double monto, String numeroTarjeta);
}