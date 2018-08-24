package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.hilling.lang.annotations.tests.IntegrationTestAnnotation;
import de.hilling.lang.annotations.tests.IntegrationTestAnnotation__Literal;

public class IntegrationTestAnnotation__LiteralTest {

    private static final IntegrationTestAnnotation__Literal LITERAL_UNDER_TEST =
            new IntegrationTestAnnotation__Literal("Hilling", "Gunnar", 42, Object.class);

    @Test
    public void isAnnotation() {
        assertTrue(LITERAL_UNDER_TEST instanceof IntegrationTestAnnotation);
    }

    @Test
    public void valuesSet() {
        assertEquals("Hilling", LITERAL_UNDER_TEST.value());
    }
}
