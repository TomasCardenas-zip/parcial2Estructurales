package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AgenciaRutaVerde {
    // Instancia única (Patrón Singleton)
    private static AgenciaRutaVerde instance;

    private final String nombre = "RutaVerde Quindío";
    private final String nit = "900.123.456-7";
    private final String rnt = "123456";

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
    public void registrarReserva(Reserva r) { reservas.add(r); }

    public String getNombre() { return nombre; }
    public String getNit() { return nit; }
    public String getRnt() { return rnt; }

    public List<Cliente> getClientes() { return Collections.unmodifiableList(clientes); }
    public List<Guia> getGuias() { return Collections.unmodifiableList(guias); }
    public List<ProductoTuristico> getProductos() { return Collections.unmodifiableList(productos); }
    public List<Reserva> getReservas() { return Collections.unmodifiableList(reservas); }
}