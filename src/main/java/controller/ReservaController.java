package controller;

import service.*;
import model.*;

import java.time.LocalDate;
import java.util.List;


public class ReservaController {
    private final AgenciaRutaVerde agencia;

    public ReservaController() {
        this.agencia = AgenciaRutaVerde.getInstance();
    }

    public List<Cliente> obtenerClientes() { return agencia.getClientes(); }
    public List<ProductoTuristico> obtenerProductos() { return agencia.getProductos(); }
    public List<Guia> obtenerGuias() { return agencia.getGuias(); }

    public ResultadoConfirmacion procesarReserva(
            Cliente cliente, ProductoTuristico producto, LocalDate fechaViaje,
            int numViajeros, Guia guia, boolean transporte, boolean seguro,
            String observaciones, String tarjeta, CanalEmision canal) {

        // Validaciones del parcial
        if (cliente == null) {
            return new ResultadoConfirmacion(false, "El cliente es obligatorio.", null);
        }
        if (producto == null) {
            return new ResultadoConfirmacion(false, "El producto turístico es obligatorio.", null);
        }
        if (fechaViaje == null || fechaViaje.isBefore(LocalDate.now())) {
            return new ResultadoConfirmacion(false, "La fecha del viaje no puede estar en el pasado.", null);
        }
        if (numViajeros < 1 || numViajeros > 15) {
            return new ResultadoConfirmacion(false, "El grupo debe tener entre 1 y 15 viajeros.", null);
        }
        if (producto.tieneActividadSuspendida()) {
            return new ResultadoConfirmacion(false, "El producto incluye actividades suspendidas.", null);
        }
        if (guia != null && !guia.getIdiomas().contains(cliente.getIdiomaPreferido())) {
            return new ResultadoConfirmacion(false, "El guía asignado (" + guia.getNombre() + ") no habla el idioma preferido del cliente (" + cliente.getIdiomaPreferido() + ").", null);
        }

        // Creación del objeto Reserva
        Reserva reserva = new Reserva(cliente, producto, fechaViaje, numViajeros, guia, transporte, seguro, observaciones);

        // Integración con Adaptador y Facade
        PasarelaPago pasarela = new PagoAndinoAdapter(new PagoAndinoAPI());
        ConfirmacionReservaFacade facade = new ConfirmacionReservaFacade(pasarela);

        return facade.confirmarReserva(reserva, tarjeta, canal);
    }

    public List<Reserva> obtenerReservasConfirmadas() {
        return agencia.getReservas().stream()
                .filter(r -> "CONFIRMADA".equals(r.getEstado()))
                .toList();
    }

    public double obtenerIngresos(LocalDate inicio, LocalDate fin) {
        return agencia.calcularIngresosEnPeriodo(inicio, fin);
    }

    public ProductoTuristico obtenerProductoMasPopular(LocalDate inicio, LocalDate fin) {
        return agencia.obtenerProductoMasPopularEnPeriodo(inicio, fin);
    }
}