package Interfaces.Factura;

import AutocompleteFields.*;
import DBConnection.*;
import ExceptionsManage.StockInsuficienteException;
import Interfaces.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturacionApp {
    private JPanel pfacturacion;
    private JButton EMPLEADOSButton;
    private JButton STOCKButton;
    private JButton FACTURASButton;
    private JTable TFacturacionDetalle;
    private JTable TFacturacionEncabezado;
    private JButton FACTURALButton;
    private JButton CLIENTESButton;
    private JButton PROVEEDORESButton;
    private JTextField TFCANTIDAD;
    private JTextField TFCLIENTE;
    private JTextField TFPRODUCTO;
    private JTextField TFSUBTOTAL;
    private JTextField TFDESCUENTOTOT;
    private JTextField TFTOTAL;
    private JButton agregarButton;
    private JButton eliminarButton;
    private JTextField TFEMPLEADO;
    private JButton FINALIZARButton;
    private JTextField TFCODIGO;
    private FacturacionDB facturacionDB;
    private Connect Connect;
    private DefaultTableModel encabezadoTableModel;
    private DefaultTableModel detalleTableModel;
    private AutoCompletar autoCompletar;
    private List<String> clienteSuggestionsList;
    private static int idFacturaGenerado = -1;
    private static double subtotalTotal = 0.0;
    private static int descuento = 0;

    private int idProducto;
    private double total;


    public FacturacionApp() throws SQLException {
        detalleTableModel = new DefaultTableModel();
        detalleTableModel.addColumn("CLIENTE");
        detalleTableModel.addColumn("EMPLEADO");
        detalleTableModel.addColumn("PRODUCTO");
        detalleTableModel.addColumn("PRECIO");
        detalleTableModel.addColumn("CANTIDAD");
        detalleTableModel.addColumn("SUBTOTAL");
        detalleTableModel.addColumn("DESCUENTO");
        detalleTableModel.addColumn("TOTAL");

        encabezadoTableModel = new DefaultTableModel();
        encabezadoTableModel.addColumn("ID");
        encabezadoTableModel.addColumn("ID_FACTURA");
        encabezadoTableModel.addColumn("ID_PRODUCTO");
        encabezadoTableModel.addColumn("CANTIDAD");
        encabezadoTableModel.addColumn("PRECIO_UNITARIO");
        encabezadoTableModel.addColumn("SUBTOTAL");

        facturacionDB = new FacturacionDB();
        Connect = new Connect();
        autoCompletar = new AutoCompletar();
        Connect.conectar();


        clienteSuggestionsList = new ArrayList<>();
        autoCompletar.agregarAutoCompletado(TFPRODUCTO, autoCompletar.obtenerNombresProductosDesdeBD());

        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nombreCliente = TFCLIENTE.getText();
                    String nombreVendedor = TFEMPLEADO.getText();
                    String productoBuscado = TFPRODUCTO.getText();
                    String codigoProducto = TFCODIGO.getText();
                    int cantidad = Integer.parseInt(TFCANTIDAD.getText());

                    if (nombreCliente.isEmpty()) {
                        nombreCliente = obtenerValorAnonimo("cliente");
                        if (nombreCliente == null) {
                            return;
                        }
                    }
                    if (nombreVendedor.isEmpty()) {
                        nombreVendedor = obtenerValorAnonimo("vendedor");
                        if (nombreVendedor == null) {
                            return;
                        }
                    }

                    int idCliente = Connect.getIDCliente(nombreCliente);
                    int idEmpleado = Connect.getIDEmpleado(nombreVendedor);

                    double precioProducto = 0.0;
                    String nombreProducto = "";

                    if (!codigoProducto.isEmpty()) {
                        int idProducto = Integer.parseInt(codigoProducto);
                        precioProducto = Connect.getPrecioProductoPorId(idProducto);
                        nombreProducto = Connect.getNombreProductoPorId(idProducto);
                    } else if (!productoBuscado.isEmpty()) {
                        int idProducto = Connect.getIDProducto(productoBuscado);
                        precioProducto = Connect.getPrecioProductoPorId(idProducto);
                        nombreProducto = productoBuscado; // Utilizar el nombre ingresado.
                    }

                    if (precioProducto == 0.0) {
                        int respuesta = JOptionPane.showConfirmDialog(
                                null,
                                "El producto no se encuentra en la base de datos. ¿Desea cargarlo de todos modos?",
                                "Producto no encontrado",
                                JOptionPane.YES_NO_OPTION
                        );

                        if (respuesta == JOptionPane.YES_OPTION) {
                            String precioStr = JOptionPane.showInputDialog("Ingrese el precio del producto:");

                            try {
                                precioProducto = Double.parseDouble(precioStr);
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(null, "Ingrese un precio válido.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        } else {
                            return;
                        }
                    }

                    double subtotal = precioProducto * cantidad;
                    subtotalTotal += subtotal;

                    TFSUBTOTAL.setText(String.valueOf(subtotalTotal));

                    Object[] fila = new Object[8];
                    fila[0] = nombreCliente;
                    fila[1] = nombreVendedor;
                    fila[2] = !codigoProducto.isEmpty() ? nombreProducto : productoBuscado;
                    fila[3] = precioProducto;
                    fila[4] = cantidad;
                    fila[5] = subtotal;
                    fila[6] = descuento;
                    fila[7] = total;

                    detalleTableModel.addRow(fila);
                    TFacturacionDetalle.setModel(detalleTableModel);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Ingrese valores numéricos válidos en los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = TFacturacionDetalle.getSelectedRow();

                if (filaSeleccionada != -1) {
                    Object subtotalObject = TFacturacionDetalle.getValueAt(filaSeleccionada, 5);
                    double subtotalEliminar = Double.parseDouble(String.valueOf(subtotalObject));

                    subtotalTotal -= subtotalEliminar;
                    TFSUBTOTAL.setText(String.valueOf(subtotalTotal));

                    detalleTableModel.removeRow(filaSeleccionada);
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione una fila para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        FACTURALButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double descuento = 0;
                    String descuentoStr = TFDESCUENTOTOT.getText();
                    if (!descuentoStr.isEmpty()) {
                        descuento = Double.parseDouble(descuentoStr);
                    }
                    double total = subtotalTotal * (1 - (descuento / 100));
                    TFTOTAL.setText(String.valueOf(total));

                    List<DetalleFactura> detallesFactura = new ArrayList<>();
                    int idEmpleado = 0;
                    int idCliente = 0;
                    for (int i = 0; i < detalleTableModel.getRowCount(); i++) {
                        String nombreCliente = String.valueOf(detalleTableModel.getValueAt(i, 0));
                        String nombreVendedor = String.valueOf(detalleTableModel.getValueAt(i, 1));
                        String nombreProducto = String.valueOf(detalleTableModel.getValueAt(i, 2));
                        double precioProducto = Double.parseDouble(String.valueOf(detalleTableModel.getValueAt(i, 3)));
                        int cantidad = Integer.parseInt(String.valueOf(detalleTableModel.getValueAt(i, 4)));
                        double subtotal = Double.parseDouble(String.valueOf(detalleTableModel.getValueAt(i, 5)));

                        idCliente = Connect.getIDCliente(nombreCliente);
                        idEmpleado = Connect.getIDEmpleado(nombreVendedor);
                        int idProducto = Connect.getIDProducto(nombreProducto);

                        DetalleFactura detalle = new DetalleFactura(idCliente, idEmpleado, idProducto, cantidad, precioProducto, subtotal);
                        detallesFactura.add(detalle);
                    }

                    for (DetalleFactura detalle : detallesFactura) {
                        double descuentoProducto = detalle.getSubtotal() * (descuento / 100);
                        detalle.setDescuento(descuentoProducto);
                        detalle.setTotal(detalle.getSubtotal() - descuentoProducto);
                    }

                    idFacturaGenerado = facturacionDB.insertarFacturaEncabezado(idCliente, idEmpleado, subtotalTotal, descuento, total);
                    facturacionDB.insertarDetalleFactura(detallesFactura, idFacturaGenerado);

                    JOptionPane.showMessageDialog(null, "Factura guardada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Ingrese un descuento válido.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        FINALIZARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirmacion = JOptionPane.showConfirmDialog(null, "¿Desea continuar realizando otras facturas?", "Confirmar", JOptionPane.YES_NO_OPTION);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    limpiarCampos();
                } else {
                    System.exit(0);
                }
            }
        });
        STOCKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stock pstock = new stock();
                pstock.setVisiblestock(true);
            }
        });
        FACTURASButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FacturacionApp pfacturacion = null;
                try {
                    pfacturacion = new FacturacionApp();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                pfacturacion.setVisiblefacturacion(true);
            }
        });
        EMPLEADOSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                empleados pempleados = new empleados();
                pempleados.setVisibleempleados(true);
            }
        });
        CLIENTESButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientes pclientes = new clientes();
                pclientes.setVisibleclientes(true);
            }
        });
        PROVEEDORESButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                proveedores pproveedores = new proveedores();
                pproveedores.setVisibleproveedores(true);
            }
        });
    }
    private void limpiarCampos() {
        TFCLIENTE.setText("");
        TFEMPLEADO.setText("");
        TFPRODUCTO.setText("");
        TFCANTIDAD.setText("");
        TFSUBTOTAL.setText("");
        TFDESCUENTOTOT.setText("");
        TFTOTAL.setText("");
        detalleTableModel.setRowCount(0);
    }
    private boolean gestionarStock(int idProducto, int cantidad) throws StockInsuficienteException, SQLException {
        try {
            int stockActual = Connect.getStockProducto(idProducto);

            if (stockActual >= cantidad) {
                int nuevoStock = stockActual - cantidad;
                boolean actualizacionExitosa = Connect.actualizarStockProducto(idProducto, nuevoStock);

                if (!actualizacionExitosa) {
                    throw new StockInsuficienteException("Error al actualizar el stock en la base de datos.", stockActual);
                }

                return true;
            } else {
                throw new StockInsuficienteException("No hay suficiente stock disponible. Stock actual: " + stockActual, stockActual);
            }
        } catch (SQLException ex) {
            throw new StockInsuficienteException("Error en la base de datos: " + ex.getMessage(), 0);
        }
    }
    private String obtenerValorAnonimo(String tipo) {
        String mensaje = "El campo de " + tipo + " está vacío. ¿Desea usar un valor anónimo?";
        int opcion = JOptionPane.showConfirmDialog(null, mensaje, "Campo vacío", JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            return "anonimo";
        }
        return null;
    }
    public void setVisiblefacturacion(boolean b) {
        JFrame frame = new JFrame("Facturacion");

        frame.setContentPane(this.pfacturacion);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                FacturacionApp app = null;
                try {
                    app = new FacturacionApp();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                app.setVisiblefacturacion(true);
            }
        });
    }
}
