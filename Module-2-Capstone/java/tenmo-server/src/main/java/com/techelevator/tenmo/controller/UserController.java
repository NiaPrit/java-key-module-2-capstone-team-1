package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    UserDAO userData;

    public UserController(UserDAO userData) {
        this.userData = userData;
    }

    @RequestMapping(path = "/users" ,method = RequestMethod.GET)
    public List<User> getAllUsers (){
        List<User> theUsers = new ArrayList<>();
        try {theUsers = userData.findAll();}
        catch (NullPointerException ex) {
            System.out.println("No Users Exist");
        }
        return theUsers;
    }

    @RequestMapping(path = "/user/{id}/username",method = RequestMethod.GET)
    public String getName (@PathVariable int id ){
      String getName = userData.findUsernameById(id);
      return getName;
    }
}
