package md.akdev.loyality_cms.service;

import de.brendamour.jpasskit.PKBarcode;
import de.brendamour.jpasskit.PKField;
import de.brendamour.jpasskit.PKPass;
import de.brendamour.jpasskit.enums.PKBarcodeFormat;
import de.brendamour.jpasskit.passes.PKGenericPass;
import de.brendamour.jpasskit.passes.PKStoreCard;
import de.brendamour.jpasskit.signing.PKFileBasedSigningUtil;
import de.brendamour.jpasskit.signing.PKSigningInformation;
import de.brendamour.jpasskit.signing.PKSigningInformationUtil;

import lombok.RequiredArgsConstructor;
import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.repository.ClientsRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AppleWalletService {
    private final ClientsRepository clientsRepository;
    @Value("${wallet.apple.passTypeId}")
    private String passTypeId;

    @Value("${wallet.apple.teamId}")
    private String teamId;

    @Value("${wallet.apple.cert.path}")
    private String certPath;

    @Value("${wallet.apple.cert.password}")
    private String certPassword;
    @Value("${wallet.apple.cert.wwdr}")
    private  String wwdrCertPath;

    public byte[] generatePass() throws Exception {

        ClientsModel clientsModel = getLoggedInClientId().orElseThrow(() -> new Exception("Client not found"));

        PKField bonusField = PKField.builder()
                .key("Card Frumos")
                .label("")
                .value("")
                .build();
        PKField secondaryField = PKField.builder()
                .key("name")
                .label("Loyalty member")
                .value(clientsModel.getClientName())
                .build();

        PKGenericPass generic = PKStoreCard.builder()
                .primaryField(bonusField)
                .secondaryField(secondaryField)
                .build();


        PKPass pkPass = PKPass.builder()
                .passTypeIdentifier(passTypeId)
                .teamIdentifier(teamId)
                .serialNumber(clientsModel.getId().toString())
                .description("Card Frumos")
                .organizationName("Felica Healthcare Group")
                .formatVersion(1)
                .logoText("Card Frumos")
//                .backgroundColor(
//                        "rgb(2, 154, 168)"
//                )
                .foregroundColor(
                        "rgb(2, 154, 168)"
                )
                .labelColor(
                        "rgb(2, 154, 168)"
                )
                .pass(generic)
                .barcodeBuilder(
                        PKBarcode.builder()
                                .format(PKBarcodeFormat.PKBarcodeFormatCode128)
                                .message(clientsModel.getPhoneNumber())
                                .messageEncoding("iso-8859-1")
                                .altText("     "+clientsModel.getPhoneNumber())
                )

                .build();

//        String cert =  new ClassPathResource("cert/CardfrumosPass.p12").getPath();
//        String wwdr = new ClassPathResource("cert/WWDR.pem").getPath();
        ClassPathResource certResource = new ClassPathResource("cert/CardfrumosPass.p12");
        ClassPathResource wwdrResource = new ClassPathResource("cert/WWDR.pem");

        File tempCertFile = File.createTempFile("signing-cert", ".p12");
        try (InputStream in = certResource.getInputStream();
             OutputStream out = new FileOutputStream(tempCertFile)) {
            in.transferTo(out);
        }

        File tempWWDRFile = File.createTempFile("ww-dr", ".p12");
        try(InputStream in = wwdrResource.getInputStream();
            OutputStream out = new FileOutputStream(tempWWDRFile)){
                in.transferTo(out);
            }



        PKSigningInformation signingInformation = new PKSigningInformationUtil().loadSigningInformation(
                tempCertFile.getAbsolutePath(),
                certPassword, tempWWDRFile.getAbsolutePath()
        );

//        String assetsPath = "src/main/resources/assets";
//        String assetsPath = new  ClassPathResource( "/assets").getFile().getAbsolutePath();

        File tempDir = Files.createTempDirectory("pkpass-assets").toFile();
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources("classpath:/assets/*");
        for (Resource resource : resources) {
            if (resource.exists() && resource.isReadable()) {
                File targetFile = new File(tempDir, Objects.requireNonNull(resource.getFilename()));
                try (InputStream in = resource.getInputStream();
                     OutputStream out = new FileOutputStream(targetFile)) {
                    in.transferTo(out);
                }
            }
        }

        PKFileBasedSigningUtil signingUtil = new PKFileBasedSigningUtil();
      try {
          return signingUtil.createSignedAndZippedPkPassArchive(
                  pkPass,
                  tempDir.getAbsolutePath(),
                  signingInformation);
      }finally {
          FileUtils.deleteDirectory(tempDir);
          FileUtils.deleteQuietly(tempCertFile);
          FileUtils.deleteQuietly(tempWWDRFile);

      }


    }

    private Optional<ClientsModel> getLoggedInClientId() {
        return clientsRepository.getClientByUuid1c((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
