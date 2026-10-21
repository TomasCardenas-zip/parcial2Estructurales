package model;

import java.util.List;

public abstract class ProductoTuristico {
    private final String codigo;
    private final String nombre;

    public ProductoTuristico(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }

    // Métodos abstractos del patrón Composite
    public abstract double getPrecioPorPersona();
    public abstract int getDuracionHoras();
    public abstract List<Actividad> getActividades();
    public abstract boolean tieneActividadSuspendida();

    public double getPrecio() {
        return getPrecioPorPersona();
    }

    @Override
    public String toString() {
        return nombre + " (" + codigo + ")";
    }
}