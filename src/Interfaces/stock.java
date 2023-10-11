package Interfaces;

import DBConnection.*;
import Interfaces.Factura.FacturacionApp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.*;

public class stock {
    public JPanel panel1;
    public JPanel pstock;
    public JButton PROVEEDORESButton;
    public JButton CLIENTESButton;
    private JButton agregarButton;
    private JButton modificarButton;
    private JButton eliminarButton;
    private JTextField TFID;
    private JTextField TFNOMBRE;
    private JTextField TFPRECIO;
    private JButton STOCKButton;
    private JButton FACTURASButton;
    private JButton EMPLEADOSButton;
    private JTable TSTOCK;
    private JTextField TFCANTIDAD;
    private JTextField TFPROVEEDOR;
    private JTextField TFDETALLE;
    private Connect jj;
    private DefaultTableModel stockTableModel;
    public void setVisiblestock(boolean b) {
        JFrame frame = new JFrame("Stock");
        frame.setContentPane((new stock()).pstock);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    public stock() {
        jj = new Connect();

        String[] columnHeaders = {"ID", "Nombre", "Precio", "Cantidad", "Detalle", "ID_Proveedor"};
        stockTableModel = new DefaultTableModel(columnHeaders, 0);

        String sql = "SELECT * FROM producto";

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
                stockTableModel.addRow(fila);
            }
            TSTOCK.setModel(stockTableModel);
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
                agregarProducto();
            }
        });
        modificarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarProducto();
            }
        });
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarProducto();
            }
        });
        TSTOCK.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
            }
        });
    }

    private void agregarProducto() {
        String nuevoNombreProveedor = TFPROVEEDOR.getText();

        if (!proveedorExiste(nuevoNombreProveedor)) {
            JOptionPane.showMessageDialog(null, "El proveedor no existe en la base de datos. Por favor, ingrese un proveedor válido.", "Proveedor no encontrado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int proveedorId;
        try {
            proveedorId = jj.getProveedorIdPorNombre(nuevoNombreProveedor);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Proveedor no encontrado", JOptionPane.WARNING_MESSAGE);
            return;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener el ID del proveedor: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String nuevoIDStr = TFID.getText();
        String nuevoNombre = TFNOMBRE.getText();
        String nuevoPrecioStr = TFPRECIO.getText();
        String nuevaCantStr = TFCANTIDAD.getText();

        if (nuevoNombre.isEmpty() || nuevoPrecioStr.isEmpty() || nuevaCantStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos (excepto Detalle) son obligatorios. Por favor, complete todos los campos obligatorios.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int nuevoID;
        double nuevoPrecio;
        int nuevaCant;

        if (nuevoIDStr.isEmpty()) {
            int proximoID = jj.obtenerUltimoID ("facturaciono", "producto");
            JOptionPane.showMessageDialog(null, "Se ha generado automáticamente el ID: " + proximoID, "ID Generado", JOptionPane.INFORMATION_MESSAGE);
            nuevoID = proximoID;
        } else {
            try {
                nuevoID = Integer.parseInt(nuevoIDStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un valor numérico válido para el ID.", "Valor inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        try {
            nuevoPrecio = Double.parseDouble(nuevoPrecioStr);
            nuevaCant = Integer.parseInt(nuevaCantStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese valores numéricos válidos para Precio y Cantidad.", "Valores inválidos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nuevoDetalle = TFDETALLE.getText();

        Object[] fila = new Object[6];
        fila[0] = nuevoID;
        fila[1] = nuevoNombre;
        fila[2] = nuevoPrecio;
        fila[3] = nuevaCant;
        fila[4] = nuevoDetalle;
        fila[5] = proveedorId;

        // Agregar la fila al modelo de la tabla
        stockTableModel.addRow(fila);

        String sql = "INSERT INTO producto (id, nombre, precio, cant, descripcion, id_proveedor) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = jj.getConnection().prepareStatement(sql);
            ps.setInt(1, nuevoID);
            ps.setString(2, nuevoNombre);
            ps.setDouble(3, nuevoPrecio);
            ps.setInt(4, nuevaCant);
            ps.setString(5, nuevoDetalle);
            ps.setInt(6, proveedorId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Registro agregado correctamente.");
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void modificarProducto() {
        int filaSeleccionada = TSTOCK.getSelectedRow();
        if (filaSeleccionada >= 0) {
            String nuevoNombre = TFNOMBRE.getText();
            String nuevoPrecio = TFPRECIO.getText();
            String nuevaCant = TFCANTIDAD.getText();
            String nuevoProveedorNombre = TFPROVEEDOR.getText();
            String nuevoDetalle = TFDETALLE.getText();

            int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas modificar este registro?", "Confirmar modificación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                int id = (int) stockTableModel.getValueAt(filaSeleccionada, 0);
                String nombreAnterior = (String) stockTableModel.getValueAt(filaSeleccionada, 1);
                String precioAnterior = String.valueOf(stockTableModel.getValueAt(filaSeleccionada, 2));

                stockTableModel.setValueAt(nuevoNombre, filaSeleccionada, 1);
                stockTableModel.setValueAt(nuevoPrecio, filaSeleccionada, 2);
                stockTableModel.setValueAt(nuevaCant, filaSeleccionada, 3);
                stockTableModel.setValueAt(nuevoDetalle, filaSeleccionada, 4);
                stockTableModel.setValueAt(nuevoProveedorNombre, filaSeleccionada, 5);

                int nuevoProveedorId;
                try {
                    nuevoProveedorId = jj.getProveedorIdPorNombre(nuevoProveedorNombre);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al obtener el ID del proveedor: " + ex.getMessage());
                    return;
                }

                String sql = "UPDATE producto SET nombre = ?, precio = ?, cant = ?, descripcion = ?, id_proveedor = ? WHERE id = ?";
                try {
                    PreparedStatement ps = jj.getConnection().prepareStatement(sql);
                    ps.setString(1, nuevoNombre);
                    ps.setString(2, nuevoPrecio);
                    ps.setString(3, nuevaCant);
                    ps.setString(4, nuevoDetalle);
                    ps.setInt(5, nuevoProveedorId);
                    ps.setInt(6, id);
                    ps.executeUpdate();
                    // Mostrar avisos de acuerdo a los cambios realizados
                    String mensaje = "Registro modificado correctamente.";
                    if (!nombreAnterior.equals(nuevoNombre)) {
                        mensaje += "\nNombre anterior: " + nombreAnterior + "\nNuevo nombre: " + nuevoNombre;
                    }
                    if (!precioAnterior.equals(nuevoPrecio)) {
                        mensaje += "\nPrecio anterior: " + precioAnterior + "\nNuevo precio: " + nuevoPrecio;
                    }
                    JOptionPane.showMessageDialog(null, mensaje);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione una fila para modificar.");
            }
        }
    }

    private void eliminarProducto() {
        int filaSeleccionada = TSTOCK.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar este registro?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                int id = (int) stockTableModel.getValueAt(filaSeleccionada, 0);
                stockTableModel.removeRow(filaSeleccionada);

                String sql = "DELETE FROM producto WHERE id = ?";
                try {
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
    public ResultSet buscarProductoPorId(int idProducto) {
        String sql = "SELECT * FROM producto WHERE id = ?";
        try {
            PreparedStatement ps = jj.getConnection().prepareStatement(sql);
            ps.setInt(1, idProducto);
            return ps.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar el producto: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private boolean proveedorExiste(String nombreProveedor) {
        try {
            String sql = "SELECT COUNT(*) FROM proveedores WHERE nombre = ?";
            PreparedStatement ps = jj.getConnection().prepareStatement(sql);
            ps.setString(1, nombreProveedor);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int rowCount = rs.getInt(1);
                return rowCount > 0; // Si rowCount es mayor que 0, el proveedor existe.
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al verificar el proveedor: " + ex.getMessage());
        }
        return false;
    }
    public void setVisible(boolean b) {
        JFrame frame = new JFrame("Stock");
        frame.setContentPane(pstock);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
