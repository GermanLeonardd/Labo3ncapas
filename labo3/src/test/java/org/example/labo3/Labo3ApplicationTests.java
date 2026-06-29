package org.example.labo3;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled("Requiere conexión real a base de datos; no aplica para los tests unitarios del CI")
@SpringBootTest
class Labo3ApplicationTests {

    @Test
    void contextLoads() {
    }
}