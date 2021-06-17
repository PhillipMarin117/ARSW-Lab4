package edu.escuelaing.arsw.app.HTTP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Html5Resource {

    /**
     * Escribe componentes HTML5 en clientSocket
     * @param clientSocket
     * @param out
     * @param archivoEncontrado
     * @param type
     * @throws IOException 
     */
    public void writeText(OutputStream clientSocket, PrintWriter out, File archivoEncontrado,String type) throws IOException {
        StringBuilder cadena = new StringBuilder();
        String line = null;
        FileReader prueba = new FileReader(archivoEncontrado);
        BufferedReader reader = new BufferedReader(prueba);

        while ((line = reader.readLine()) != null) {
            cadena.append(line);
        }
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: "+type);
        out.println();
        out.println(cadena);

    }

}
