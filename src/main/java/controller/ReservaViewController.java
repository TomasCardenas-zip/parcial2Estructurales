package controller; // Si tu paquete es directo, usa: package controller;

import model.*;
import service.CanalEmision;
import service.ResultadoConfirmacion;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class ReservaViewController {

    // Componentes de la Pestaña 1: Nueva Reserva
    @FXML private ComboBox<Cliente> cbClientes;
    @FXML private ComboBox<ProductoTuristico> cbProductos;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtViajeros;
    @FXML private ComboBox<Guia> cbGuias;
    @FXML private ComboBox<CanalEmision> cbCanal;
    @FXML private CheckBox chkTransporte;
    @FXML private CheckBox chkSeguro;
    @FXML private TextField txtObservaciones;
    @FXML private TextField txtTarjeta;
    @FXML private Label lblPrecioEstimado;
    @FXML private TextArea txtVoucher;

    // Componentes de la Pestaña 2: Tabla de Reservas
    @FXML private TableView<Reserva> tablaReservas;
    @FXML private TableColumn<Reserva, Integer> colConsecutivo;
    @FXML private TableColumn<Reserva, String> colCliente;
    @FXML private TableColumn<Reserva, String> colProducto;
    @FXML private TableColumn<Reserva, String> colFecha;
    @FXML private TableColumn<Reserva, Integer> colViajeros;
    @FXML private TableColumn<Reserva, Double> colTotal;
    @FXML private TableColumn<Reserva, String> colEstado;

    // Componentes de la Pestaña 3: Reportes y KPIs
    @FXML private DatePicker dpInicio;
    @FXML private DatePicker dpFin;
    @FXML private Label lblIngresos;
    @FXML private Label lblPopular;

    private ReservaController controller;

    @FXML
    public void initialize() {
        controller = new ReservaController();
        cargarDatosPrueba();

        // 1. Cargar ComboBoxes
        cbClientes.getItems().addAll(controller.obtenerClientes());
        cbProductos.getItems().addAll(controller.obtenerProductos());
        cbGuias.getItems().addAll(controller.obtenerGuias());
        cbCanal.getItems().addAll(CanalEmision.values());
        cbCanal.setValue(CanalEmision.ASESOR);

        // 2. Establecer Fechas por Defecto
        dpFecha.setValue(LocalDate.now().plusDays(2));
        dpInicio.setValue(LocalDate.now().minusDays(1));
        dpFin.setValue(LocalDate.now().plusMonths(2));

        // 3. Configurar UI
        configurarTabla();
        configurarCalculoEnVivo();
        actualizarTablaReservas();

        // 4. Escuchadores para recalculación automática del reporte al cambiar fechas
        dpInicio.valueProperty().addListener((obs, oldVal, newVal) -> recalcularReporteSilencioso());
        dpFin.valueProperty().addListener((obs, oldVal, newVal) -> recalcularReporteSilencioso());

        // 5. Calcular reporte inicial
        recalcularReporteSilencioso();
    }

    private void configurarTabla() {
        colConsecutivo.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getConsecutivo()).asObject());
        colCliente.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getCliente() != null ? d.getValue().getCliente().getNombreCompleto() : "N/A"
        ));
        colProducto.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getProducto() != null ? d.getValue().getProducto().getNombre() : "N/A"
        ));
        colFecha.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getFechaViaje() != null ? d.getValue().getFechaViaje().toString() : ""
        ));
        colViajeros.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getNumViajeros()).asObject());
        colTotal.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().calcularValorTotal()).asObject());
        colEstado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEstado()));
    }

    private void configurarCalculoEnVivo() {
        Runnable actualizarTotal = () -> {
            try {
                ProductoTuristico p = cbProductos.getValue();
                String textoViajeros = txtViajeros.getText();
                if (p != null && textoViajeros != null && !textoViajeros.trim().isEmpty()) {
                    int viajeros = Integer.parseInt(textoViajeros.trim());
                    Guia guia = cbGuias.getValue();
                    boolean trans = chkTransporte.isSelected();
                    boolean seg = chkSeguro.isSelected();

                    if (viajeros > 0) {
                        double total = p.getPrecioPorPersona() * viajeros;
                        if (trans) total += (35000.0 * viajeros);
                        if (seg) total += (15000.0 * viajeros);
                        if (guia != null) {
                            int diasGuia = (int) Math.ceil(p.getDuracionHoras() / 8.0);
                            total += (guia.getTarifaPorDia() * diasGuia);
                        }
                        lblPrecioEstimado.setText("Total Estimado: $" + String.format("%,.2f", total));
                        return;
                    }
                }
            } catch (Exception ignored) {}
            lblPrecioEstimado.setText("Total Estimado: $0.00");
        };

        cbProductos.setOnAction(e -> actualizarTotal.run());
        cbGuias.setOnAction(e -> actualizarTotal.run());
        chkTransporte.setOnAction(e -> actualizarTotal.run());
        chkSeguro.setOnAction(e -> actualizarTotal.run());
        txtViajeros.textProperty().addListener((obs, oldV, newV) -> actualizarTotal.run());
    }

    @FXML
    private void onConfirmarReserva() {
        Cliente cliente = cbClientes.getValue();
        ProductoTuristico producto = cbProductos.getValue();
        LocalDate fecha = dpFecha.getValue();

        try {
            int viajeros = Integer.parseInt(txtViajeros.getText().trim());
            Guia guia = cbGuias.getValue();
            String tarjeta = txtTarjeta.getText();
            CanalEmision canal = cbCanal.getValue();
            String obs = txtObservaciones.getText();

            ResultadoConfirmacion res = controller.procesarReserva(
                    cliente, producto, fecha, viajeros, guia,
                    chkTransporte.isSelected(), chkSeguro.isSelected(),
                    obs, tarjeta, canal
            );

            if (res.isExito()) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Reserva Exitosa", res.getMensaje());
                txtVoucher.setText(res.getVoucher());
                actualizarTablaReservas();
                recalcularReporteSilencioso();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Reserva / Cobro", res.getMensaje());
                txtVoucher.setText("OPERACIÓN FALLIDA / CANCELADA:\n" + res.getMensaje());
            }
        } catch (NumberFormatException ex) {
            mostrarAlerta(Alert.AlertType.ERROR, "Entrada Inválida", "El número de viajeros debe ser un número entero válido.");
        }
    }

    @FXML
    private void onGenerarReporte() {
        LocalDate inicio = dpInicio.getValue();
        LocalDate fin = dpFin.getValue();

        if (inicio == null || fin == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Fechas Incompletas",
                    "Por favor seleccione tanto la fecha inicial como la fecha final.");
            return;
        }

        if (inicio.isAfter(fin)) {
            mostrarAlerta(Alert.AlertType.ERROR, "Rango Inválido",
                    "La fecha inicial (" + inicio + ") no puede ser posterior a la fecha final (" + fin + ").");
            return;
        }

        double ingresos = controller.obtenerIngresos(inicio, fin);
        lblIngresos.setText("$" + String.format("%,.2f", ingresos));

        ProductoTuristico p = controller.obtenerProductoMasPopular(inicio, fin);
        String nombreProducto = (p != null) ? p.getNombre() : "Sin reservas en este periodo";
        lblPopular.setText(nombreProducto);

        // Ventana emergente con los resultados del reporte
        mostrarAlerta(Alert.AlertType.INFORMATION, "Reporte Actualizado",
                "Cálculo realizado con éxito para el periodo:\n" +
                        "Desde: " + inicio + "  |  Hasta: " + fin + "\n\n" +
                        "• Ingresos Totales: $" + String.format("%,.2f", ingresos) + "\n" +
                        "• Producto Más Solicitado: " + nombreProducto);
    }

    private void recalcularReporteSilencioso() {
        LocalDate inicio = dpInicio.getValue();
        LocalDate fin = dpFin.getValue();
        if (inicio != null && fin != null && !inicio.isAfter(fin)) {
            double ingresos = controller.obtenerIngresos(inicio, fin);
            lblIngresos.setText("$" + String.format("%,.2f", ingresos));

            ProductoTuristico p = controller.obtenerProductoMasPopular(inicio, fin);
            lblPopular.setText(p != null ? p.getNombre() : "Sin reservas en este periodo");
        }
    }

    private void actualizarTablaReservas() {
        tablaReservas.setItems(FXCollections.observableArrayList(controller.obtenerReservasConfirmadas()));
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void cargarDatosPrueba() {
        AgenciaRutaVerde agencia = AgenciaRutaVerde.getInstance();
        if (!agencia.getClientes().isEmpty()) return;

        Cliente c1 = new Cliente("1094123456", "Carlos Pérez", "3001234567", "carlos@gmail.com", "Colombia", "Español");
        Cliente c2 = new Cliente("PAS-998877", "John Smith", "3159876543", "john@gmail.com", "EEUU", "Inglés");
        agencia.agregarCliente(c1);
        agencia.agregarCliente(c2);

        Guia g1 = new Guia("G-101", "Ana Gómez", List.of("Español", "Inglés"), "3109876543", 120000.0);
        Guia g2 = new Guia("G-102", "Pedro López", List.of("Español"), "3123456789", 90000.0);
        agencia.agregarGuia(g1);
        agencia.agregarGuia(g2);

        Actividad a1 = new Actividad("ACT-01", "Avistamiento de Aves", "Salento", 4, 80000.0, 10, NivelDificultad.BAJA, EstadoActividad.DISPONIBLE);
        Actividad a2 = new Actividad("ACT-02", "Tour Finca Cafetera", "Calarcá", 5, 60000.0, 15, NivelDificultad.BAJA, EstadoActividad.DISPONIBLE);
        agencia.agregarProducto(a1);
        agencia.agregarProducto(a2);

        PaqueteTuristico paq = new PaqueteTuristico("PAQ-01", "Quindío Total", 10.0);
        paq.agregarProducto(a1);
        paq.agregarProducto(a2);
        agencia.agregarProducto(paq);
    }
}