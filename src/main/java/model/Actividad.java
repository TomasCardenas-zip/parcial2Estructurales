package model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Actividad extends ProductoTuristico{
    private String municipio;
    private int duracionHoras;
    private double precioPorPersona;
    private int cupoMaximoPorDia;
    private NivelDificultad dificultad;
    private EstadoActividad estado;
    private final Map<LocalDate, Integer> cuposOcupadosPorFecha;

    public Actividad(String codigo, String nombre, String municipio, int duracionHoras,
                     double precioPorPersona, int cupoMaximoPorDia, NivelDificultad dificultad,
                     EstadoActividad estado) {
        super(codigo, nombre);
        this.municipio = municipio;
        this.duracionHoras = duracionHoras;
        this.precioPorPersona = precioPorPersona;
        this.cupoMaximoPorDia = cupoMaximoPorDia;
        this.dificultad = dificultad;
        this.estado = estado;
        this.cuposOcupadosPorFecha = new HashMap<>();
    }

    @Override
    public double getPrecioPorPersona() { return precioPorPersona; }

    @Override
    public int getDuracionHoras() { return duracionHoras; }

    @Override
    public List<Actividad> getActividades() { return List.of(this); }

    @Override
    public boolean tieneActividadSuspendida() {
        return estado == EstadoActividad.SUSPENDIDA;
    }

    public boolean hayCupoDisponible(LocalDate fecha, int cantidad) {
        int ocupados = cuposOcupadosPorFecha.getOrDefault(fecha, 0);
        return (ocupados + cantidad) <= cupoMaximoPorDia;
    }

    public void descontarCupo(LocalDate fecha, int cantidad) {
        int ocupados = cuposOcupadosPorFecha.getOrDefault(fecha, 0);
        cuposOcupadosPorFecha.put(fecha, ocupados + cantidad);
    }

    public String getMunicipio() { return municipio; }
    public EstadoActividad getEstado() { return estado; }
    public void setEstado(EstadoActividad estado) { this.estado = estado; }
}
