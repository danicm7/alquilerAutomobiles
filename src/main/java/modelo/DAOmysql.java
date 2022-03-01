/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import controlador.logica;
import java.time.LocalDate;
import java.util.List;
import javafx.scene.control.Alert;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import usuarios.Usuario;

/**
 *
 * @author a
 */
public class DAOmysql {

    public DAOmysql() {
    }

    public List obtenerTodosCoches() {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesio = factory.openSession();
        //Query query = sesio.createQuery("from Automobil l");
        Query query = sesio.createQuery("from Automobil");
        List<Automobil> lista = query.list();
        return lista;
    }

    public List obtenerTodosCochesDisponibles() {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesio = factory.openSession();
        //Query query = sesio.createQuery("from Automobil l");
        Query query = sesio.createQuery("from Automobil where enUso = false");
        List<Automobil> lista = query.list();
        return lista;
    }

    public boolean clienteExiste(Cliente a) {
        //busca si un cliente existe
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesio = factory.openSession();
        //Query query = sesio.createQuery("from Automobil l");
        Query query = sesio.createQuery("from Cliente c where c.dni = :p").setParameter("p", a.getDni());
        List<Automobil> lista = query.list();
        if (lista.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean guardaCliente(Cliente a) {
        try {
            SessionFactory factory = HibernateUtil.getSessionFactory();
            Session sesio = factory.openSession();
            Transaction tx = sesio.beginTransaction();
            sesio.save(a);
            tx.commit();
            return true;
        } catch (HibernateException excepcion) {
            return false;
        }
    }

    public boolean guardaAlquiler(Cliente Cliente_2, Automobil Automobil, String FechaFin, String Observaciones) {
        Cliente Cliente = Cliente_2;
        try {
            SessionFactory factory = HibernateUtil.getSessionFactory();
            Session sesio = factory.openSession();
            //si el cliente ya existia, no hace falta crear uno nuevo
            if (clienteExiste(Cliente)) {
                Cliente = buscaClientePorDNI(Cliente.getDni());
                Cliente.setId(buscaIdCliente(Cliente.getDni()));
                //esto evita HibernateException: Illegal attempt to associate a collection with two open sessions
                //lo que hace es relacionar el objeto  con su omonimo en la bbdd, porque dentro del programa eran  objetos diferentes(auto en el programa/ auto en la bbdd), y daba error al borrar con hibernate
                Cliente = ((Cliente) sesio.merge(Cliente));
            }
            //b.setId(buscaIdAutomobil(b.getMatricula()));

            Alquiler alquiler = new Alquiler(Automobil, Cliente, LocalDate.now().toString(), FechaFin, Observaciones);
            Transaction tx = sesio.beginTransaction();
            sesio.save(alquiler);
            tx.commit();
            return true;

        } catch (HibernateException excepcion) {
            return false;
        }
    }

//esto pone los id en los objetos cloentes/coche que se guardaran en la tabla Alquiler, porque el id lo asigna 
    //mysql automaticamente al guardarse en la bbdd, en el programa los estan vacios por defecto. (en automobil no hace falta creo)
    public int buscaIdCliente(int dni) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesio = factory.openSession();
        Query query = sesio.createQuery("select id from Cliente where dni = :p").setParameter("p", dni);
        return (int) query.getSingleResult();
    }

    public int buscaIdAutomobil(String matricula) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesio = factory.openSession();
        Query query = sesio.createQuery("select id from Automobil where matricula = :p").setParameter("p", matricula);
        return (int) query.getSingleResult();
    }

    public boolean marcaEnUso(Automobil automobil, boolean uso) {
        //marca un coche como que se esta usando
        try {
            SessionFactory factory = HibernateUtil.getSessionFactory();
            Session sesio = factory.openSession();
            Query query = sesio.createQuery("from Automobil e where e.matricula = :parametro").setParameter("parametro", automobil.getMatricula());
            //Query query = sesio.createQuery("from Empleat e where e.empNo=1001");
            List<Automobil> lista = query.list();
            Automobil auto = lista.get(0);
            auto.setEnUso(uso);
            Transaction tx = sesio.beginTransaction();
            sesio.update(auto);
            tx.commit();
            return true;
        } catch (HibernateException excepcion) {
            return false;
        } catch (java.lang.NullPointerException a) {
            //esta excepcion sale cuando no se ha seleccionado ningun vehiculo(automobil = null)
            return false;
        }
    }

    public List ObtenerHistorial() {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesio = factory.openSession();
        Query query = sesio.createQuery("from Alquiler order by fechaFin desc");
        List<Alquiler> lista = query.list();
        //esto de aqui es para rellenar los atributos auxiliares de la clase Alquiler porque el tableView
        //de la pantalla historial no puede leer atributos de varios objetos diferentes
        int idAuto;
        int idCliente;
        for (int cont = 0; cont < lista.size(); cont++) {
            Alquiler alquilerObj = lista.get(cont);
            idAuto = alquilerObj.getAutomobil().getId();
            idCliente = alquilerObj.getCliente().getId();
            alquilerObj.setNombreCliente(buscaNombreCliente(idCliente));
            alquilerObj.setNombreMarca(buscaNombreAuto(idAuto));
            alquilerObj.setDni(buscaDni(idCliente));
        }
        return lista;
    }

    public List ObtenerHistorialAnularReserva() {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesio = factory.openSession();
        String hoy = LocalDate.now().toString();
        Query query = sesio.createQuery("from Alquiler where fechaFin > '" + hoy + "' order by fechaInicio desc ");
        List<Alquiler> lista = query.list();
        //esto de aqui es para rellenar los atributos auxiliares de la clase Alquiler porque el tableView
        //de la pantalla historial no puede leer atributos de varios objetos diferentes
        int idAuto;
        int idCliente;
        for (int cont = 0; cont < lista.size(); cont++) {
            Alquiler alquilerObj = lista.get(cont);
            idAuto = alquilerObj.getAutomobil().getId();
            idCliente = alquilerObj.getCliente().getId();
            alquilerObj.setNombreCliente(buscaNombreCliente(idCliente));
            alquilerObj.setNombreMarca(buscaNombreAuto(idAuto));
            alquilerObj.setDni(buscaDni(idCliente));
            alquilerObj.setId_coche(idAuto);
        }
        return lista;
    }

    private String buscaNombreCliente(int idCliente) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesio = factory.openSession();
        Query query = sesio.createQuery("select nombre from Cliente where id = " + idCliente);
        String nombre = query.list().get(0).toString();
        return nombre;
    }

    private String buscaNombreAuto(int idAuto) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesio = factory.openSession();
        Query query = sesio.createQuery("select marca from Automobil where id = " + idAuto);
        String nombre = query.list().toString();
        query = sesio.createQuery("select modelo from Automobil where id = " + idAuto);
        nombre = nombre + "-" + query.list().toString();
        nombre = nombre.replace("]", " ");
        return nombre.replace("[", " ");
    }

    public boolean guardaAutomobil(Automobil automobil) {
        try {
            SessionFactory factory = HibernateUtil.getSessionFactory();
            Session sesio = factory.openSession();
            Transaction tx = sesio.beginTransaction();
            sesio.save(automobil);
            tx.commit();
            return true;
        } catch (HibernateException a) {
            return false;
        }
    }

    public boolean eliminarAutomobil(Automobil auto) {
        try {
            //Lo que hago es marcar los automobiles, pero no eliminarlos de la base de datos, porque sino se perderia informacion del vehiculo en el historial
            marcaEnUso(auto, true);
            auto.setMarca("%" + auto.getMarca());
            updateAuto(auto);
            //sesio.delete(a);
            return true;
        } catch (HibernateException a) {
            return false;
        }
    }
// si hay alguna excepcion devuelve true

    public boolean enUso(Automobil auto) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesio = factory.openSession();
        Query query = null;
        try {
            query = sesio.createQuery("select enUso from Automobil where matricula = '" + auto.getMatricula() + "'");
        } catch (java.lang.NullPointerException a) {
            //esta excepcion sale cuando no se ha seleccionado ningun automobil
            new logica().mostrarDialeg(Alert.AlertType.ERROR, "Selecciona un vehiculo", null, null);
            return true;
        }
        boolean uso = query.getSingleResult() == "true";
        return uso;
    }

    public List obtenerTodosCochesNoDisponibles() {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesio = factory.openSession();
        Query query = sesio.createQuery("from Automobil where enUso = true");
        List<Automobil> lista = query.list();
        return lista;
    }

    private int buscaDni(int idCliente) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesio = factory.openSession();
        Query query = sesio.createQuery("select dni from Cliente where id = " + idCliente);
        int dni = (int) query.list().get(0);
        return dni;
    }

    public boolean borrarEntradaHistorial(Alquiler selectedItem) {
        try {
            SessionFactory factory = HibernateUtil.getSessionFactory();
            Session sesio = factory.openSession();
            Transaction tx = sesio.beginTransaction();
            //esto evita HibernateException: Illegal attempt to associate a collection with two open sessions
            //lo que hace es relacionar el objeto a, con su omonimo en la bbdd, porque dentro del programa eran  objetos diferentes(auto en el programa/ auto en la bbdd), y daba error al borrar con hibernate
            Alquiler a = ((Alquiler) sesio.merge(selectedItem));
            sesio.delete(a);
            //sesio.delete(selectedItem);
            tx.commit();
            return true;
        } catch (HibernateException a) {
            return false;
        }
    }

    public boolean login(Usuario user) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesio = factory.openSession();
        //select contraseña buscando por el nombre de usuario
        Query query = sesio.createQuery("select psswd from Usuario where usuario = '" + user.getUsuario() + "'");
        try {
            return ((String) query.list().get(0)).equals(user.getPsswd());
        } catch (javax.persistence.NoResultException a) {
            return false;
        } catch (java.lang.IndexOutOfBoundsException b) {
            //si salta esta excepcion es porque no hay resultado de la query
            return false;
        }
    }

    public boolean RegistrarUsuario(String usuario, String psswd) {
        Usuario user = new Usuario(usuario, psswd);
        try {
            SessionFactory factory = HibernateUtil.getSessionFactory();
            Session sesio = factory.openSession();
            Transaction tx = sesio.beginTransaction();
            sesio.save(user);
            tx.commit();
            return true;
        } catch (HibernateException a) {
            return false;
        }
    }

    public void marcaEnUsoPorIdAuto(int id_coche, boolean uso) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesio = factory.openSession();
        Query query = sesio.createQuery("from Automobil e where e.id = :parametro").setParameter("parametro", id_coche);
        //Query query = sesio.createQuery("from Empleat e where e.empNo=1001");
        List<Automobil> lista = query.list();
        Automobil auto = lista.get(0);
        auto.setEnUso(uso);
        Transaction tx = sesio.beginTransaction();
        sesio.update(auto);
        tx.commit();
    }

    private void updateAuto(Automobil aux) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesio = factory.openSession();
        Transaction tx = sesio.beginTransaction();
        Query query = sesio.createQuery("from Automobil e where e.id = :parametro").setParameter("parametro", aux.getId());
        //Query query = sesio.createQuery("from Empleat e where e.empNo=1001");
        List<Automobil> lista = query.list();
        Automobil auto = lista.get(0);
        auto.setMarca(aux.getMarca());
        sesio.update(auto);
        tx.commit();
    }

    private Cliente buscaClientePorDNI(int dni) {
        try {
            SessionFactory factory = HibernateUtil.getSessionFactory();
            Session sesio = factory.openSession();
            Query query = sesio.createQuery("from Cliente e where e.dni = :parametro").setParameter("parametro", dni);
            return (Cliente) query.list().get(0);
        } catch (HibernateException a) {
            return null;

        }
    }

}
