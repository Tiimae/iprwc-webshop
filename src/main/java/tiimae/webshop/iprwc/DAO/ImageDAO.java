package tiimae.webshop.iprwc.DAO;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import tiimae.webshop.iprwc.IprwcApplication;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Paths.get;

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

    public String getImage(String path, String brandName, String fileName) throws IOException {
        String folder = "src/main/resources/images/brand/" + brandName + "/";
        Path filePath = get(folder).toAbsolutePath().normalize().resolve(fileName);
        return filePath.toString();
//        Resource resource = new UrlResource(filePath.toUri());
//        return resource;
    }

}
