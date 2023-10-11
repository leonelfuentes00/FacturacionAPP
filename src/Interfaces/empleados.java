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

public class empleados {
    private JPanel pempleados;
    private JButton EMPLEADOSButton;
    private JButton STOCKButton;
    private JButton PROVEEDORESButton1;
    private JButton FACTURASButton;
    private JButton CLIENTESButton1;
    private JTable tableEmpleados;
    private JButton agregarButton;
    private JButton eliminarButton;
    private JButton modificarButton;
    private JTextField textFieldID;
    private JTextField textFieldLEGAJO;
    private JTextField textFieldNOMBRE;
    private JTextField textFieldDNI;
    private JTextField textFieldDIRECCION;
    private JTextField textfieldTELEFONO;
    private JTextField textfieldCORREO;
    private JTextField textFieldTIPOEMPLEADO;
    private DefaultTableModel empleadosTableModel;
    private Connect jj;


    public void setVisibleempleados(boolean b) {
        JFrame frame = new JFrame("Empleados");
        frame.setContentPane(this.pempleados); // Usamos la instancia actual
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public empleados() {
        Connect jj = new Connect();

        empleadosTableModel = new DefaultTableModel();
        empleadosTableModel.addColumn("ID");
        empleadosTableModel.addColumn("NOMBRE");
        empleadosTableModel.addColumn("DNI");
        empleadosTableModel.addColumn("DIRECCION");
        empleadosTableModel.addColumn("TELEFONO");
        empleadosTableModel.addColumn("CORREO");
        empleadosTableModel.addColumn("LEGAJO");
        empleadosTableModel.addColumn("FECHA DE REGISTRO");
        empleadosTableModel.addColumn("NOMBRE DE TIPO EMPLEADO");
        empleadosTableModel.addColumn("HABILITADO");

        String sql = "SELECT * FROM empleado";

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
                empleadosTableModel.addRow(fila);

                int idTipoEmpleado = (int) fila[8];
                String nombreTipoEmpleado = jj.getNombreTipoEmpleadoPorID(idTipoEmpleado);
                empleadosTableModel.setValueAt(nombreTipoEmpleado, empleadosTableModel.getRowCount() - 1, 9);
            }
            tableEmpleados.setModel(empleadosTableModel);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = textFieldNOMBRE.getText();
                String dni = textFieldDNI.getText();
                String direccion = textFieldDIRECCION.getText();
                String telefono = textfieldTELEFONO.getText();
                String correo = textfieldCORREO.getText();
                String legajo = textFieldLEGAJO.getText();
                int id_tipo_empleado = Integer.parseInt(textFieldTIPOEMPLEADO.getText());
                java.sql.Date fechaRegistro = new java.sql.Date(System.currentTimeMillis());
                int habilitado = 1;

                DefaultTableModel model = (DefaultTableModel) tableEmpleados.getModel();
                Object[] fila = {null, nombre, dni, direccion, telefono, correo, legajo, fechaRegistro, null, habilitado};
                model.addRow(fila);

                // Insertar los datos en la base de datos
                String sql = "INSERT INTO empleado (nombre, DNI, direccion, telefono, correo, legajo, fecha_registro, id_tipo_empleado, habilitado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try {
                    jj.conectar();
                    PreparedStatement ps = jj.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                    ps.setString(1, nombre);
                    ps.setString(2, dni);
                    ps.setString(3, direccion);
                    ps.setString(4, telefono);
                    ps.setString(5, correo);
                    ps.setString(6, legajo);
                    ps.setDate(7, fechaRegistro);
                    ps.setInt(8, id_tipo_empleado);
                    ps.setInt(9, habilitado);
                    int rowsAffected = ps.executeUpdate();

                    if (rowsAffected == 1) {
                        ResultSet generatedKeys = ps.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            int generatedId = generatedKeys.getInt(1);
                            // Actualizar el ID en la última fila visualizada
                            model.setValueAt(generatedId, model.getRowCount() - 1, 0);

                            // Obtener el nombre del empleado por su ID
                            String nombreEmpleado = jj.getNombreEmpleadoPorID(generatedId);

                            // Obtener el tipo de empleado por su ID
                            String nombreTipoEmpleado = jj.getNombreTipoEmpleadoPorID(id_tipo_empleado); // Usar el id_tipo_empleado que obtuviste
                            model.setValueAt(nombreEmpleado, model.getRowCount() - 1, 8); // Actualizar la columna "NOMBRE DE EMPLEADO" en la tabla visual
                            model.setValueAt(nombreTipoEmpleado, model.getRowCount() - 1, 9); // Actualizar la columna "NOMBRE DE TIPO EMPLEADO" en la tabla visual
                        }
                    }
                    JOptionPane.showMessageDialog(null, "Registro agregado correctamente.");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        modificarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idTexto = textFieldID.getText();
                if (!idTexto.isEmpty()) {
                    // El campo de ID no está vacío, significa que el usuario quiere modificar un registro por ID.
                    int id = Integer.parseInt(idTexto);
                    String nuevoNombre = textFieldNOMBRE.getText();
                    String nuevoDNI = textFieldDNI.getText();
                    String nuevaDireccion = textFieldDIRECCION.getText();
                    String nuevoTelefono = textfieldTELEFONO.getText();
                    String nuevoCorreo = textfieldCORREO.getText();
                    String nuevoLegajo = textFieldLEGAJO.getText();
                    int nuevoIdTipoEmpleado = Integer.parseInt(textFieldTIPOEMPLEADO.getText());

                    int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas modificar este registro por ID?");

                    if (confirmacion == JOptionPane.YES_OPTION) {
                        // Actualizar los datos en la base de datos
                        String sql = "UPDATE empleado SET nombre = ?, DNI = ?, direccion = ?, telefono = ?, correo = ?, legajo = ?, id_tipo_empleado = ? WHERE id = ?";
                        try {
                            jj.conectar();
                            PreparedStatement ps = jj.getConnection().prepareStatement(sql);
                            ps.setString(1, nuevoNombre);
                            ps.setString(2, nuevoDNI);
                            ps.setString(3, nuevaDireccion);
                            ps.setString(4, nuevoTelefono);
                            ps.setString(5, nuevoCorreo);
                            ps.setString(6, nuevoLegajo);
                            ps.setInt(7, nuevoIdTipoEmpleado);
                            ps.setInt(8, id);
                            ps.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Registro modificado correctamente por ID.");
                            for (int fila = 0; fila < empleadosTableModel.getRowCount(); fila++) {
                                if ((int) empleadosTableModel.getValueAt(fila, 0) == id) {
                                    empleadosTableModel.setValueAt(nuevoNombre, fila, 1);
                                    empleadosTableModel.setValueAt(nuevoDNI, fila, 2);
                                    empleadosTableModel.setValueAt(nuevaDireccion, fila, 3);
                                    empleadosTableModel.setValueAt(nuevoTelefono, fila, 4);
                                    empleadosTableModel.setValueAt(nuevoCorreo, fila, 5);
                                    empleadosTableModel.setValueAt(nuevoLegajo, fila, 6);
                                    empleadosTableModel.setValueAt(nuevoIdTipoEmpleado, fila, 8);
                                    break; // Romper el bucle una vez que se haya encontrado y actualizado la fila.
                                }
                            }
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Ingrese un ID válido o seleccione una fila para modificar.");
                    }
                } else {
                    int filaSeleccionada = tableEmpleados.getSelectedRow();
                    if (filaSeleccionada >= 0) {
                        int id = (int) empleadosTableModel.getValueAt(filaSeleccionada, 0);
                        String nuevoNombre = textFieldNOMBRE.getText();
                        String nuevoDNI = textFieldDNI.getText();
                        String nuevaDireccion = textFieldDIRECCION.getText();
                        String nuevoTelefono = textfieldTELEFONO.getText();
                        String nuevoCorreo = textfieldCORREO.getText();
                        String nuevoLegajo = textFieldLEGAJO.getText();
                        int nuevoIdTipoEmpleado = Integer.parseInt(textFieldTIPOEMPLEADO.getText());

                        int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas modificar este registro?");

                        if (confirmacion == JOptionPane.YES_OPTION) {
                            empleadosTableModel.setValueAt(nuevoNombre, filaSeleccionada, 1);
                            empleadosTableModel.setValueAt(nuevoDNI, filaSeleccionada, 2);
                            empleadosTableModel.setValueAt(nuevaDireccion, filaSeleccionada, 3);
                            empleadosTableModel.setValueAt(nuevoTelefono, filaSeleccionada, 4);
                            empleadosTableModel.setValueAt(nuevoCorreo, filaSeleccionada, 5);
                            empleadosTableModel.setValueAt(nuevoLegajo, filaSeleccionada, 6);
                            empleadosTableModel.setValueAt(nuevoIdTipoEmpleado, filaSeleccionada, 8);

                            // Actualizar los datos en la base de datos
                            String sql = "UPDATE empleado SET nombre = ?, DNI = ?, direccion = ?, telefono = ?, correo = ?, legajo = ?, id_tipo_empleado = ? WHERE id = ?";
                            try {
                                jj.conectar();
                                PreparedStatement ps = jj.getConnection().prepareStatement(sql);
                                ps.setString(1, nuevoNombre);
                                ps.setString(2, nuevoDNI);
                                ps.setString(3, nuevaDireccion);
                                ps.setString(4, nuevoTelefono);
                                ps.setString(5, nuevoCorreo);
                                ps.setString(6, nuevoLegajo);
                                ps.setInt(7, nuevoIdTipoEmpleado);
                                ps.setInt(8, id);
                                ps.executeUpdate();
                                JOptionPane.showMessageDialog(null, "Registro modificado correctamente.");
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Seleccione una fila para modificar.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Seleccione una fila para modificar.");
                    }
                }
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = tableEmpleados.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    int id = (int) empleadosTableModel.getValueAt(filaSeleccionada, 0);

                    // Eliminar la fila de la tabla visualmente
                    empleadosTableModel.removeRow(filaSeleccionada);

                    // Eliminar el registro de la base de datos
                    String sql = "DELETE FROM empleado WHERE id = ?";
                    try {
                        jj.conectar();
                        PreparedStatement ps = jj.getConnection().prepareStatement(sql);
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
        CLIENTESButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientes pclientes = new clientes();
                pclientes.setVisibleclientes(true);
            }
        });
        PROVEEDORESButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                proveedores pproveedores = new proveedores();
                pproveedores.setVisibleproveedores(true);
            }
        });
    }
}
