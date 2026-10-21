package model;

import java.time.LocalDate;
import java.util.*;

public class AgenciaRutaVerde {
    // Instancia única (Patrón Singleton)
    private static AgenciaRutaVerde instance;

    private String nombreComercial = "RutaVerde Quindío";
    private String nit = "900.123.456-7";
    private String rnt = "894521";

    private final List<Cliente> clientes = new ArrayList<>();
    private final List<Guia> guias = new ArrayList<>();
    private final List<ProductoTuristico> productos = new ArrayList<>();
    private final List<Reserva> reservas = new ArrayList<>();

    // Constructor privado para evitar instanciación externa
    private AgenciaRutaVerde() {}

    // Punto de acceso global
    public static synchronized AgenciaRutaVerde getInstance() {
        if (instance == null) {
            instance = new AgenciaRutaVerde();
        }
        return instance;
    }

    public void agregarCliente(Cliente c) { clientes.add(c); }
    public void agregarGuia(Guia g) { guias.add(g); }
    public void agregarProducto(ProductoTuristico p) { productos.add(p); }
    public int obtenerCupoDisponible(ProductoTuristico producto, LocalDate fecha) {
        if (producto == null || fecha == null) return 0;

        int cupoDisponibleMinimo = Integer.MAX_VALUE;

        for (Actividad act : producto.getActividades()) {
            int cuposReservados = reservas.stream()
                    .filter(r -> "CONFIRMADA".equals(r.getEstado()))
                    .filter(r -> r.getFechaViaje().equals(fecha))
                    .filter(r -> r.getProducto().getActividades().contains(act))
                    .mapToInt(Reserva::getNumViajeros)
                    .sum();

            int disponible = act.getCupoMaximo() - cuposReservados;
            if (disponible < cupoDisponibleMinimo) {
                cupoDisponibleMinimo = disponible;
            }
        }

        return Math.max(0, cupoDisponibleMinimo);
    }

    public void registrarReserva(Reserva reserva) {
        if (reserva != null && !reservas.contains(reserva)) {
            reservas.add(reserva);
        }
    }


    public String getNombreComercial() {
        return nombreComercial;
    }

    public String getNit() {
        return nit;
    }

    public String getRnt() {
        return rnt;
    }

    public double calcularIngresosEnPeriodo(LocalDate inicio, LocalDate fin) {
        if (inicio == null || fin == null) return 0.0;
        return reservas.stream()
                .filter(r -> "CONFIRMADA".equals(r.getEstado()))
                .filter(r -> !r.getFechaViaje().isBefore(inicio) && !r.getFechaViaje().isAfter(fin))
                .mapToDouble(Reserva::calcularValorTotal)
                .sum();
    }

    public ProductoTuristico obtenerProductoMasPopularEnPeriodo(LocalDate inicio, LocalDate fin) {
        if (inicio == null || fin == null) return null;

        Map<ProductoTuristico, Integer> conteoViajeros = new HashMap<>();

        for (Reserva r : reservas) {
            if ("CONFIRMADA".equals(r.getEstado())
                    && !r.getFechaViaje().isBefore(inicio)
                    && !r.getFechaViaje().isAfter(fin)) {

                ProductoTuristico p = r.getProducto();
                conteoViajeros.put(p, conteoViajeros.getOrDefault(p, 0) + r.getNumViajeros());
            }
        }

        return conteoViajeros.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public List<Cliente> getClientes() { return Collections.unmodifiableList(clientes); }
    public List<Guia> getGuias() { return Collections.unmodifiableList(guias); }
    public List<ProductoTuristico> getProductos() { return Collections.unmodifiableList(productos); }
    public List<Reserva> getReservas() { return Collections.unmodifiableList(reservas); }
}