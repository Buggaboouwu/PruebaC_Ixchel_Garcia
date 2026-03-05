package com.PruebaC;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.PruebaC.Model.UserModel;
import com.PruebaC.Service.UserService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) 
public class UserServiceTest {
	
	@Autowired
    private UserService service;

    @Test
    @Order(1)
    public void testListar() {
        List<UserModel> usuarios = service.listar();
        assertFalse(usuarios.isEmpty());
        assertEquals(3, usuarios.size());
    }

    @Test
    public void testValidarRFCCorrecto() {
        assertTrue(service.validarRFC("AARR990101XXX"));
    }

    @Test
    public void testValidarRFCIncorrecto() {
        assertFalse(service.validarRFC("123"));
    }

    @Test
    @Order(2)
    public void testExisteTaxId() {
        assertTrue(service.existeTaxId("AARR990101XXX"));
    }

    @Test
    public void testNoExisteTaxId() {
        assertFalse(service.existeTaxId("ZZZZ999999ZZZ"));
    }

    @Test
    @Order(3)
    public void testEliminar() {
      
        String idReal = service.buscarPorTaxId("AARR990101XXX").getId();
        assertTrue(service.eliminar(idReal));
    }

    @Test
    public void testEliminarNoExiste() {
        assertFalse(service.eliminar("id-que-no-existe"));
    }

}
