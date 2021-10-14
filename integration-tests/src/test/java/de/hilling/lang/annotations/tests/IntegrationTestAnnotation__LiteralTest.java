package de.hilling.lang.annotations.tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntegrationTestAnnotation__LiteralTest {

    private static final IntegrationTestAnnotation__Literal CUSTOM_LITERAL_UNDER_TEST =
            new IntegrationTestAnnotation__Literal("Hilling", "Gunnar", 42, Object.class);

    private static final Deprecated__Literal DEPRECRATED_LITERAL_UNDER_TEST =
            new Deprecated__Literal("2.3", true);

    @Test
    void isAnnotation() {
        assertTrue(CUSTOM_LITERAL_UNDER_TEST instanceof IntegrationTestAnnotation);
    }

    @Test
    void valuesSet() {
        assertEquals("Hilling", CUSTOM_LITERAL_UNDER_TEST.value());
    }

    @Test
    void isDeprecatedAnnotation() {
        assertTrue(DEPRECRATED_LITERAL_UNDER_TEST instanceof Deprecated);
    }
}
