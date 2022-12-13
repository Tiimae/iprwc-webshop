package tiimae.webshop.iprwc.DAO;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Paths.get;

@Component
public class ImageDAO {

    public String saveBrandImage(MultipartFile image, String brandName, String type) throws IOException {
        String folder = "src/main/resources/static/images/" + type + "/" + brandName + "/";
        byte[] bytes = image.getBytes();
        if (!Files.isDirectory(Paths.get(folder))) {
            Files.createDirectories(Paths.get(folder));
        }

        Path path = Paths.get(folder + image.getOriginalFilename());
        Files.write(path, bytes);

        return "http://localhost:8080/images/" + type + "/" + brandName + "/" + image.getOriginalFilename();
    }

    public boolean deleteImage(String brandName, String fileName, String type) throws IOException {
        String folder = "src/main/resources/static/images/" + type + "/" + brandName + "/";
        Path filePath = get(folder).toAbsolutePath().normalize().resolve(fileName);
        final File file = new File(String.valueOf(filePath));
        return file.delete();
    }

}
