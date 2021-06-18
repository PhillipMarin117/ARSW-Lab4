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
     * Prepare the connection between server and client
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
     * Validation of request type
     * getPetitions catch FileNotFound exceptions but throws any other IOExeption
     * @param res
     * @param out
     * @throws IOException
     */
    private void getPetitions(String res,PrintWriter out ) throws IOException{
        String outputLine = "";
        if(res.length()>0){
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
                    error(outputLine,res,out);
                }
            }
        }
    }

    /**
     * Makes the call to the class depending on the file's type
     * @param archivoEncontrado
     * @param out
     * @param res
     * @param clientSocket
     * @throws IOException
     */
    private void getRequestFile(File archivoEncontrado, PrintWriter out,
                                String res, Socket clientSocket) throws IOException {

        if (res.contains("png") || res.contains("jpg") || res.contains("PNG") || res.contains("JPG")) {
            ImageResource imgr = new ImageResource();
            imgr.drawImage(clientSocket.getOutputStream(), out, res, archivoEncontrado);
        } else if (res.contains("html") || res.contains("HTML")) {
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
            System.out.println("Recursos diferentes de html5");
            Html5Resource texto = new Html5Resource();
            texto.writeText(clientSocket.getOutputStream(), out, archivoEncontrado, "text/html");
        }
    }

    /**
     * Concats the file name with the root path
     *
     * @param res
     * @return
     */
    private File buscarArchivo(String res) {
        File nuevo = new File(System.getProperty("user.dir") + "/src/main/resources/" + res);
        return nuevo;

    }

    public static void main(String[] args) {
        ExecutorService pool = null;
        try {
            ServerSocket serverSocket = new ServerSocket(getPort());
            System.out.println("Listo para recibir ...");
            pool = Executors.newCachedThreadPool();
            while (true) {
                Socket socket = serverSocket.accept();
                HttpServer req = new HttpServer(socket);
                pool.execute(req);
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        } finally {
            pool.shutdown();
        }

    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 35000;
    }

    /**
     * Write an html with an error text.
     * @param outputLine
     * @param res
     * @return
     */
    public static void error(String outputLine, String res, PrintWriter out) {

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
                + "<h1>ERROR 404.<p><div style='color:red'>" + res + "</div>" + " NO ENCONTRADO</p></h1>"
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
        }catch(FileNotFoundException ex){

        } catch (IOException ex) {
            System.err.println("Run exception while executing thread.");
            //Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}