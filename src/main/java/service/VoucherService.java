package service;

import model.Reserva;

public class VoucherService {
    public static String generarVoucher(Reserva reserva, CanalEmision canal) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== AGENCIA RUTAVERDE QUINDÍO ===\n");
        sb.append("RNT: 123456 | NIT: 900.123.456-7\n");
        sb.append("---------------------------------\n");

        switch (canal) {
            case ASESOR -> {
                sb.append("VOUCHER DETALLADO - RESERVA #").append(reserva.getNumeroReserva()).append("\n");
                sb.append("Cliente: ").append(reserva.getCliente().getNombreCompleto()).append("\n");
                sb.append("Producto: ").append(reserva.getProducto().getNombre()).append("\n");
                sb.append("Fecha Viaje: ").append(reserva.getFechaViaje()).append("\n");
                sb.append("Viajeros: ").append(reserva.getNumViajeros()).append("\n");
                if (reserva.getGuia() != null) {
                    sb.append("Guía Asignado: ").append(reserva.getGuia().getNombre()).append("\n");
                }
                sb.append("Opcionales: Transporte=").append(reserva.isIncluyeTransporte() ? "SÍ" : "NO")
                        .append(", Seguro=").append(reserva.isIncluyeSeguro() ? "SÍ" : "NO").append("\n");
                sb.append("Total Pagado: $").append(String.format("%,.2f", reserva.calcularValorTotal())).append("\n");
            }
            case KIOSCO -> {
                sb.append("TIQUETE RÁPIDO #").append(reserva.getNumeroReserva())
                        .append(" | CÓDIGO ENTRADA: RV-").append(reserva.getNumeroReserva()).append("-2026\n");
            }
            case APP_MOVIL -> {
                sb.append("VOUCHER DIGITAL MÓVIL #").append(reserva.getNumeroReserva()).append("\n");
                sb.append("M-TICKET PASAJERO: ").append(reserva.getCliente().getNombreCompleto()).append("\n");
            }
        }

        sb.append("---------------------------------\n");
        sb.append("POLÍTICA: Cancelación sin costo hasta 48 horas antes del viaje.\n");
        return sb.toString();
    }
}