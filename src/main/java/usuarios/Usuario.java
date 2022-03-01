/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usuarios;

/**
 *
 * @author a
 */
public class Usuario {
    private String Usuario;
    private String psswd;
    //el campo id evita la excepcion Caused by: java.sql.SQLException: Field 'usuario' doesn't have a default value
    private int id;

    public Usuario() {
    }

    public Usuario(String Usuario, String psswd, int id) {
        this.Usuario = Usuario;
        this.psswd = psswd;
        this.id = id;
    }

    public Usuario(String Usuario, String psswd) {
        this.Usuario = Usuario;
        this.psswd = psswd;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public String getPsswd() {
        return psswd;
    }

    public void setPsswd(String psswd) {
        this.psswd = psswd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Usuario{" + "Usuario=" + Usuario + ", psswd=" + psswd + ", id=" + id + '}';
    }

    
}
