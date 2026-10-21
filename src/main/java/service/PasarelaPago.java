package service;

public interface PasarelaPago {
    ResultadoPago procesarPago(String numeroTarjeta, double valorPesos, String referencia);
}
