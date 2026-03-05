package com.PruebaC.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.PruebaC.Model.UserModel;
import com.PruebaC.Service.UserService;


@RestController
@RequestMapping(path = "/users")
@CrossOrigin
public class UserController {
	

	
	@Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<?> listar(
        @RequestParam(required = false) String sortedBy, 
        @RequestParam(required = false) String filter) { 

        List<UserModel> resultado;

        if (filter != null && !filter.isEmpty()) {
            resultado = service.filtrar(filter);
        } else {
            resultado = service.listarOrdenado(sortedBy);
        }

        return ResponseEntity.ok(resultado);
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody UserModel user) {
        if (!service.validarRFC(user.getTax_id())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid tax format");
        }
        if (service.existeTaxId(user.getTax_id())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Tax already exists");
        }
        if (!service.validarPhone(user.getPhone())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid phone format");
        }
        service.guardar(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Success!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable String id) {
        if (service.eliminar(id)) {
            return ResponseEntity.ok("Deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patch(@PathVariable String id, @RequestBody UserModel cambios) {
        if (service.patch(id, cambios)) {
            return ResponseEntity.ok("Updated");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
    }
    
}
