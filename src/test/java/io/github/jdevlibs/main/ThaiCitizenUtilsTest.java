package io.github.jdevlibs.main;

import io.github.jdevlibs.utils.ThaiCitizenUtils;

/**
 * @author supot.jdev
 * @version 1.0
 */
public class ThaiCitizenUtilsTest {
    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            String idCard = ThaiCitizenUtils.generateThaiCitizenId();
            System.out.println(idCard + " : " + ThaiCitizenUtils.isThaiCitizenId(idCard));
        }
    }
}
