package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void busServices_areConfigured() {
        BusService jotur = BusService.JOTUR;
        assertNotNull(jotur);
        assertEquals("https://www.jotur.com.br/horarios/", jotur.getUrl());
        assertTrue(jotur.hasMap());

        BusService fenix = BusService.CONSORCIO_FENIX;
        assertNotNull(fenix);
        assertEquals("https://www.consorciofenix.com.br/horarios", fenix.getUrl());
    }
}
