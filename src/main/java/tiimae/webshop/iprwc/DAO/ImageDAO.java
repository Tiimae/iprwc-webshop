package tiimae.webshop.iprwc.DAO;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class ImageDAO {

    public String saveBrandImage(MultipartFile image, String brandName) throws IOException {
        String folder = "src/main/resources/images/brand/" + brandName + "/";
        byte[] bytes = image.getBytes();
        if (!Files.isDirectory(Paths.get(folder))) {
            Files.createDirectories(Paths.get(folder));
        }

        Path path = Paths.get(folder + image.getOriginalFilename());
        Files.write(path, bytes);

        return path.toString();
    }

}
