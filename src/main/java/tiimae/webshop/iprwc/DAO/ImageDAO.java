package tiimae.webshop.iprwc.DAO;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ImageDAO {

    public String saveBrandImage(MultipartFile image) throws IOException {
        String folder = "src/main/resources/images/brand/";
        byte[] bytes = image.getBytes();
        Path path = Paths.get(folder + image.getOriginalFilename());
        Files.write(path, bytes);

        return path.toString();
    }

}
