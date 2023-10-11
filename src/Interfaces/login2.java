package Interfaces;

import Interfaces.stock;

import javax.swing.JOptionPane;

public class login2 {

    private String a, b;

    public void setA(String aa) {
        this.a = aa;
    }

    public void setB(String bb) {
        this.b = bb;
    }

    public void comprobar() {
        if (a.equals("1") && b.equals("1")) {
            stock pstock = new stock();
            pstock.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Usuario o Contraseña incorrecta");
        }
    }
}
