/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import static java.lang.Thread.sleep;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import modelo.Alquiler;
import modelo.Automobil;
import modelo.Cliente;
import modelo.DAOmysql;
import vista.NuevoAutomobilController;

/**
 *
 * @author a
 */
public class logica {
//esta clase valida los datos y conecta el controlador del fxml con el DAOmysql

    public logica() {
    }

    public boolean validaReserva(Cliente a, Automobil b, String fecha, String Observaciones) throws IOException, InterruptedException {
        if (validaCliente(a)) {
            if (validaFecha(fecha)) {
                if (new DAOmysql().marcaEnUso(b, true)) {
                    //primero marca el coche como en uso
                    if (new DAOmysql().guardaAlquiler(a, b, fecha, Observaciones)) {
                        new coordinador().setEscenaInicio();
                        mostrarDialeg(AlertType.CONFIRMATION, "Contrato de alquiler guardado", null, "contrato de alquiler guardado correctamente");
                        return true;
                    } else {
                        mostrarDialeg(AlertType.ERROR, "Error", null, "La longitud de las observaciones es de 10000 caracteres maximo");
                        return false;
                    }

                } else {
                    mostrarDialeg(AlertType.ERROR, "Error", null, "Error al guardar el contrato en la BBDD");
                    return false;
                }

            } else {
                mostrarDialeg(AlertType.ERROR, "Error en la fecha", null, "usa el formato aaaa/mm/dd");
                return false;
            }
        } else {
            mostrarDialeg(AlertType.ERROR, "Error en los datos del cliente", null, "usa la cantidad de digitos correcta en el dni y el telefono");
            return false;
        }
    }

    public void mostrarDialeg(Alert.AlertType tipus, String titol, String header, String contingut) {
        Alert alert = new Alert(tipus);
        alert.setTitle(titol);
        alert.setHeaderText(header);
        alert.setContentText(contingut);
        alert.showAndWait();
    }

    public boolean validaCliente(Cliente a) throws InterruptedException {
        //comprueba el num telefono, nombre, y dni
        if (a.getDni() <= 99999999 && a.getDni() >= 10000000) {
            //comprueba dni
            if (!a.getNombre().equals("")) {
                //comprueba que el nombre no este vacio
                if (a.getTelefono() >= 600000000 && a.getTelefono() <= 669999999) {
                    //comprueba que el telefono es valido
                    if (new DAOmysql().clienteExiste(a)) {
                        //si todo es correcto comprueba si el cliente ya existia
                        //mostrarDialeg(AlertType.CONFIRMATION, "Contrato guardado", null, "contrato guardado correctamente, el cliente ya existia");
                        //new coordinador().setEscenaInicio(); 
                        //return new DAOmysql().guardaAlquiler(a,b,fecha,Observaciones);
                        return true;
                    } else {
                        //si el cliente no existe crea uno nuevo
                        mostrarDialeg(AlertType.INFORMATION, "Nuevo cliente", null, "Se ha guardado el nuevo cliente");
                        new DAOmysql().guardaCliente(a);
                        // new coordinador().setEscenaInicio();
                        sleep(500);
                        //return new DAOmysql().guardaAlquiler(a,b,fecha,Observaciones);
                        return true;
                    }
                } else {
                    mostrarDialeg(AlertType.ERROR, "Telefono mal formado", null, "introduce un numero de telefono con 9 digitos y que empieze en 6");
                    return false;
                }
            } else {
                mostrarDialeg(AlertType.ERROR, "Nombre mal formado", null, "introduce un Nombre");
                return false;
            }
        } else {

            mostrarDialeg(AlertType.ERROR, "DNI mal formado", null, "introduce un DNI valido(sin letra)");
            return false;
        }
    }

    public boolean validaFecha(String fechaFin) {
        //esto recibe un String y si la fecha no esta bien formada lanza un mensaje de error
        //(LocalDate es lo mismo que DATETIME de sql)
        try {
            //esto es para ver si la fecha tiene el formado correcto dd/mm//aaaa
            LocalDate localDate = LocalDate.parse(fechaFin);
            //comprueva que la fecha no es anterior al dia de hoy
            if (localDate.isBefore(LocalDate.now())) {
                mostrarDialeg(AlertType.ERROR, "Error en la fecha", null, "La fecha final es anterior a el dia de hoy");
                return false;
            } else {
                //comprueba que la fecha no supera los noventa dias
                if (localDate.isAfter(LocalDate.now().plusDays(90))) {
                    mostrarDialeg(AlertType.ERROR, "Error en la fecha", null, "La fecha final no puede ser mayor que 90 dias desde el dia de hoy");
                    return false;
                } else {
                    return true;
                }
            }
        } catch (DateTimeParseException DateTimeParseException) {
            mostrarDialeg(AlertType.ERROR, "Error en la fecha", null, "introduce una fecha con formato-> aaaa-mm-dd");
            return false;
        }
    }

    private boolean validaMatricula(String matriculaAux) {
        return matriculaAux.toUpperCase().matches("^[0-9]{4}[A-Z]{3}$") && !matriculaExiste(matriculaAux);
    }

    private boolean validaModelo(String modeloAux) {
        char[] array = modeloAux.toCharArray();
        //si la cadena de texto esta vacia devuelve false
        if (modeloAux.equals("") || modeloAux.equals(null)) {
            return false;
        }
        //comprueva que solo hay letras, digitos o espacio en blanco
        for (int cont = 0; cont < array.length; cont++) {
            if (!Character.isLetter(array[cont]) && !Character.isDigit(array[cont]) && !Character.isWhitespace(array[cont])) {
                return false;
            }
        }
        return true;
    }

    private boolean validaCombustible(char letra) {
        //pone el char en minuscula
        letra = String.valueOf(letra).toLowerCase().toCharArray()[0];
        // si la letra coincide con alguna de las cuatro duelve true else false
        return letra == 'g' || letra == 'h' || letra == 'd' || letra == 'e';
    }

    public boolean validaCoche(String marca, String modelo, String matricula, String color, String tipo, char combustible, int numRuedas, int autonomia, int Km, int numPlazas, String enUsoString) {
        if (validaSoloLetras(marca)) {
            if (validaSoloLetras(color)) {
                if (validaSoloLetras(tipo)) {
                    if (validaMatricula(matricula)) {
                        if (validaModelo(modelo)) {
                            if (validaCombustible(combustible)) {
                                if (validaNumRuedas(numRuedas)) {
                                    if (validaAutonomia(autonomia)) {
                                        if (validaKm(Km)) {
                                            if (validaNumPlazas(numPlazas)) {
                                                if (validaEnUso(enUsoString)) {
                                                    return true;
                                                } else {
                                                    mostrarDialeg(Alert.AlertType.ERROR, "Error", "Revisa si el automobil esta disponible", "Introduce 'Y' o 'N'");
                                                    return false;
                                                }
                                            } else {
                                                mostrarDialeg(Alert.AlertType.ERROR, "Error", "Revisa el numero de plazas", "las plazas del vehiculo tienen que estar entre 0 y 100");
                                                return false;
                                            }
                                        } else {
                                            mostrarDialeg(Alert.AlertType.ERROR, "Error", "Revisa los kilometros del vehiculo", "los kilometros deberia estar entre 0 y 10 millones");
                                            return false;
                                        }
                                    } else {
                                        mostrarDialeg(Alert.AlertType.ERROR, "Error", "Revisa la autonomia del vehiculo.", "la autonomia deberia estar entre 10 y 2000km");
                                        return false;
                                    }
                                } else {
                                    mostrarDialeg(Alert.AlertType.ERROR, "Error", "Revisa el numero de ruedas...", "Introduce entre 1 y 20 ruedas");
                                    return false;
                                }
                            } else {
                                mostrarDialeg(Alert.AlertType.ERROR, "Error", "Revisa el combustible...", "Introduce-> G,D,E o H");
                                return false;
                            }
                        } else {
                            mostrarDialeg(Alert.AlertType.ERROR, "Error", "Revisa el modelo...", "Introduce solo letras y numeros");
                            return false;
                        }
                    } else {
                        mostrarDialeg(Alert.AlertType.ERROR, "Error", "Revisa la matricula...", "Introduce 4 numeros y 3 letras, ademas no puede existir ningun otro vehiculo con la misma matricula");
                        return false;
                    }
                } else {
                    mostrarDialeg(Alert.AlertType.ERROR, "Error", "Revisa el tipo...", "Has introducido numeros o otros caracteres donde solo pueden ir letras...");
                    return false;
                }
            } else {
                mostrarDialeg(Alert.AlertType.ERROR, "Error", "Revisa el color...", "Has introducido numeros o otros caracteres donde solo pueden ir letras...");
                return false;
            }
        } else {
            mostrarDialeg(Alert.AlertType.ERROR, "Error", "Revisa la marca...", "Has introducido numeros o otros caracteres donde solo pueden ir letras...");
            return false;
        }
    }

    private boolean validaNumRuedas(int numRuedas) {
        //el maximo de ruedas son 21
        try {
            return numRuedas > 1 && numRuedas < 21;
        } catch (Exception a) {
            return false;
        }

    }

    private boolean validaAutonomia(int autonomia) {
        try {
            int aux = autonomia - 1;
            return autonomia > 10 && autonomia < 2000;
        } catch (Exception a) {
            return false;
        }
    }

    private boolean validaKm(int km) {
        try {
            int a = km + 1;
            return km >= 0 && km < 10000000;
        } catch (Exception a) {
            return false;
        }
    }

    private boolean validaNumPlazas(int numPlazas) {
        try {
            int a = numPlazas + 1;
            return numPlazas > 0 && numPlazas < 100;
        } catch (Exception a) {
            return false;
        }
    }

    private boolean validaSoloLetras(String marca) {
        return marca.toLowerCase().replace(" ", "").matches("[a-z]+");
    }

    private boolean validaEnUso(String enUsoString) {
        try {
            switch (enUsoString.toLowerCase().charAt(0)) {
                case 'y':
                    NuevoAutomobilController.enUso = true;
                    return true;
                case 'n':
                    NuevoAutomobilController.enUso = false;
                    return true;
                default:
                    return false;
            }
        } catch (java.lang.StringIndexOutOfBoundsException trhtynjhg) {
            return false;
        } catch (Exception adsffgh) {
            return false;
        }
    }

    private boolean esSoloLetras(String texto) {
        //es para saber si una cadena solo tiene texto. esta funcion estaba en interntet
        /**
         * *********************************************************
         */
        //Recorremos cada caracter de la cadena y comprobamos si son letras.
        //Para comprobarlo, lo pasamos a mayuscula y consultamos su numero ASCII.
        //Si está fuera del rango 65 - 90, es que NO son letras.
        //Para ser más exactos al tratarse del idioma español, tambien comprobamos
        //el valor 165 equivalente a la Ñ
        for (int i = 0; i < texto.length(); i++) {
            char caracter = texto.toUpperCase().charAt(i);
            int valorASCII = (int) caracter;
            if (valorASCII != 165 && (valorASCII < 65 || valorASCII > 90)) {
                return false; //Se ha encontrado un caracter que no es letra
            }
        }
        //Terminado el bucle sin que se haya retornado false, es que todos los caracteres son letras
        return true;
    }

    //esto valida que la fecha esta dentro del limite(evita alquilar un bien por un tiempo demasiado largo)
    public boolean validaFechaLimite(String fecha) {
        LocalDate hoy = LocalDate.now();
        //el limite seran 90 dias (3 meses)
        LocalDate limite = hoy.plusDays(90);
        //fecha fin elejida por el usuario
        LocalDate fechaFinAlquiler = LocalDate.parse(fecha);
        //si la fecha sobrepasa el limite, devuelve false, else return true
        return fechaFinAlquiler.isBefore(limite);

    }

    public boolean validaUsuario(String usuario, String psswd) {
        //solo comprueba que los campos no estan vacios
        return !usuario.equals("") && !psswd.equals("");
    }

    public static String cifraPsswd(String psswd) throws NoSuchAlgorithmException {
        //crea instancia MessageDigest
        MessageDigest md = MessageDigest.getInstance("MD5");
        //cifra contraseña y lo guarda en array de bytes
        byte[] messageDigest = md.digest(psswd.getBytes());
        //usando la clase bigInteger obtenemos el el valor de la clave en forma String
        BigInteger number = new BigInteger(1, messageDigest);
        String hashtext = number.toString(16);
        String a = null;
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }
    //esta funcion borra las entradas del historial cuya fecha fin sea posterior a ayer

    public ObservableList borraEntradasConFechaFinAnteriorHoy(ObservableList ObservableList) {
        int cont = 0;
        while (true) {
            try { //recoje el objeto alquiler de la tabla
                Alquiler alquiler = (Alquiler) ObservableList.get(cont);
                //crea objeto localdate con la fecha fin de ese objeto alquiler
                LocalDate fechaFin = LocalDate.parse(alquiler.getFechaFin());
                //if fechafin es anterior a ayer
                if (fechaFin.isAfter(LocalDate.now().minusDays(1))) {
                    ObservableList.remove(cont);
                    cont = 0;
                } else {
                    cont = cont + 1;
                }
            } catch (Exception a) {
                //cuando salte una excepcion, probablemente IndexoutBoundException rompe el bucle
                break;
            }

        }
        return ObservableList;
    }
    //esta funcion borra los alquileres que ya havian acabado o que el automobil ha sido retirado de la empresa

    public ObservableList borraAlquileresAcabadosAndAutosRetiradosAndAutosNoEnUso(ObservableList ObservableList) {
        int cont = 0;
        Alquiler alquiler;
        LocalDate ahora = LocalDate.now();
        LocalDate fechaFin;
        bucle:
        while (true) {
            try {
                alquiler = (Alquiler) ObservableList.get(cont);
                fechaFin = LocalDate.parse(alquiler.getFechaFin());
                if (fechaFin.isBefore(ahora) || alquiler.getAutomobil().getMarca().charAt(0)=='%'||!alquiler.getAutomobil().getEnUso()) {
                    ObservableList.remove(alquiler);
                    //reiniciar el contador porque cuando se hace .remove() se resetea el observableList(esto evita IndexOutOfBoundsException)
                    cont = 0;
                } else {
                    cont = cont + 1;
                }

            } catch (java.lang.IndexOutOfBoundsException a) {
                //cuando se acaba el observableList da este error y rompe el bucle
                break;
            } catch (java.time.format.DateTimeParseException b) {
                //este error suele salir cuando he puesto un espacio en blanco en la fecha sin querer en la bbdd
                System.out.println("Puede que haya un espacio en blanco en la fecha en la base de datos");
                break;
            }catch(Exception c){
                break;
            }
        }

        return ObservableList;
    }

    public ObservableList eliminarVehiculosRetirados(ObservableList ObservableList) {
        int cont = 0;
        while (true) {
            try {
                if (((Automobil) ObservableList.get(cont)).getMarca().charAt(0) == '%') {
                    ObservableList.remove(cont);
                    //reiniciar el contador porque cuando se hace .remove() se resetea el observableList(esto evita IndexOutOfBoundsException)
                    cont = 0;
                } else {
                    cont = cont + 1;
                }

            } catch (java.lang.IndexOutOfBoundsException a) {
                //cuando se acaba el observableList da este error y rompe el bucle
                break;
            }
        }
        return ObservableList;
    }
//este metodo cambia los simbolos % por R en la lista para mostrar los automobiles retirados con una R al principio

    public ObservableList sustituyeSimboloporR(ObservableList ObservableList) {
        int cont = 0;
        char caracterEscpecial = '%';
        while (true) { 
            try {
                Alquiler alquiler = (Alquiler) ObservableList.get(cont);
                //para no tener porblemas con los espacion, si hay un espacio al principio, lo borro
                if (alquiler.getNombreMarca().charAt(0) == ' ') {
                    alquiler.setNombreMarca(alquiler.getNombreMarca().replaceFirst(" ", ""));
                }
                //si el automovil esta marcado con % lo sustituyo con una R para que quede mas bonito
                if (alquiler.getNombreMarca().charAt(0) == caracterEscpecial) {
                    String aux = "";
                    aux = ((Alquiler) ObservableList.get(cont)).getNombreMarca();
                    aux = aux.replaceFirst("%", "(R) ");
                    ((Alquiler) ObservableList.get(cont)).setNombreMarca(aux);
                    //reiniciar el contador porque cuando se hace .remove() se resetea el observableList(esto evita IndexOutOfBoundsException)
                    cont = 0;
                } else {
                    cont = cont + 1;
                }

            } catch (java.lang.IndexOutOfBoundsException a) {
                //cuando se acaba el observableList da este error y rompe el bucle
                break;
            }
        }
        return ObservableList;
    }
//borra de la lista los vehiculos no eliminados del catalogo de la empresa

    public ObservableList borraCochesNoEliminados(ObservableList observableList) {
        int cont = 0;
        char caracterEscpecial = '%';
        while (true) {
            try {
                Automobil Automobil = (Automobil) observableList.get(cont);
                //para no tener porblemas con los espacion, si hay un espacio al principio, lo borro
                if (Automobil.getMarca().charAt(0) == ' ') {
                    Automobil.setMarca(Automobil.getMarca().replaceFirst(" ", ""));
                }

                //si el automovil esta marcado con % lo sustituyo con una R para que quede mas bonito
                if (Automobil.getMarca().charAt(0) == caracterEscpecial) {
                    cont = cont + 1;
                } else {
                    observableList.remove(cont);
                    cont = 0;
                }

            } catch (java.lang.IndexOutOfBoundsException a) {
                //cuando se acaba el observableList da este error y rompe el bucle
                break;
            }
        }
        //borra el caracter especial %
        cont = 0;
        ObservableList aux = FXCollections.observableList(new ArrayList<>());
        while (true) {
            try {
                Automobil Automobil = (Automobil) observableList.get(cont);
                observableList.remove(cont);
                Automobil.setMarca(Automobil.getMarca().replaceFirst("%", ""));
                aux.add(Automobil);
                //cuando el array se vacia se sale del bucle
                if (observableList.isEmpty()) {
                    break;
                }
            } catch (java.lang.IndexOutOfBoundsException a) {
                //cuando se acaba el observableList da este error y rompe el bucle
                break;
            }
        }
        return aux;
    }

    //esta funcion hace lo mismo pero para la clase automobil
    public ObservableList sustituyeSimboloporR_2(ObservableList ObservableList) {
        int cont = 0;
        char caracterEscpecial = '%';
        while (true) {
            try {
                Automobil Automobil = (Automobil) ObservableList.get(cont);
                //para no tener porblemas con los espacion, si hay un espacio al principio, lo borro
                if (Automobil.getMarca().charAt(0) == ' ') {
                    Automobil.setMarca(Automobil.getMarca().replaceFirst(" ", ""));
                }
                //si el automovil esta marcado con % lo sustituyo con una R para que quede mas bonito
                if (Automobil.getMarca().charAt(0) == caracterEscpecial) {
                    String aux = "";
                    aux = ((Automobil) ObservableList.get(cont)).getMarca();
                    aux = aux.replaceFirst("%", "(R) ");
                    ((Automobil) ObservableList.get(cont)).setMarca(aux);
                    //reiniciar el contador porque cuando se hace .remove() se resetea el observableList(esto evita IndexOutOfBoundsException)
                    cont = 0;
                } else {
                    cont = cont + 1;
                }

            } catch (java.lang.IndexOutOfBoundsException a) {
                //cuando se acaba el observableList da este error y rompe el bucle
                break;
            }
        }
        return ObservableList;
    }

    //esta funcion pone el nombre completo del combustible(cambia el carcater por la palabra completa)
    public ObservableList ponNombreCombustible(ObservableList ObservableList) {
        int cont = 0;
        char combustible;
        while (true) {
            try {
                combustible = ((Automobil) ObservableList.get(cont)).getCombustible();
                switch (combustible) {
                    case 'G':
                        ((Automobil) ObservableList.get(cont)).setCombustibleString("Gasolina");
                        break;
                    case 'D':
                        ((Automobil) ObservableList.get(cont)).setCombustibleString("Diesel");
                        break;
                    case 'H':
                        ((Automobil) ObservableList.get(cont)).setCombustibleString("Hibrido");
                        break;
                    case 'E':
                        ((Automobil) ObservableList.get(cont)).setCombustibleString("Electrico");
                        break;
                }
                cont = cont + 1;
            } catch (java.lang.IndexOutOfBoundsException a) {
                //cuando se acaba el observableList da este error y rompe el bucle
                break;
            }
        }
        return ObservableList;
    }

    private boolean matriculaExiste(String matricula) {
        int cont = 0;
        ObservableList<Automobil> ObservableList = FXCollections.observableList(new DAOmysql().obtenerTodosCoches());
        ObservableList = eliminarVehiculosRetirados(ObservableList);
        while (true) {
            try {
                if (ObservableList.get(cont).getMatricula().equals(matricula)) {
                    return true;
                }
                cont = cont + 1;
            } catch (java.lang.IndexOutOfBoundsException a) {
                return false;
            }
        }
    }
}
