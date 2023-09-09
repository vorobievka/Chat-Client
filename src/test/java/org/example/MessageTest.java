package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void testToString() {
        Message m = new Message(1694249809,"Kate","Cool");
        String actual = m.toString();
        String expected = "# Sat Sep 09 11:56:49  Kate: Cool ";
        assertEquals(expected, actual);
    }
}