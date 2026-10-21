package model;

import java.time.LocalDate;

public class Reserva {
    private int numeroReserva;
    private final Cliente cliente;
    private final ProductoTuristico producto;
    private final LocalDate fechaViaje;
    private final int numViajeros;
    private final Guia guia;
    private final boolean incluyeTransporte;
    private final boolean incluyeSeguro;
    private final String observaciones;
    private String estado;

    public Reserva(ReservaBuilder builder) {
        this.cliente = builder.cliente;
        this.producto = builder.producto;
        this.fechaViaje = builder.fechaViaje;
        this.numViajeros = builder.numViajeros;
        this.guia = builder.guia;
        this.incluyeTransporte = builder.incluyeTransporte;
        this.incluyeSeguro = builder.incluyeSeguro;
        this.observaciones = builder.observaciones;
        this.estado = "PENDIENTE";
    }

    public double calcularValorTotal() {
        double subtotalProducto = producto.getPrecioPorPersona() * numViajeros;

        double costoGuia = 0.0;
        if (guia != null) {
            int duracionTotal = producto.getDuracionHoras();
            int diasGuia = (int) Math.ceil((double) duracionTotal / 8.0);
            costoGuia = diasGuia * guia.getTarifaPorDia();
        }

        double costoTransporte = incluyeTransporte ? (35000.0 * numViajeros) : 0.0;
        double costoSeguro = incluyeSeguro ? (15000.0 * numViajeros) : 0.0;

        return subtotalProducto + costoGuia + costoTransporte + costoSeguro;
    }

    public int getNumeroReserva() { return numeroReserva; }
    public void setNumeroReserva(int numeroReserva) { this.numeroReserva = numeroReserva; }
    public Cliente getCliente() { return cliente; }
    public ProductoTuristico getProducto() { return producto; }
    public LocalDate getFechaViaje() { return fechaViaje; }
    public int getNumViajeros() { return numViajeros; }
    public Guia getGuia() { return guia; }
    public boolean isIncluyeTransporte() { return incluyeTransporte; }
    public boolean isIncluyeSeguro() { return incluyeSeguro; }
    public String getObservaciones() { return observaciones; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}