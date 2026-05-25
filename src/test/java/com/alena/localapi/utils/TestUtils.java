package com.alena.localapi.utils;

import java.util.UUID;

public final class TestUtils {
    private TestUtils() {
    }

    public static String generateRandomEmail() {
        return "email_" + UUID.randomUUID().toString().substring(0, 8) + "@mail.com";
    }
}