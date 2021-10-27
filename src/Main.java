import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;

public class Main {
    //TODO будет интересно почитать на тему самих типов файлов и их чтения
    public static Dimension getJPEGDimension(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);

        // check for SOI marker
        if (fis.read() != 255 || fis.read() != 216)
            throw new RuntimeException("SOI (Start Of Image) marker 0xff 0xd8 missing");

        Dimension d = null;

        while (fis.read() == 255) {
            int marker = fis.read();
            int len = fis.read() << 8 | fis.read();

            if (marker == 192) {
                fis.skip(1);

                int height = fis.read() << 8 | fis.read();
                int width = fis.read() << 8 | fis.read();

                d = new Dimension(width, height);
                break;
            }

            fis.skip(len - 2);
        }


        fis.close();

        return d.getSize();
    }

    public static void main(String[] args) throws IOException {
        String absolutPath = "C:\\Users\\user\\Downloads\\Luck\\горы.jpeg";
        System.out.println(getJPEGDimension(new File(absolutPath)));
        //System.out.println(extractBytes(absolutPath));
    }

    public static String extractBytes(String ImageName) throws IOException {
        // open image
        File imgPath = new File(ImageName);
        BufferedImage bufferedImage = ImageIO.read(imgPath);

        // get DataBufferBytes from Raster
        WritableRaster raster = bufferedImage.getRaster();
        DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
        data.getData();

        return URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(data.getData()));

    }
}
