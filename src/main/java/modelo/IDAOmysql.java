/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.List;
import usuarios.Usuario;

/**
 *
 * @author a
 */
public interface IDAOmysql {

    public List obtenerTodosCoches();

    public List obtenerTodosCochesDisponibles();

    public boolean clienteExiste(Cliente a);

    public boolean guardaCliente(Cliente a);

    public boolean guardaAlquiler(Cliente Cliente_2, Automobil Automobil, String FechaFin, String Observaciones);

    public int buscaIdCliente(int dni);

    public int buscaIdAutomobil(String matricula);

    public boolean marcaEnUso(Automobil automobil, boolean uso);

    public List ObtenerHistorial();

    public List ObtenerHistorialAnularReserva();

    public boolean guardaAutomobil(Automobil automobil);

    public boolean eliminarAutomobil(Automobil auto);

    public boolean enUso(Automobil auto);

    public List obtenerTodosCochesNoDisponibles();

    public boolean borrarEntradaHistorial(Alquiler selectedItem);

    public boolean login(Usuario user);

    public boolean RegistrarUsuario(String usuario, String psswd);

    public void marcaEnUsoPorIdAuto(int id_coche, boolean uso);
}
