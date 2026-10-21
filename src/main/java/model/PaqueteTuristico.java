package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PaqueteTuristico extends ProductoTuristico {
    private double porcentajeDescuento;
    private final List<ProductoTuristico> items;

    public PaqueteTuristico(String codigo, String nombre, double porcentajeDescuento) {
        super(codigo, nombre);
        if (porcentajeDescuento < 0.0 || porcentajeDescuento > 20.0) {
            throw new IllegalArgumentException("El descuento debe estar entre 0% y 20%");
        }
        this.porcentajeDescuento = porcentajeDescuento;
        this.items = new ArrayList<>();
    }

    public void agregarProducto(ProductoTuristico producto) {
        if (producto == this) {
            throw new IllegalArgumentException("Un paquete no puede contenerse a sí mismo.");
        }
        if (producto instanceof PaqueteTuristico p && p.contienePaqueteRecursivo(this)) {
            throw new IllegalArgumentException("Referencia circular detectada entre paquetes.");
        }

        List<Actividad> nuevasActividades = producto.getActividades();
        List<Actividad> actividadesExistentes = this.getActividades();

        for (Actividad nueva : nuevasActividades) {
            for (Actividad existente : actividadesExistentes) {
                if (existente.getCodigo().equalsIgnoreCase(nueva.getCodigo())) {
                    throw new IllegalArgumentException("El paquete ya incluye la actividad: " + nueva.getNombre());
                }
            }
        }
        items.add(producto);
    }

    private boolean contienePaqueteRecursivo(PaqueteTuristico objetivo) {
        for (ProductoTuristico p : items) {
            if (p == objetivo) return true;
            if (p instanceof PaqueteTuristico pt && pt.contienePaqueteRecursivo(objetivo)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double getPrecioPorPersona() {
        double sumaSubproductos = items.stream().mapToDouble(ProductoTuristico::getPrecioPorPersona).sum();
        return sumaSubproductos * (1.0 - (porcentajeDescuento / 100.0));
    }

    @Override
    public int getDuracionHoras() {
        return items.stream().mapToInt(ProductoTuristico::getDuracionHoras).sum();
    }

    @Override
    public List<Actividad> getActividades() {
        List<Actividad> lista = new ArrayList<>();
        for (ProductoTuristico item : items) {
            lista.addAll(item.getActividades());
        }
        return lista;
    }

    @Override
    public boolean tieneActividadSuspendida() {
        return items.stream().anyMatch(ProductoTuristico::tieneActividadSuspendida);
    }

    public double getPorcentajeDescuento() { return porcentajeDescuento; }
    public List<ProductoTuristico> getItems() { return Collections.unmodifiableList(items); }
}