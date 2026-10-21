package service;

import model.Actividad;
import model.AgenciaRutaVerde;
import model.Reserva;


public class ConfirmacionReservaFacade {
    private final PasarelaPago pasarelaPago;

    public ConfirmacionReservaFacade(PasarelaPago pasarelaPago) {
        this.pasarelaPago = pasarelaPago;
    }

    /**
     * Secuencia exacta de 7 pasos exigida por el negocio:
     * 1. Verificar cupos en la fecha para todas las actividades del producto.
     * 2. Validar tarjeta mediante algoritmo de Luhn.
     * 3. Cobrar en la pasarela.
     * 4. Descontar cupos de las actividades.
     * 5. Registrar reserva con número consecutivo.
     * 6. Emitir voucher.
     * 7. Enviar voucher/notificación por correo electrónico.
     */
    public ResultadoConfirmacion confirmarReserva(Reserva reserva, String numeroTarjeta, CanalEmision canal) {
        for (Actividad act : reserva.getProducto().getActividades()) {
            if (!act.hayCupoDisponible(reserva.getFechaViaje(), reserva.getNumViajeros())) {
                notificarCorreo(reserva.getCliente().getCorreo(), "Fallo en reserva: Cupo insuficiente en la actividad " + act.getNombre());
                return new ResultadoConfirmacion(false, "Sin cupo disponible para la actividad: " + act.getNombre(), null);
            }
        }

        if (!ValidadorLuhn.esTarjetaValida(numeroTarjeta)) {
            notificarCorreo(reserva.getCliente().getCorreo(), "Fallo en reserva: Tarjeta de crédito inválida por verificación Luhn.");
            return new ResultadoConfirmacion(false, "Número de tarjeta inválido (Luhn falló).", null);
        }

        double valorTotal = reserva.calcularValorTotal();
        String ref = "RES-" + System.currentTimeMillis();
        ResultadoPago resultadoPago = pasarelaPago.procesarPago(numeroTarjeta, valorTotal, ref);

        if (!resultadoPago.isAprobado()) {
            notificarCorreo(reserva.getCliente().getCorreo(), "Fallo en reserva: Rechazo de pago. " + resultadoPago.getMensajeError());
            return new ResultadoConfirmacion(false, "Cobro rechazado: " + resultadoPago.getMensajeError(), null);
        }

        for (Actividad act : reserva.getProducto().getActividades()) {
            act.descontarCupo(reserva.getFechaViaje(), reserva.getNumViajeros());
        }

        AgenciaRutaVerde agencia = AgenciaRutaVerde.getInstance();
        agencia.registrarReserva(reserva);
        reserva.setEstado("CONFIRMADA");

        String voucher = VoucherService.generarVoucher(reserva, canal);

        notificarCorreo(reserva.getCliente().getCorreo(), "¡Reserva Confirmada!\n\n" + voucher);

        return new ResultadoConfirmacion(true, "Reserva #" + reserva.getNumeroReserva() + " confirmada con éxito.", voucher);
    }

    private void notificarCorreo(String correo, String mensaje) {
        System.out.println("[EMAIL NOTIFICATION -> " + correo + "]\n" + mensaje + "\n");
    }
}

