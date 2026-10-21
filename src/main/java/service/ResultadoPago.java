package service;

public class ResultadoPago {
    private final boolean aprobado;
    private final String mensaje;

    public ResultadoPago(boolean aprobado, String mensaje) {
        this.aprobado = aprobado;
        this.mensaje = mensaje;
    }

    public boolean isAprobado() { return aprobado; }
    public String getMensaje() { return mensaje; }
}