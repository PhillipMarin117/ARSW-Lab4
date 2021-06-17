package edu.escuelaing.arsw.app.HTTP;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.imageio.ImageIO;

public class ImageResource {

    public void drawImage(OutputStream  clientSocket,PrintWriter out ,String res, File archivoEncontrado) throws IOException {
        
        System.out.println("entra?????? ****final: "+archivoEncontrado);
        System.out.println("ME ENCUENTRO EN IMAGEN Y ESTA ES LA RES: "+res);
        if(res.contains("img/")){
            res = res.substring(4,res.length());
            System.out.println("Entro en condicAICIOn: "+res);
        }

        if(res.substring(5, 7).equals("bs")){
            BufferedImage image = ImageIO.read(new File("C:\\Users\\Felipe\\Desktop\\Trabajos Universidad\\Inter ARSW\\ARWS\\ARSW-Lab4\\src\\main\\resource\\bootstrap\\" + res));

        }else{
            BufferedImage image = ImageIO.read(new File("C:\\Users\\Felipe\\Desktop\\Trabajos Universidad\\Inter ARSW\\ARWS\\ARSW-Lab4\\src\\main\\resources\\img\\"+res));
            //System.out.println("PASA EL PRIMER LINEA");
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: image/PNG");
            out.println();
            ImageIO.write(image, "PNG", clientSocket);
        }
    }
}
