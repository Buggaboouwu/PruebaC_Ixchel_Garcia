package com.PruebaC.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PruebaC.Model.AddressModel;
import com.PruebaC.Model.UserModel;
import com.PruebaC.Security.AESUtil;

import jakarta.annotation.PostConstruct;


@Service
public class UserService {
	
	@Autowired
	private AESUtil aesUtil;

	private List<UserModel> usuarios = new ArrayList<>();
  	
  	ZonedDateTime madagascar = ZonedDateTime.now(ZoneId.of("Indian/Antananarivo"));
  	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
  	String fechaActual = madagascar.format(formatter);
  	
  	@PostConstruct
  	public void init() {
          usuarios.add(new UserModel(
              UUID.randomUUID().toString(), 
              "user1@mail.com",
              "user1",
              "+1 55 555 555 55",
              aesUtil.encriptar("password123"),
              "AARR990101XXX",
              fechaActual, 
              Arrays.asList( 
                  new AddressModel(1, "workaddress", "street No. 1", "UK"),
                  new AddressModel(2, "homeaddress", "street No. 2", "AU")
              )
          ));
              usuarios.add(new UserModel(
                  UUID.randomUUID().toString(),
                  "user2@mail.com",
                  "user2",
                  "+1 44 444 444 44",
                  // user1
                  aesUtil.encriptar("password321"),
                  "AARR990101123",
                  fechaActual,
                  Arrays.asList(
                      new AddressModel(1, "workaddress2", "street No. 12", "MX"),
                      new AddressModel(2, "homeaddress2", "street No. 22", "EUA")
                  )
              ));
              
              usuarios.add(new UserModel(
                      UUID.randomUUID().toString(),
                      "user3@mail.com",
                      "user3",
                      "+1 33 333 333 33",
                      // user2
                      aesUtil.encriptar("password231"),
                      "AARR990101321",
                      fechaActual,
                      Arrays.asList(
                          new AddressModel(1, "workaddress3", "street No. 13", "UK"),
                          new AddressModel(2, "homeaddress3", "street No. 23", "MX")
                      )
                  ));
  	

  }
  	
  	public List<UserModel> listar() {
  	    return usuarios.stream().map(u -> {
  	        UserModel copia = new UserModel(
  	            u.getId(), u.getEmail(), u.getName(),
  	            u.getPhone(), null, 
  	            u.getTax_id(), u.getCreated_at(), u.getAddresses()
  	        );
  	        return copia;
  	    }).collect(Collectors.toList());
  	}
  	 
  	 //Lista a-b
  	 public List<UserModel> listarOrdenado(String sortedBy) {
  	        if (sortedBy == null || sortedBy.isEmpty()) {
  	            return usuarios; 
  	        }
  	        return usuarios.stream().sorted((a, b) -> { 
  	            switch (sortedBy) {
  	                case "email": return a.getEmail().compareTo(b.getEmail());
  	                case "name": return a.getName().compareTo(b.getName());
  	                case "phone": return a.getPhone().compareTo(b.getPhone());
  	                case "tax_id": return a.getTax_id().compareTo(b.getTax_id());
  	                case "created_at": return a.getCreated_at().compareTo(b.getCreated_at());
  	                case "id": return a.getId().compareTo(b.getId());
  	                default: return 0;
  	            }
  	        }).collect(Collectors.toList());
  	    }
  	 
  	 public List<UserModel> filtrar(String filter) {
  	        String[] partes = filter.split("\\+"); 
  	        String campo = partes[0];
  	        String operador = partes[1];
  	        String valor = partes[2];

  	        return usuarios.stream().filter(u -> {
  	            String campoValor = switch (campo) {
  	                case "email" -> u.getEmail();
  	                case "name" -> u.getName();
  	                case "phone" -> u.getPhone();
  	                case "tax_id" -> u.getTax_id();
  	                case "created_at" -> u.getCreated_at();
  	                case "id" -> u.getId();
  	                default -> "";
  	            };
  	            return switch (operador) {
  	                case "co" -> campoValor.contains(valor); 
  	                case "eq" -> campoValor.equals(valor); 
  	                case "sw" -> campoValor.startsWith(valor); 
  	                case "ew" -> campoValor.endsWith(valor);
  	                default -> false;
  	            };
  	        }).collect(Collectors.toList());
  	    }
  	 
  	 
  	 		public void guardar(UserModel user) {
  		    user.setId(UUID.randomUUID().toString());
  		    user.setPassword(aesUtil.encriptar(user.getPassword()));
  		    ZonedDateTime madagascar = ZonedDateTime.now(ZoneId.of("Indian/Antananarivo"));
  		    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
  		    user.setCreated_at(madagascar.format(formatter));
  		    usuarios.add(user);
  		    
  	 		}
  		    
  		    
  		    public boolean eliminar(String id) {
  		        return usuarios.removeIf(u -> u.getId().equals(id));
  		    }	
  		    
  		    
  		    public boolean patch(String id, UserModel cambios) {
  		        for (UserModel u : usuarios) {
  		            if (u.getId().equals(id)) {
  		                if (cambios.getEmail() != null) u.setEmail(cambios.getEmail());
  		                if (cambios.getName() != null) u.setName(cambios.getName());
  		                if (cambios.getPhone() != null) u.setPhone(cambios.getPhone());
  		                if (cambios.getTax_id() != null) u.setTax_id(cambios.getTax_id());
  		                if (cambios.getPassword() != null) u.setPassword(cambios.getPassword());
  		                if (cambios.getAddresses() != null) u.setAddresses(cambios.getAddresses());
  		                return true;
  		            }
  		        }
  		        return false;
  		    }

  		    public UserModel buscarPorTaxId(String tax_id) {
  		        return usuarios.stream()
  		            .filter(u -> u.getTax_id().equals(tax_id))
  		            .findFirst()
  		            .orElse(null);
  		    }
  		    
  		  public boolean existeTaxId(String tax_id) {
  		    return usuarios.stream()
  		        .anyMatch(u -> u.getTax_id().equals(tax_id));
  		}
  		    
  		  
  		public boolean validarRFC(String tax_id) {
  		    String patron = "^[A-ZÑ&]{3,4}[0-9]{6}[A-Z0-9]{3}$";
  		    return tax_id != null && tax_id.matches(patron);
  		}
  		
  		public boolean validarPhone(String phone) {
  		    if (phone == null) return false;
  		    String soloDigitos = phone.replaceAll("(\\+\\d{1,3}\\s?)", "")
  		                              .replaceAll("[\\s-]", "");
  		    return soloDigitos.length() == 10;
  		}
		}
