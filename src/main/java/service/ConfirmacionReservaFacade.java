package service;

import model.*;

public class ConfirmacionReservaFacade {
    private final AgenciaRutaVerde agencia;
    private final PasarelaPago pasarelaPago;
    private final EmailService emailService;

    public ConfirmacionReservaFacade(PasarelaPago pasarelaPago) {
        this.agencia = AgenciaRutaVerde.getInstance();
        this.pasarelaPago = pasarelaPago;
        this.emailService = new EmailService();
    }

    public ResultadoConfirmacion confirmarReserva(Reserva reserva, String numeroTarjeta, CanalEmision canal) {
        Cliente cliente = reserva.getCliente();
        String correo = cliente != null ? cliente.getCorreo() : "";

        try {
            // PASO 1: Verificar cupo disponible en la fecha de viaje
            int cupoDisponible = agencia.obtenerCupoDisponible(reserva.getProducto(), reserva.getFechaViaje());
            if (reserva.getNumViajeros() > cupoDisponible) {
                String motivo = "Cupo insuficiente para la fecha seleccionada. Disponibles: " + cupoDisponible;
                emailService.enviarNotificacionFallo(correo, motivo);
                return new ResultadoConfirmacion(false, motivo, null);
            }

            // PASO 2: Validar tarjeta con el algoritmo de Luhn
            if (!ValidadorLuhn.esValida(numeroTarjeta)) {
                String motivo = "El número de tarjeta no supera la validación del algoritmo de Luhn.";
                emailService.enviarNotificacionFallo(correo, motivo);
                return new ResultadoConfirmacion(false, motivo, null);
            }

            // PASO 3: Realizar cobro en la pasarela de pagos
            double montoTotal = reserva.calcularValorTotal();
            ResultadoPago resultadoPago = pasarelaPago.procesarPago(montoTotal, numeroTarjeta);
            if (!resultadoPago.isAprobado()) {
                String motivo = "Cobro rechazado por la pasarela: " + resultadoPago.getMensaje();
                emailService.enviarNotificacionFallo(correo, motivo);
                return new ResultadoConfirmacion(false, motivo, null);
            }

            // PASO 4 y 5: Descontar cupos y Registrar la reserva
            reserva.setEstado("CONFIRMADA");
            agencia.registrarReserva(reserva);

            // PASO 6: Emitir el voucher de forma estática
            String voucher = VoucherService.generarVoucher(reserva, canal);

            // PASO 7: Enviar el voucher al cliente por correo
            emailService.enviarVoucher(correo, voucher);

            return new ResultadoConfirmacion(true, "Reserva N° " + reserva.getConsecutivo() + " confirmada con éxito.", voucher);

        } catch (Exception e) {
            emailService.enviarNotificacionFallo(correo, e.getMessage());
            return new ResultadoConfirmacion(false, "Error en la confirmación: " + e.getMessage(), null);
        }
    }
}