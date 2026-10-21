package model;

import java.util.List;

public class Guia {
    private String identificacion;
    private String nombre;
    private List<String> idiomas;
    private String telefono;
    private double tarifaPorDia;

    public Guia(String identificacion, String nombre, List<String> idiomas, String telefono, double tarifaPorDia) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.idiomas = idiomas;
        this.telefono = telefono;
        this.tarifaPorDia = tarifaPorDia;
    }

    public boolean hablaIdioma(String idioma) {
        return idiomas.stream().anyMatch(i -> i.equalsIgnoreCase(idioma));
    }

    public String getIdentificacion() { return identificacion; }
    public String getNombre() { return nombre; }
    public List<String> getIdiomas() { return idiomas; }
    public String getTelefono() { return telefono; }
    public double getTarifaPorDia() { return tarifaPorDia; }

    @Override
    public String toString() {
        return nombre + " - $" + tarifaPorDia + "/día";
    }
}