package it.unisa.diem.wordageddong17.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilsTest {

    @Test
    public void testEmailValida() {
        assertTrue(ValidationUtils.isValidEmail("mario.rossi@gmail.com"));
        assertTrue(ValidationUtils.isValidEmail("user+test@sub.example.org"));
        assertTrue(ValidationUtils.isValidEmail("admin123@wordageddon.it"));
    }

    @Test
    public void testEmailNonValida() {
        assertFalse(ValidationUtils.isValidEmail(null));
        assertFalse(ValidationUtils.isValidEmail(""));
        assertFalse(ValidationUtils.isValidEmail("   "));
        assertFalse(ValidationUtils.isValidEmail("senzaChiocciola.com"));
        assertFalse(ValidationUtils.isValidEmail("@mancanteUsername.it"));
        assertFalse(ValidationUtils.isValidEmail("mario@"));
        assertFalse(ValidationUtils.isValidEmail("mario@.com"));
        assertFalse(ValidationUtils.isValidEmail("mario@com"));
    }
}
