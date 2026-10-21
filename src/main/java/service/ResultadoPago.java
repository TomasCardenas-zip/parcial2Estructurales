package service;

public class ResultadoPago {
    private final boolean aprobado;
    private final String codigoAutorizacion;
    private final String mensajeError;

    public ResultadoPago(boolean aprobado, String codigoAutorizacion, String mensajeError) {
        this.aprobado = aprobado;
        this.codigoAutorizacion = codigoAutorizacion;
        this.mensajeError = mensajeError;
    }

    public boolean isAprobado() {
        return aprobado; }

    public String getCodigoAutorizacion() {
        return codigoAutorizacion; }

    public String getMensajeError() {
        return mensajeError; }
}
