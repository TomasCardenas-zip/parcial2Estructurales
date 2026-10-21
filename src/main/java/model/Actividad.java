package model;

import java.util.Collections;
import java.util.List;

public class Actividad extends ProductoTuristico {
    private String municipio;
    private int duracionHoras;
    private double precioPorPersona;
    private int cupoMaximo;
    private NivelDificultad nivelDificultad;
    private EstadoActividad estado;

    public Actividad(String codigo, String nombre, String municipio, int duracionHoras,
                     double precioPorPersona, int cupoMaximo,
                     NivelDificultad nivelDificultad, EstadoActividad estado) {
        super(codigo, nombre);
        this.municipio = municipio;
        this.duracionHoras = duracionHoras;
        this.precioPorPersona = precioPorPersona;
        this.cupoMaximo = cupoMaximo;
        this.nivelDificultad = nivelDificultad;
        this.estado = estado;
    }

    @Override
    public double getPrecioPorPersona() {
        return precioPorPersona;
    }

    @Override
    public int getDuracionHoras() {
        return duracionHoras;
    }

    @Override
    public List<Actividad> getActividades() {
        return Collections.singletonList(this);
    }

    @Override
    public boolean tieneActividadSuspendida() {
        return estado == EstadoActividad.SUSPENDIDA;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public String getMunicipio() {
        return municipio;
    }

    public NivelDificultad getNivelDificultad() {
        return nivelDificultad;
    }

    public EstadoActividad getEstado() {
        return estado;
    }

    public void setEstado(EstadoActividad estado) {
        this.estado = estado;
    }
}