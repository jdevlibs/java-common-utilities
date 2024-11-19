package io.github.jdevlibs.utils;

import io.github.jdevlibs.utils.unique.SnowFlakeGenerator;
import io.github.jdevlibs.utils.unique.ULIDGenerator;

import java.util.UUID;

/**
 * @author supot.jdev
 * @version 1.0
 */
public final class GeneratorUtils {
    private static final SnowFlakeGenerator SNOW_FLAKE_GENERATOR = new SnowFlakeGenerator();
    private static final ULIDGenerator ULID_GENERATOR = new ULIDGenerator();

    private GeneratorUtils() {

    }

    public static Long getSnowFlakeId() {
        return SNOW_FLAKE_GENERATOR.nextId();
    }

    public static String getSnowFlakeIdString() {
        return SNOW_FLAKE_GENERATOR.nextIdString();
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().toUpperCase();
    }

    public static String getULID() {
        return ULID_GENERATOR.nextULID();
    }

    public static String getULID(long timestamp) {
        return ULID_GENERATOR.nextULID(timestamp);
    }
}
