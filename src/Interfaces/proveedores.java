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

public class proveedores {
    private JPanel pproveedores;
    private JButton EMPLEADOSButton;
    private JButton STOCKButton;
    private JButton PROVEEDORESButton1;
    private JButton FACTURASButton;
    private JButton CLIENTESButton1;
    private JTable tableProveedor;
    private JButton agregarButton;
    private JButton eliminarButton;
    private JButton modificarButton;
    private JTextField textFieldIDproveedor;
    private JTextField textFieldNOMBREProveedor;
    private JTextField textFieldDNIProveedor;
    private JTextField textFieldDIRECCIONProveedor;
    private JTextField textFieldTIPOPROVEEDOR;
    private JTextField textFieldCORREOproveedor;
    private JTextField textFieldTELEFONOproveedor;
    private Connect jj;
    private DefaultTableModel proveedoresTableModel;

    public void setVisibleproveedores(boolean b) {
        JFrame frame = new JFrame("Proveedores");
        frame.setContentPane(this.pproveedores);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public proveedores() {
        jj = new Connect();

        proveedoresTableModel = new DefaultTableModel();
        proveedoresTableModel.addColumn("ID");
        proveedoresTableModel.addColumn("NOMBRE");
        proveedoresTableModel.addColumn("DNI");
        proveedoresTableModel.addColumn("DIRECCION");
        proveedoresTableModel.addColumn("TELEFONO");
        proveedoresTableModel.addColumn("CORREO");
        proveedoresTableModel.addColumn("FECHA REGISTRO");
        proveedoresTableModel.addColumn("ID TIPO PROVEEDOR");
        proveedoresTableModel.addColumn("HABILITADO");

        String sql = "SELECT * FROM proveedores";

        try {
            jj.conectar();

            PreparedStatement ps = jj.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData resul = rs.getMetaData();
            int cantidadColumnas = resul.getColumnCount();

            while (rs.next()) {
                Object[] fila = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                proveedoresTableModel.addRow(fila);

                int idTipoProveedor = (int) fila[7];
                String nombreTipoProveedor = jj.getNombreTipoProveedorPorID(idTipoProveedor);
                proveedoresTableModel.setValueAt(nombreTipoProveedor, proveedoresTableModel.getRowCount() - 1, 8);
            }
            tableProveedor.setModel(proveedoresTableModel);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProveedor();
            }
        });

        modificarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarProveedor();
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarProveedor();
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

    private void agregarProveedor() {
        String nuevoNombre = textFieldNOMBREProveedor.getText();
        String nuevoDNI = textFieldDNIProveedor.getText();
        String nuevaDireccion = textFieldDIRECCIONProveedor.getText();
        String nuevoTelefono = textFieldTELEFONOproveedor.getText();
        String nuevoCorreo = textFieldCORREOproveedor.getText();
        int id_tipo_proveedor = Integer.parseInt(textFieldTIPOPROVEEDOR.getText());
        java.sql.Date fechaRegistro = new java.sql.Date(System.currentTimeMillis());
        int habilitado = 1;

        DefaultTableModel model = (DefaultTableModel) tableProveedor.getModel();
        Object[] fila = {null, nuevoNombre, nuevoDNI, nuevaDireccion, nuevoTelefono, nuevoCorreo, fechaRegistro, id_tipo_proveedor, habilitado};

        model.addRow(fila);

        String sql = "INSERT INTO proveedores (nombre, DNI, direccion, telefono, correo, fecha_registro, id_tipo_proveedor, habilitado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            jj.conectar();
            PreparedStatement ps = jj.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, nuevoNombre);
            ps.setString(2, nuevoDNI);
            ps.setString(3, nuevaDireccion);
            ps.setString(4, nuevoTelefono);
            ps.setString(5, nuevoCorreo);
            ps.setDate(6, fechaRegistro);
            ps.setInt(7, id_tipo_proveedor);
            ps.setInt(8, habilitado);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 1) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    model.setValueAt(generatedId, model.getRowCount() - 1, 0); // Actualizar el ID en la última fila visualizada
                }
            }
            JOptionPane.showMessageDialog(null, "Registro agregado correctamente.");
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void modificarProveedor() {
        int SelectedRow = tableProveedor.getSelectedRow();
        if (SelectedRow >= 0) {
            int id = (int) proveedoresTableModel.getValueAt(SelectedRow, 0);
            String nuevoNombre = textFieldNOMBREProveedor.getText();
            String nuevoDNI = textFieldDNIProveedor.getText();
            String nuevaDireccion = textFieldDIRECCIONProveedor.getText();
            String nuevoTelefono = textFieldTELEFONOproveedor.getText();
            String nuevoCorreo = textFieldCORREOproveedor.getText(); // Agregar el campo de correo

            int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas modificar este registro?");
            if (confirmacion == JOptionPane.YES_OPTION) {
                proveedoresTableModel.setValueAt(nuevoNombre, SelectedRow, 1);
                proveedoresTableModel.setValueAt(nuevoDNI, SelectedRow, 2);
                proveedoresTableModel.setValueAt(nuevaDireccion, SelectedRow, 3);
                proveedoresTableModel.setValueAt(nuevoTelefono, SelectedRow, 4);
                proveedoresTableModel.setValueAt(nuevoCorreo, SelectedRow, 5); // Actualizar la columna de correo

                String sql = "UPDATE proveedores SET nombre = ?, DNI = ?, direccion = ?, telefono = ?, correo = ? WHERE id = ?";
                try {
                    jj.conectar();
                    PreparedStatement ps = jj.getConnection().prepareStatement(sql);
                    ps.setString(1, nuevoNombre);
                    ps.setString(2, nuevoDNI);
                    ps.setString(3, nuevaDireccion);
                    ps.setString(4, nuevoTelefono);
                    ps.setString(5, nuevoCorreo);
                    int nuevoIdTipoProveedor = Integer.parseInt(textFieldTIPOPROVEEDOR.getText());
                    ps.setInt(6, id);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Proveedor modificado correctamente.");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione una fila para modificar.");
            }
        }
    }


    private void eliminarProveedor() {
        int filaSeleccionada = tableProveedor.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int id = (int) proveedoresTableModel.getValueAt(filaSeleccionada, 0);
            int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar este registro?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                proveedoresTableModel.removeRow(filaSeleccionada);

                String sql = "DELETE FROM proveedores WHERE id = ?";
                try {
                    jj.conectar();
                    PreparedStatement ps = jj.getConnection().prepareStatement(sql);
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Registro eliminado correctamente.");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una fila para eliminar.");
        }
    }
}
