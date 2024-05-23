package io.github.jdevlibs.main;

import io.github.jdevlibs.utils.unique.ULIDGenerator;

/**
 * @author supot.jdev
 * @version 1.0
 */
public class ULIDGeneratorTest {
    public static void main(String[] args) {
        ULIDGenerator generator = new ULIDGenerator();
        int max = 1000;
        for (int i = 0; i < max; i++) {
            String id = generator.nextULID();
            System.out.println(id);
        }
    }
}
