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

    @Test
    public void testUsernameValido() {
        assertTrue(ValidationUtils.isValidUsername("mario"));
        assertTrue(ValidationUtils.isValidUsername("a"));
        assertTrue(ValidationUtils.isValidUsername("a".repeat(ValidationUtils.USERNAME_MAX_LENGTH)));
    }

    @Test
    public void testUsernameNonValido() {
        assertFalse(ValidationUtils.isValidUsername(null));
        assertFalse(ValidationUtils.isValidUsername(""));
        assertFalse(ValidationUtils.isValidUsername("   "));
        assertFalse(ValidationUtils.isValidUsername("a".repeat(ValidationUtils.USERNAME_MAX_LENGTH + 1)));
    }

    @Test
    public void testPasswordValida() {
        assertTrue(ValidationUtils.isValidPassword("password1"));
        assertTrue(ValidationUtils.isValidPassword("Abcdefg1"));
        assertTrue(ValidationUtils.isValidPassword("1".repeat(ValidationUtils.PASSWORD_MIN_LENGTH) + "a"));
    }

    @Test
    public void testPasswordNonValida() {
        assertFalse(ValidationUtils.isValidPassword(null));
        assertFalse(ValidationUtils.isValidPassword(""));
        assertFalse(ValidationUtils.isValidPassword("corta1"));
        assertFalse(ValidationUtils.isValidPassword("soltantoLettere"));
        assertFalse(ValidationUtils.isValidPassword("12345678"));
    }
}
