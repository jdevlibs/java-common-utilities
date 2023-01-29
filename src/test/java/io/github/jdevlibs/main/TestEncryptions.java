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
        data = Encryptions.encryptAES(data);
        System.out.println(data);
    }
}
