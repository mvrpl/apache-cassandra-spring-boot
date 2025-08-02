package mvrpl.dev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import mvrpl.dev.model.User;
import mvrpl.dev.model.Pessoas;

@RestController
public class UserController {

    @Autowired(required = true)
	private Pessoas pessoas;

    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = "application/json")
    public List<User> getAllUsers() {
        return pessoas.findAll();
    }

    @RequestMapping(value = "/user/{email}", method = RequestMethod.GET, produces = "application/json")
    public User getUserByEmail(@PathVariable String email) {
        return pessoas.findByEmail(email).orElse(null);
    }
}