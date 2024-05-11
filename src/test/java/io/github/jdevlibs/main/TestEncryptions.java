package io.github.jdevlibs.main;

import io.github.jdevlibs.utils.Encryptions;
import io.github.jdevlibs.utils.exception.SystemException;

/**
 * @author supot.jdev
 * @version 1.0
 */
public class TestEncryptions {
    public static void main(String[] args) throws SystemException {
        String data = "Mr. Supot Saelao";
        System.out.println(data);

        String dataEn1 = Encryptions.encryptAES(data, "ENCRYPTION_DEFAULT_SECRET_KEY");
        System.out.println("encryptAES1 : ");
        System.out.println(dataEn1);

        String dataDecode1 = Encryptions.decryptAES(dataEn1, "ENCRYPTION_DEFAULT_SECRET_KEY");
        System.out.println("decryptAES1 : ");
        System.out.println(dataDecode1);

        String dataEn2 = Encryptions.encryptAES(data, "ENCRYPTION_DEFAULT_SECRET_KEY");
        System.out.println("encryptAES2 : ");
        System.out.println(dataEn2);

        String dataDecode2 = Encryptions.decryptAES(dataEn2, "ENCRYPTION_DEFAULT_SECRET_KEY");
        System.out.println("decryptAES2 : ");
        System.out.println(dataDecode2);
    }
}
