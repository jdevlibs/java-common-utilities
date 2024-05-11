package io.github.jdevlibs.main;

import io.github.jdevlibs.utils.Encryptions;
import io.github.jdevlibs.utils.exception.SystemException;

/**
 * @author supot.jdev
 * @version 1.0
 */
public class TestEncryptionsRSA {
    public static void main(String[] args) throws SystemException {
        String data = "Mr. Supot Saelao";
        System.out.println(data);

        Encryptions.KeyPairValue keyPairValue = Encryptions.generateKeyPair();
        System.out.println("Private Key: ");
        System.out.println(keyPairValue.getPrivateKey());

        System.out.println("Public Key: ");
        System.out.println(keyPairValue.getPublicKey());

        String dataEn = Encryptions.encryptRSA(data, keyPairValue.getPublicKey());
        System.out.println("encryptRSA1 : ");
        System.out.println(dataEn);

        data = Encryptions.decryptRSA(dataEn, keyPairValue.getPrivateKey());
        System.out.println("decryptRSA1 : ");
        System.out.println(data);

        dataEn = Encryptions.encryptRSA(data, keyPairValue.getPublicKey());
        System.out.println("encryptRSA2 : ");
        System.out.println(dataEn);

        data = Encryptions.decryptRSA(dataEn, keyPairValue.getPrivateKey());
        System.out.println("decryptRSA3 : ");
        System.out.println(data);
    }
}
