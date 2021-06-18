package edu.escuelaing.arsw.app.HTTP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class HtmlResource {

    public void writeText(OutputStream clientSocket, PrintWriter out, File archivoEncontrado) throws IOException {
        StringBuilder cadena = new StringBuilder();
        String line = null;
        FileReader prueba = new FileReader(archivoEncontrado);
        BufferedReader reader = new BufferedReader(prueba);

        //System.out.println("HA CREADO EL READER");
        while ((line = reader.readLine()) != null) {
            cadena.append(line);
        }
        //System.out.println("PASO POR EL WHILE");
        out.println("HTTP/1.1 200 OK \r");
        out.println("Content-Type: text/html \r");
        out.println("\r");
        out.println(cadena);
    }

}
