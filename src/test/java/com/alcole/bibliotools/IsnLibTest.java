package com.alcole.bibliotools;

import static org.junit.Assert.*;

import org.junit.Test;

import static com.alcole.bibliotools.IsnLib.*;

/**
 * Created by alex on 05/11/16.
 */
public class IsnLibTest {

    @Test
    public void testCanonicalForm() {
        assertEquals("978140885565", canonicalForm("978140885565 "));
        assertEquals("9783161484100", canonicalForm("978-3-16-148410-0"));
        assertEquals("978140885565", canonicalForm("978140885565"));
    }

    @Test
    public void testPadLength() {
        assertEquals(8, pad("14664", 8).length());
        assertEquals(10, pad("14664", 10).length());
    }

    @Test
    public void testPadResult() {
        assertEquals("00014664", pad("14664", 8));
        assertEquals("0000014664", pad("14664", 10));
    }

    @Test
    public void testValidateIsn() {
        assertTrue(validateIsn("9781408855652"));
        assertTrue(validateIsn("00014664"));
        assertTrue(validateIsn("978-0805071665"));
        assertTrue(validateIsn("0805071660"));
        assertTrue(validateIsn("9772434561006"));

        assertFalse(validateIsn("0805071661"));
        assertFalse("invalid form", validateIsn("0-8-0-5-0-7-1-6-6-1"));

        assertTrue(validateIsn("2434561X"));
        assertTrue(validateIsn("2434561x"));
    }

    @Test
    public void testGenerateCheck() {
        assertEquals("valid check digit", '2', generateCheck("978140885565"));
        assertEquals('X', generateCheck("2434561X"));
        assertEquals('X', generateCheck("2434561"));
        assertEquals('1', generateCheck("184854957"));
    }

    @Test
    public void testIsbn10To13String() {
        assertEquals("9780262510875", isbn10To13("0262510871"));
    }

    @Test
    public void testIsbn10To13StringString() {
        assertEquals("9780262510875", isbn10To13("978", "0262510871"));
    }

    @Test
    public void testLostLeadingZeroes() {
        assertTrue(lostLeadingZeroes("306406152", IdentifierType.ISBN10));
        assertTrue(lostLeadingZeroes("14664", IdentifierType.ISSN));
        assertFalse(lostLeadingZeroes("4664", IdentifierType.ISSN));
    }

    @Test
    public void testIssnFromEan13() {
        assertEquals("2434561X", issnFromEan13("9772434561006"));
    }

    @Test
    public void testGetType() {
        assertEquals(IdentifierType.ISSN, getType("00014664"));
        assertEquals(IdentifierType.ISBN13, getType("9780262510875"));
        assertEquals(IdentifierType.ISMN, getType("9790260000438"));
        assertEquals(IdentifierType.ISSNEAN13, getType("9772434561006"));
        assertEquals(IdentifierType.ISSNEAN13.toString(), getType("9772434561006").toString());
    }

}
