package service;

public class EmailService {

    public void enviarNotificacionFallo(String correoDestino, String motivo) {
        System.out.println("[CORREO ENVIADO A: " + correoDestino + "]");
        System.out.println("Asunto: Estado de su Reserva - Operación Fallida");
        System.out.println("Motivo: " + motivo);
        System.out.println("------------------------------------------------");
    }

    public void enviarVoucher(String correoDestino, String voucher) {
        System.out.println("[CORREO ENVIADO A: " + correoDestino + "]");
        System.out.println("Asunto: Comprobante de Reserva Confirmada");
        System.out.println(voucher);
        System.out.println("------------------------------------------------");
    }
}