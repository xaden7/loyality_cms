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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
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

        String cert =  new ClassPathResource("/cert/CardfrumosPass.p12").getPath();
        String wwdr = new ClassPathResource("/cert/WWDR.pem").getPath();

        PKSigningInformation signingInformation = new PKSigningInformationUtil().loadSigningInformation(
                cert,
                certPassword, wwdr
        );

//        String assetsPath = "src/main/resources/assets";
        String assetsPath = new  ClassPathResource( "/assets").getFile().getAbsolutePath();

        PKFileBasedSigningUtil signingUtil = new PKFileBasedSigningUtil();

        return signingUtil.createSignedAndZippedPkPassArchive(
                pkPass,
                assetsPath,
                signingInformation
        );

    }

    private Optional<ClientsModel> getLoggedInClientId() {
        return clientsRepository.getClientByUuid1c((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
