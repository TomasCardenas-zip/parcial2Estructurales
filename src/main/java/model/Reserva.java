package model;

import java.time.LocalDate;

public class Reserva {
    private static int contadorConsecutivo = 1;

    private final int consecutivo;
    private Cliente cliente;
    private ProductoTuristico producto;
    private LocalDate fechaViaje;
    private int numViajeros;
    private Guia guia;
    private boolean incluyeTransporte;
    private boolean incluyeSeguro;
    private String observaciones;
    private String estado; // "PENDIENTE", "CONFIRMADA", "CANCELADA"

    public Reserva(Cliente cliente, ProductoTuristico producto, LocalDate fechaViaje,
                   int numViajeros, Guia guia, boolean incluyeTransporte,
                   boolean incluyeSeguro, String observaciones) {
        this.consecutivo = contadorConsecutivo++;
        this.cliente = cliente;
        this.producto = producto;
        this.fechaViaje = fechaViaje;
        this.numViajeros = numViajeros;
        this.guia = guia;
        this.incluyeTransporte = incluyeTransporte;
        this.incluyeSeguro = incluyeSeguro;
        this.observaciones = observaciones;
        this.estado = "PENDIENTE";
    }

    public Reserva(ReservaBuilder reservaBuilder) {
        consecutivo = 0;
    }

    public int getNumeroReserva() {
        return consecutivo;
    }

    public double calcularValorTotal() {
        double valor = producto.getPrecioPorPersona() * numViajeros;
        if (incluyeTransporte) valor += (35000.0 * numViajeros);
        if (incluyeSeguro) valor += (15000.0 * numViajeros);
        if (guia != null) {
            int diasGuia = (int) Math.ceil(producto.getDuracionHoras() / 8.0);
            valor += (guia.getTarifaPorDia() * diasGuia);
        }
        return valor;
    }

    public int getConsecutivo() {
        return consecutivo;
    }
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