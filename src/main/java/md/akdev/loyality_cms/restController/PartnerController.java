package md.akdev.loyality_cms.restController;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import md.akdev.loyality_cms.model.ImageType;
import md.akdev.loyality_cms.model.partners.Partner;
import md.akdev.loyality_cms.model.partners.PartnerImage;
import md.akdev.loyality_cms.repository.PartnerImageRepository;
import md.akdev.loyality_cms.service.PartnerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/partners")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerImageRepository partnerImageRepository;
    private final PartnerService partnerService;

    @PostMapping("/image")
    public ResponseEntity<PartnerImage> uploadPartnerImage(@RequestParam("file") MultipartFile file, String type,
                                                           @Value("${app.upload.partner-images}") String uploadPath) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadPath).resolve(fileName);

            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            PartnerImage partnerImage = new PartnerImage();
            partnerImage.setName(fileName);
            partnerImage.setType(ImageType.valueOf(type));

            PartnerImage savedImage = partnerImageRepository.save(partnerImage);

            return ResponseEntity.ok(savedImage);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Partner>> getAllPartners() {
        Iterable<Partner> partners = partnerService.getAllPartners();
        return ResponseEntity.ok(partners);
    }

    @GetMapping("/partner-images/{filename}")
    public ResponseEntity<byte[]> getPartnerImage(@PathVariable String filename, @Value("${app.upload.partner-images}") String uploadPath) {

        try {
            File file;

            file = verifyPathAndFileExists(uploadPath, filename);

            byte[] fileContent = getBytes(file);

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.IMAGE_JPEG)
                    .header("Content-Disposition", file.getName() )
                    .body(fileContent);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }


    private File verifyPathAndFileExists(String path, String filename) throws FileNotFoundException {
        File folder = new File(path);

        if(!folder.exists() || !folder.isDirectory()){
            throw new FileNotFoundException("Folder " + path+ "  not found");
        }

        File file = new File(path +"/"+ filename);
        // Check if file exists, todo maybe a good idea is to return no-photo.jpg ?
        if(!file.exists() || !file.isFile()){
            throw new FileNotFoundException("File "+ filename +" not found");
        }
        return file;
    }


    private static byte @NotNull [] getBytes(File file) throws IOException {
        byte[] fileContent = new byte[(int) file.length()];

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            int bytesRead;

            int totalBytesRead = 0;

            while ((bytesRead = fileInputStream.read(fileContent, totalBytesRead, fileContent.length - totalBytesRead)) != -1) {
                totalBytesRead += bytesRead;

                if (totalBytesRead == fileContent.length) {
                    break;
                }
            }
        }
        return fileContent;
    }

}


