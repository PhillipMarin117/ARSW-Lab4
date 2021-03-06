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
    public void writeText(OutputStream clientSocket, PrintWriter out, File archivoEncontrado, String type)
            throws IOException {
        StringBuilder cadena = new StringBuilder();
        String line = null;
        FileReader prueba = new FileReader(archivoEncontrado);
        BufferedReader reader = new BufferedReader(prueba);
        String outputLine = "";
        while ((line = reader.readLine()) != null) {
            cadena.append(line);
        }
        outputLine += "HTTP/1.1 200 OK \r\n"
                + "Content-Type: " + type
                + "\r\n\r\n"
                + cadena;
        out.print(outputLine);
    }
}

