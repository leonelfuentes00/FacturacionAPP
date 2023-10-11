package Interfaces;

import DBConnection.*;
import Interfaces.Factura.FacturacionApp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class clientes {
    public JPanel pclientes;
    private JButton EMPLEADOSButton;
    private JButton STOCKButton;
    private JButton FACTURASButton;
    private JButton modificarButton;
    private JButton agregarButton;
    private JButton PROVEEDORESButton;
    private JButton CLIENTESButton;
    private JTable TClientes;
    private JButton eliminarButton;
    private JTextField TFID;
    private JTextField TFDNI;
    private JTextField TFNOMBRE;
    private JTextField TFCUIT;
    private JTextField TFDIRECCION;
    private JTextField TFTELEFONO;
    private JTextField TFCORREO;
    private Connect jj;
    private DefaultTableModel clientestablemodel;

    public clientes() {
        clientestablemodel = new DefaultTableModel();
        clientestablemodel.addColumn("ID");
        clientestablemodel.addColumn("NOMBRE");
        clientestablemodel.addColumn("DNI");
        clientestablemodel.addColumn("DIRECCION");
        clientestablemodel.addColumn("TELEFONO");
        clientestablemodel.addColumn("CORREO");
        clientestablemodel.addColumn("FECHA DE REGISTRO");
        clientestablemodel.addColumn("HABILITADO");

        jj = new Connect();

        String sql = "SELECT * FROM cliente";
        try {
            jj.conectar(); // Realiza la conexión a la base de datos

            PreparedStatement ps = jj.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData resul = rs.getMetaData();
            int cantidadColumnas = resul.getColumnCount();

            while (rs.next()) {
                Object[] fila = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                clientestablemodel.addRow(fila);
            }
            TClientes.setModel(clientestablemodel);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

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

        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = TFNOMBRE.getText();
                String dni = TFDNI.getText();
                String direccion = TFDIRECCION.getText();
                String telefono = TFTELEFONO.getText();
                String correo = TFCORREO.getText();
                java.sql.Date fechaRegistro = new java.sql.Date(System.currentTimeMillis());
                int habilitado = 1;

                // Agregar una fila con el ID como null, se llenará automáticamente en la base de datos
                Object[] fila = {null, nombre, dni, direccion, telefono, correo, fechaRegistro, habilitado};

                // Agregar la fila en la tabla visualmente
                clientestablemodel.addRow(fila);

                String sql = "INSERT INTO cliente (nombre, DNI, direccion, telefono, correo, fecha_registro, habilitado) VALUES (?, ?, ?, ?, ?, ?, ?)";
                // Insertar los valores en la base de datos
                try {
                    PreparedStatement ps = jj.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                    ps.setString(1, nombre);
                    ps.setString(2, dni);
                    ps.setString(3, direccion);
                    ps.setString(4, telefono);
                    ps.setString(5, correo);
                    ps.setDate(6, fechaRegistro);
                    ps.setInt(7, habilitado);
                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected == 1) {
                        ResultSet generatedKeys = ps.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            int generatedId = generatedKeys.getInt(1);
                            // Actualizar el ID en la última fila visualizada
                            clientestablemodel.setValueAt(generatedId, clientestablemodel.getRowCount() - 1, 0);
                        }
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        modificarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String idTexto = TFID.getText();
                if (!idTexto.isEmpty()) {
                    // El campo de ID no está vacío, significa que el usuario quiere modificar un registro por ID.
                    int id = Integer.parseInt(idTexto);
                    String nuevoNombre = TFNOMBRE.getText();
                    String nuevoDNI = TFDNI.getText();
                    String nuevaDireccion = TFDIRECCION.getText();
                    String nuevoTelefono = TFTELEFONO.getText();
                    String nuevoCorreo = TFCORREO.getText();

                    int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas modificar este registro por ID?");

                    if (confirmacion == JOptionPane.YES_OPTION) {
                        // Actualizar los datos en la base de datos
                        String sql = "UPDATE cliente SET nombre = ?, DNI = ?, direccion = ?, telefono = ?, correo = ? WHERE id = ?";
                        try {
                            PreparedStatement ps = jj.getConnection().prepareStatement(sql);
                            ps.setString(1, nuevoNombre);
                            ps.setString(2, nuevoDNI);
                            ps.setString(3, nuevaDireccion);
                            ps.setString(4, nuevoTelefono);
                            ps.setString(5, nuevoCorreo);
                            ps.setInt(6, id);
                            ps.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Registro modificado correctamente por ID.");
                            for (int fila = 0; fila < clientestablemodel.getRowCount(); fila++) {
                                if ((int) clientestablemodel.getValueAt(fila, 0) == id) {
                                    clientestablemodel.setValueAt(nuevoNombre, fila, 1);
                                    clientestablemodel.setValueAt(nuevoDNI, fila, 2);
                                    clientestablemodel.setValueAt(nuevaDireccion, fila, 3);
                                    clientestablemodel.setValueAt(nuevoTelefono, fila, 4);
                                    clientestablemodel.setValueAt(nuevoCorreo, fila, 5);
                                    break; // Romper el bucle una vez que se haya encontrado y actualizado la fila.
                                }
                            }
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Seleccione una fila para modificar o ingrese un ID válido.");
                    }
                } else {
                    int filaSeleccionada = TClientes.getSelectedRow();
                    if (filaSeleccionada >= 0) {
                        int id = (int) clientestablemodel.getValueAt(filaSeleccionada, 0);
                        String nuevoNombre = TFNOMBRE.getText();
                        String nuevoDNI = TFDNI.getText();
                        String nuevaDireccion = TFDIRECCION.getText();
                        String nuevoTelefono = TFTELEFONO.getText();
                        String nuevoCorreo = TFCORREO.getText();

                        int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas modificar este registro?");

                        if (confirmacion == JOptionPane.YES_OPTION) {
                            clientestablemodel.setValueAt(nuevoNombre, filaSeleccionada, 1);
                            clientestablemodel.setValueAt(nuevoDNI, filaSeleccionada, 2);
                            clientestablemodel.setValueAt(nuevaDireccion, filaSeleccionada, 3);
                            clientestablemodel.setValueAt(nuevoTelefono, filaSeleccionada, 4);
                            clientestablemodel.setValueAt(nuevoCorreo, filaSeleccionada, 5);

                            // Actualizar los datos en la base de datos
                            String sql = "UPDATE cliente SET nombre = ?, DNI = ?, direccion = ?, telefono = ?, correo = ? WHERE id = ?";
                            try {
                                jj = new Connect();
                                jj.conectar();
                                PreparedStatement ps = jj.getConnection().prepareStatement(sql);
                                ps.setString(1, nuevoNombre);
                                ps.setString(2, nuevoDNI);
                                ps.setString(3, nuevaDireccion);
                                ps.setString(4, nuevoTelefono);
                                ps.setString(5, nuevoCorreo);
                                ps.setInt(6, id);
                                ps.executeUpdate();
                                JOptionPane.showMessageDialog(null, "Registro modificado correctamente.");
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Seleccione una fila para modificar.");
                        }
                    }
                }
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = TClientes.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    int id = (int) clientestablemodel.getValueAt(filaSeleccionada, 0);

                    // Eliminar la fila de la tabla visualmente
                    clientestablemodel.removeRow(filaSeleccionada);
                    TClientes.setModel(clientestablemodel);

                    // Eliminar el registro de la base de datos
                    try {
                        PreparedStatement ps = jj.getConnection().prepareStatement("DELETE FROM cliente WHERE id = ?");
                        ps.setInt(1, id);
                        ps.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Registro eliminado correctamente.");
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione una fila para eliminar.");
                }
            }
        });
    }
    public void setVisibleclientes (boolean b) {
        JFrame frame = new JFrame("Clientes");
        frame.setContentPane((new clientes()).pclientes);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
