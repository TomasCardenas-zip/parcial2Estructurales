package service;

public class ResultadoConfirmacion {
    private final boolean exito;
    private final String mensaje;
    private final String voucher;

    public ResultadoConfirmacion(boolean exito, String mensaje, String voucher) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.voucher = voucher;
    }

    public boolean isExito() { return exito; }
    public String getMensaje() { return mensaje; }
    public String getVoucher() { return voucher; }
}
