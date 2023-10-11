package AutocompleteFields;
import DBConnection.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class AutoCompletar extends javax.swing.JFrame {
    private Connect connection;
    private JTextField TFCLIENTE;
    private JTextField TFEMPLEADO;
    private JTextField TFPRODUCTO;
    private JTextField TFPROVEEDOR;

    public AutoCompletar() throws SQLException {
        initComponents();
        setLocationRelativeTo(null); // Para centrar la ventana en la pantalla
        connection = new Connect();
        connection.conectar();
        CargarSugerencias();
    }

    private void initComponents() {
        TFCLIENTE = new JTextField();
        TFEMPLEADO = new JTextField();
        TFPRODUCTO = new JTextField();
        TFPROVEEDOR = new JTextField();
    }
    private void CargarSugerencias() {
        cargarSugerenciasClientes();
        cargarSugerenciasEmpleados();
        cargarSugerenciasProductos();
        cargarSugerenciasProveedores();
    }

    public void agregarAutoCompletado(final JTextField textField, final String[] sugerencias) {
        final List<String> sugerenciasList = new ArrayList<>();
        for (String sugerencia : sugerencias) {
            sugerenciasList.add(sugerencia);
        }

        final DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(sugerencias);

        final JComboBox<String> comboBox = new JComboBox<>(model);
        comboBox.setEditable(true);
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = (String) comboBox.getSelectedItem();
                textField.setText(text);
            }
        });
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
    }
    public String[] obtenerNombresProductosDesdeBD() {
        return new String[]{"Producto1", "Producto2", "Producto3", "Producto4"};
    }
    private void cargarSugerenciasClientes() {
        List<String> clienteSuggestions = new ArrayList<>();

        TFCLIENTE.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSuggestions(TFCLIENTE, clienteSuggestions, "cliente");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSuggestions(TFCLIENTE, clienteSuggestions, "cliente");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSuggestions(TFCLIENTE, clienteSuggestions, "cliente");
            }
        });
    }

    private void cargarSugerenciasEmpleados() {
        List<String> empleadoSuggestions = new ArrayList<>();

        TFEMPLEADO.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSuggestions(TFEMPLEADO, empleadoSuggestions, "empleado");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSuggestions(TFEMPLEADO, empleadoSuggestions, "empleado");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSuggestions(TFEMPLEADO, empleadoSuggestions, "empleado");
            }
        });
    }

    private void cargarSugerenciasProductos() {
        List<String> productoSuggestions = new ArrayList<>();

        TFPRODUCTO.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSuggestions(TFPRODUCTO, productoSuggestions, "producto");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSuggestions(TFPRODUCTO, productoSuggestions, "producto");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSuggestions(TFPRODUCTO, productoSuggestions, "producto");
            }
        });
    }

    private void cargarSugerenciasProveedores() {
        List<String> proveedorSuggestions = new ArrayList<>();

        TFPROVEEDOR.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSuggestions(TFPROVEEDOR, proveedorSuggestions, "proveedor");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSuggestions(TFPROVEEDOR, proveedorSuggestions, "proveedor");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSuggestions(TFPROVEEDOR, proveedorSuggestions, "proveedor");
            }
        });
    }

    private void updateSuggestions(JTextField textField, List<String> suggestions, String tableName) {
        String input = textField.getText();
        suggestions.clear();

        if (input.length() > 0) {
            String sql = "SELECT nombre FROM " + tableName + " WHERE nombre LIKE ?";
            try (Connection conn = connection.getConnection(); // Obtener la conexión de la instancia de Connect
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, "%" + input + "%");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    suggestions.add(rs.getString("nombre"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new AutoCompletar().setVisible(true);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
