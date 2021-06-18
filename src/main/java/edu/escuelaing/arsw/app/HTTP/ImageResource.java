package edu.escuelaing.arsw.app.HTTP;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class ImageResource {

    public void drawImage(OutputStream  clientSocket,PrintWriter out ,String res, File archivoEncontrado) throws IOException {


        System.out.println("entra?????? ****final: "+archivoEncontrado);
        System.out.println("ME ENCUENTRO EN IMAGEN Y ESTA ES LA RES: "+res);
        try {
            String archivoNombre = archivoEncontrado.toString().replace(res, "");
            if (res.contains("img/")) {
                res = res.substring(4, res.length());
                System.out.println("Entro en condicAICIOn: " + res);
            }
            File variable = new File(System.getProperty("user.dir") + "/resources/img/" + res);

            BufferedImage image = ImageIO.read(archivoEncontrado);
            ByteArrayOutputStream toBytesFile = new ByteArrayOutputStream();
            DataOutputStream writeImg = new DataOutputStream(clientSocket);
            ImageIO.write(image, "PNG", toBytesFile);
            //System.out.println("PASA EL PRIMER LINEA");
            writeImg.writeBytes("HTTP/1.1 200 OK \r\n");
            writeImg.writeBytes("Content-Type: image/png \r\n");
            writeImg.writeBytes("\r\n");
            writeImg.write(toBytesFile.toByteArray());
        } catch (javax.imageio.IIOException ex){
            HttpServer.error("", "Mensaje de error: " + ex, out);
        }
    }
}
