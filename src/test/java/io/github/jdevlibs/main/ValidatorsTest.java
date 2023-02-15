package io.github.jdevlibs.main;

import io.github.jdevlibs.utils.Validators;

import java.util.Optional;

/**
 * @author supot.jdev
 * @version 1.0
 */
public class ValidatorsTest {
    public static void main(String[] args) {
        Optional<String> op = Optional.of("11");
        System.out.println("op : " + Validators.isEmpty(op));
        op = Optional.empty();
        System.out.println("op : " + Validators.isEmpty(op));

        System.out.println("2570300022001 : " + Validators.isThaiCitizenId("2570300022001"));
        System.out.println("2570300022002 : " + Validators.isThaiCitizenId("2570300022002"));
    }
}
