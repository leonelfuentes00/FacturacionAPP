package Interfaces;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    private JTextField usuario;
    private JTextField contraseña;
    private JButton ok;
    private JPanel plogin;
    private JButton registrarButton;

    public Login() {

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String a, b;
                a = usuario .getText();
                b = contraseña.getText();
                login2 lo = new login2();
                lo.setA(a);
                lo.setB(b);
                lo.comprobar();
            }
        });
        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Registrar pregistrar = new Registrar();
                pregistrar.setVisibleRegistrar(true);
            }
        });
    }

    public static void main() {
        JFrame frame=new JFrame("Login");
        frame.setContentPane(new Login().plogin);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

