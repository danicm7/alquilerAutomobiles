/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 *
 * @author a
 */
//esta clase solo sirve para leer un fichero de texto
public class flujosDatos {

    public flujosDatos() {
    }

    public String lee(String ruta) {
        String texto = "";
        try {
            InputStream flux = flujosDatos.class.getResourceAsStream(ruta);
            InputStreamReader in = new InputStreamReader(flux);
            BufferedReader buffer = new BufferedReader(in);
            while (buffer.ready()) {
                texto = texto + buffer.readLine();
            }
            buffer.close();
            in.close();
            flux.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        //cambia los % por saltos de linea
        return texto.replace("%","\n" );

    }
}
