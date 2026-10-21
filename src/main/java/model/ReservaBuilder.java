package model;

import java.time.LocalDate;

public class ReservaBuilder {
    protected Cliente cliente;
    protected ProductoTuristico producto;
    protected LocalDate fechaViaje;
    protected int numViajeros;
    protected Guia guia;
    protected boolean incluyeTransporte;
    protected boolean incluyeSeguro;
    protected String observaciones;

    public ReservaBuilder setCliente(Cliente cliente) {
        this.cliente = cliente;
        return this;
    }

    public ReservaBuilder setProducto(ProductoTuristico producto) {
        this.producto = producto;
        return this;
    }

    public ReservaBuilder setFechaViaje(LocalDate fechaViaje) {
        this.fechaViaje = fechaViaje;
        return this;
    }

    public ReservaBuilder setNumViajeros(int numViajeros) {
        this.numViajeros = numViajeros;
        return this;
    }

    public ReservaBuilder setGuia(Guia guia) {
        this.guia = guia;
        return this;
    }

    public ReservaBuilder setTransporte(boolean incluyeTransporte) {
        this.incluyeTransporte = incluyeTransporte;
        return this;
    }

    public ReservaBuilder setSeguro(boolean incluyeSeguro) {
        this.incluyeSeguro = incluyeSeguro;
        return this;
    }

    public ReservaBuilder setObservaciones(String observaciones) {
        this.observaciones = observaciones;
        return this;
    }

    public Reserva build() {
        if (cliente == null) throw new IllegalArgumentException("La reserva exige un cliente válido.");
        if (producto == null) throw new IllegalArgumentException("La reserva exige un producto turístico.");
        if (fechaViaje == null || fechaViaje.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de viaje no puede estar en el pasado.");
        }
        if (numViajeros < 1 || numViajeros > 15) {
            throw new IllegalArgumentException("El número de viajeros debe estar entre 1 y 15.");
        }
        if (guia != null && !guia.hablaIdioma(cliente.getIdiomaPreferido())) {
            throw new IllegalArgumentException("El guía asignado debe hablar el idioma preferido del cliente.");
        }
        if (producto.tieneActividadSuspendida()) {
            throw new IllegalArgumentException("No se puede reservar un producto con actividades suspendidas.");
        }

        return new Reserva(this);
    }
}
