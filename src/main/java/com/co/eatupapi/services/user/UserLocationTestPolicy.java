package com.co.eatupapi.services.user;

import java.util.UUID;

/**
 * Temporary location policy for manual testing.
 * Replace this fixed value when real location integration is implemented.
 */
public final class UserLocationTestPolicy {

    public static final String FIXED_TEST_LOCATION_ID = "11111111-1111-1111-1111-111111111111";

    private UserLocationTestPolicy() {
    }

    public static UUID fixedTestLocationUuid() {
        return UUID.fromString(FIXED_TEST_LOCATION_ID);
    }
}
