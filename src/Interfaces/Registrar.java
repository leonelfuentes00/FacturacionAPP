package Interfaces;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Registrar {
    private JPanel pregistrar;
    private JTextField nombre;
    private JTextField usuario;
    private JButton registrarButton;
    private JTextField cuil;
    private JTextField dni;
    private JTextField mail;
    private JTextField direccion;
    private JComboBox<String> Localidades;
    private JRadioButton EmpleadoRadio;
    private JRadioButton PROVEEDORRadioButton;
    private JRadioButton CLIENTERadioButton;

    public Registrar() {
        Localidades.addItem("Mar Chiquita");
        Localidades.addItem("Mar del Plata");
        Localidades.addItem("Miramar");
        Localidades.addItem("Necochea");
        Localidades.addItem("Pinamar");
        Localidades.addItem("Villa Gesell");
        final String[] Localidadselec = {""};

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(EmpleadoRadio);
        grupo.add(PROVEEDORRadioButton);
        grupo.add(CLIENTERadioButton);
        EmpleadoRadio.setSelected(true);

        EmpleadoRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Has seleccionado Empleado");
            }
        });
        PROVEEDORRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Has seleccionado Proveedor");
            }
        });
        CLIENTERadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Has seleccionado Cliente");
            }
        });

        Localidades.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Localidadselec[0] = (String) Localidades.getSelectedItem();
            }
        });
        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
    public void setVisibleRegistrar (boolean b) {
        JFrame frame=new JFrame("Registro");
        frame.setContentPane((new Registrar()).pregistrar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

