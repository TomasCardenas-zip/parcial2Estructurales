package service;

import model.AgenciaRutaVerde;
import model.Reserva;

public class VoucherService {

    public static String generarVoucher(Reserva reserva, CanalEmision canal) {
        AgenciaRutaVerde agencia = AgenciaRutaVerde.getInstance();

        StringBuilder sb = new StringBuilder();
        sb.append("=================================================\n");
        sb.append("   ").append(agencia.getNombreComercial().toUpperCase()).append("\n");
        sb.append("   RNT: ").append(agencia.getRnt()).append(" | NIT: ").append(agencia.getNit()).append("\n");
        sb.append("=================================================\n");

        if (canal == CanalEmision.KIOSCO) {
            // Formato tiquete de una sola línea
            sb.append("TIQUETE KIOSCO | Reserva #").append(reserva.getConsecutivo())
                    .append(" | Cliente: ").append(reserva.getCliente().getNombreCompleto())
                    .append(" | Plan: ").append(reserva.getProducto().getNombre())
                    .append(" | CÓDIGO: RV-").append(reserva.getConsecutivo()).append("\n");
        } else if (canal == CanalEmision.MOVIL) {
            // Formato resumido app móvil
            sb.append("RESERVA MÓVIL #").append(reserva.getConsecutivo()).append("\n");
            sb.append("Cliente: ").append(reserva.getCliente().getNombreCompleto()).append("\n");
            sb.append("Producto: ").append(reserva.getProducto().getNombre()).append("\n");
            sb.append("Fecha: ").append(reserva.getFechaViaje()).append(" | Viajeros: ").append(reserva.getNumViajeros()).append("\n");
            sb.append("Total Pagado: $").append(String.format("%,.2f", reserva.calcularValorTotal())).append("\n");
        } else {
            // Formato detallado para Asesor comercial
            sb.append("COMPROBANTE DE RESERVA DETALLADO #").append(reserva.getConsecutivo()).append("\n");
            sb.append("Cliente: ").append(reserva.getCliente().getNombreCompleto())
                    .append(" (Doc: ").append(reserva.getCliente().getDocumento()).append(")\n");
            sb.append("Producto Reservado: ").append(reserva.getProducto().getNombre()).append("\n");
            sb.append("Fecha de Viaje: ").append(reserva.getFechaViaje()).append("\n");
            sb.append("Número de Viajeros: ").append(reserva.getNumViajeros()).append("\n");
            sb.append("Guía Asignado: ").append(reserva.getGuia() != null ? reserva.getGuia().getNombre() : "Sin guía").append("\n");
            sb.append("Servicios Opcionales: ")
                    .append(reserva.isIncluyeTransporte() ? "[Transporte] " : "")
                    .append(reserva.isIncluyeSeguro() ? "[Seguro de Viaje]" : "Ninguno").append("\n");
            sb.append("Observaciones: ").append(reserva.getObservaciones() != null ? reserva.getObservaciones() : "N/A").append("\n");
            sb.append("VALOR TOTAL PAGADO: $").append(String.format("%,.2f", reserva.calcularValorTotal())).append("\n");
        }

        sb.append("-------------------------------------------------\n");
        sb.append("Política de Cancelación: Cancelación sin costo hasta 48 horas antes del viaje.\n");
        sb.append("=================================================\n");

        return sb.toString();
    }
}