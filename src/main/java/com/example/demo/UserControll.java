package com.example.demo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins ="https://foodfixer.vercel.app")
public class UserControll {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists!");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists!");
        }
        if(userRepository.existsByUserId(user.getUserId()))
        {
        	return ResponseEntity.badRequest().body("UserId already exists");
        }

        // In production, you should hash the password before saving
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user)
    {
    	String username = user.getUsername().trim();
    	String password = user.getPassword().trim();
    	String role = user.getRole().trim();
    	
    	Optional<User> matchuser = userRepository.findByUsername(username);
    	
    	if(matchuser.isPresent())
    	{
    		User dbUser = matchuser.get();
    		
    		if(dbUser.getPassword().equals(password) && dbUser.getRole().equals(role) ){
    			return ResponseEntity.ok("Login successful!");
    		}
    		else {
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password or role!");
    		}
    	}
    	else
    	{
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username not found!");
    	}
    }
}


