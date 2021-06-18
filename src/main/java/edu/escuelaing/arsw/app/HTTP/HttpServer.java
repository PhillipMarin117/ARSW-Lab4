package edu.escuelaing.arsw.app.HTTP;

/**
 * @author Felipe Marin
 */

import java.net.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpServer implements Runnable {

    private final Socket clientSocket;
    private ServerSocket serverSocket;

    public HttpServer(final Socket clientSocket) throws IOException {
        serverSocket = null;
        this.clientSocket = clientSocket;
    }

    /**
     * Prepara la conexión entre el servidor y el cliente.
     * @param clientSocket
     * @throws IOException
     */
    private void prepareRequest(Socket clientSocket) throws IOException {
        PrintWriter out;
        BufferedReader in;
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream()));
        String inputLine, outputLine, res = "";
        int contador = 0;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Received: " + inputLine);
            if (contador == 0) {
                res = inputLine;
                contador++;
            }
            if (!in.ready()) {
                break;
            }
        }
        getPetitions(res,out);
        out.close();
        in.close();
    }

    /**
     * Validación del tipo de solicitud
     * getPetitions captura las excepciones de FileNotFound pero arroja cualquier otra IOExeption
     * @param res
     * @param out
     * @throws IOException
     */
    private void getPetitions(String res,PrintWriter out ) throws IOException{
        String outputLine = "";
        if (res.substring(0, 3).equals("GET")) {
            res = res.substring(5, res.length() - 9);
            File archivoEncontrado = buscarArchivo(res);
            if (archivoEncontrado != null) {
                try {
                    getRequestFile(archivoEncontrado, out, res, clientSocket);
                } catch (java.io.FileNotFoundException ex) {
                    error(outputLine, res,out);
                }
            } else {
                error(outputLine, res,out);
            }
        }
    }

    /**
     * Hace la llamada a la clase dependiendo del tipo de archivo
     * @param archivoEncontrado
     * @param out
     * @param res
     * @param clientSocket
     * @throws IOException
     */
    private void getRequestFile(File archivoEncontrado, PrintWriter out, String res, Socket clientSocket) throws IOException {
        if (res.contains("png") || res.contains("jpg")) {
            ImageResource imgr = new ImageResource();
            imgr.drawImage(clientSocket.getOutputStream(), out, res, archivoEncontrado);
        } else if (res.contains("html")) {
            Html5Resource texto = new Html5Resource();
            texto.writeText(clientSocket.getOutputStream(), out, archivoEncontrado, "text/html");
        } else if (res.contains(".js")) {
            Html5Resource texto = new Html5Resource();
            texto.writeText(clientSocket.getOutputStream(), out, archivoEncontrado, "text/javascript");
        } else if (res.contains(".css")) {
            Html5Resource texto = new Html5Resource();
            texto.writeText(clientSocket.getOutputStream(), out, archivoEncontrado, "text/css");
        }
        else{
            error("",res,out);
            //out.println(outputLine);
        }
    }

    /**
     * Concate el nombre del archivo con la ruta raíz.
     * @param res
     * @return
     */
    private File buscarArchivo(String res) {
        //BuscarArchivo find = new BuscarArchivo();
        return new File(System.getProperty("user.dir") + "/src/main/resources/" + res); //"index.html"
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 35000;
    }

    public static void main(String[] args) {
        ExecutorService pool = null;
        try {
            ServerSocket serverSocket = new ServerSocket(getPort());
            System.out.println("Listo para recibir ...");
            pool = Executors.newCachedThreadPool();
            while (true) {
                Socket socket = serverSocket.accept();
                edu.escuelaing.arsw.app.HTTP.HttpServer req = new edu.escuelaing.arsw.app.HTTP.HttpServer(socket);
                pool.execute(req);
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        } finally {
            pool.shutdown();
        }
    }

    /**
     * Escribe un html con un texto de error.
     * @param outputLine
     * @param res
     * @return
     */
    private void error(String outputLine, String res, PrintWriter out) {
        outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>Title of the document</title>\n"
                + "</head>"
                + "<body>"
                + "<h1>ERROR 404.<p><div style='color:red'>" + res.toUpperCase() + "</div>" + " NO ENCONTRADO</p></h1>"
                + "</body>"
                + "</html>";
        out.println(outputLine);
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            prepareRequest(clientSocket);
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }catch(FileNotFoundException ex){
            //String outputLine = error("", "Recurso no encontrado");

        } catch (IOException ex) {
            System.err.println("Run exception while executing thread.");
            Logger.getLogger(edu.escuelaing.arsw.app.HTTP.HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
