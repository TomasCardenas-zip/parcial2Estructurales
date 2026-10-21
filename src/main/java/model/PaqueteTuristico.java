package model;

import java.util.ArrayList;
import java.util.List;

public class PaqueteTuristico extends ProductoTuristico {
    private double porcentajeDescuento;
    private final List<ProductoTuristico> productos = new ArrayList<>();

    public PaqueteTuristico(String codigo, String nombre, double porcentajeDescuento) {
        super(codigo, nombre);
        setPorcentajeDescuento(porcentajeDescuento);
    }

    public void setPorcentajeDescuento(double porcentajeDescuento) {
        if (porcentajeDescuento < 0 || porcentajeDescuento > 20) {
            throw new IllegalArgumentException("El porcentaje de descuento debe estar entre 0% y 20%.");
        }
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public void agregarProducto(ProductoTuristico producto) {
        if (producto == null) return;
        if (producto == this || contienePaquete(producto, this)) {
            throw new IllegalArgumentException("Un paquete no puede contenerse a sí mismo.");
        }

        List<Actividad> actuales = getActividades();
        List<Actividad> nuevas = producto.getActividades();
        for (Actividad a : nuevas) {
            if (actuales.contains(a)) {
                throw new IllegalArgumentException("El plan no puede incluir dos veces la misma actividad: " + a.getNombre());
            }
        }
        productos.add(producto);
    }

    private boolean contienePaquete(ProductoTuristico actual, PaqueteTuristico objetivo) {
        if (actual == objetivo) return true;
        if (actual instanceof PaqueteTuristico paq) {
            for (ProductoTuristico p : paq.getProductos()) {
                if (contienePaquete(p, objetivo)) return true;
            }
        }
        return false;
    }

    @Override
    public double getPrecioPorPersona() {
        double suma = productos.stream().mapToDouble(ProductoTuristico::getPrecioPorPersona).sum();
        return suma * (1.0 - (porcentajeDescuento / 100.0));
    }

    @Override
    public int getDuracionHoras() {
        return productos.stream().mapToInt(ProductoTuristico::getDuracionHoras).sum();
    }

    @Override
    public List<Actividad> getActividades() {
        List<Actividad> lista = new ArrayList<>();
        for (ProductoTuristico p : productos) {
            lista.addAll(p.getActividades());
        }
        return lista;
    }

    @Override
    public boolean tieneActividadSuspendida() {
        return productos.stream().anyMatch(ProductoTuristico::tieneActividadSuspendida);
    }

    public List<ProductoTuristico> getProductos() { return productos; }
    public double getPorcentajeDescuento() { return porcentajeDescuento; }
}