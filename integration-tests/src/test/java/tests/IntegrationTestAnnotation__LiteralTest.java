package tests;

import de.hilling.lang.annotations.tests.Deprecated__Literal;
import de.hilling.lang.annotations.tests.IntegrationTestAnnotation;
import de.hilling.lang.annotations.tests.IntegrationTestAnnotation__Literal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegrationTestAnnotation__LiteralTest {

    private static final IntegrationTestAnnotation__Literal CUSTOM_LITERAL_UNDER_TEST =
            new IntegrationTestAnnotation__Literal("Hilling", "Gunnar", 42, Object.class);

    private static final Deprecated__Literal DEPRECRATED_LITERAL_UNDER_TEST =
            new Deprecated__Literal("2.3", true);

    @Test
    public void isAnnotation() {
        assertTrue(CUSTOM_LITERAL_UNDER_TEST instanceof IntegrationTestAnnotation);
    }

    @Test
    public void valuesSet() {
        assertEquals("Hilling", CUSTOM_LITERAL_UNDER_TEST.value());
    }

    @Test
    public void isDeprecatedAnnotation() {
        assertTrue(DEPRECRATED_LITERAL_UNDER_TEST instanceof Deprecated);
    }
}
