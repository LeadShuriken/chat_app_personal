package com.chat.controllers;

import java.util.List;

import com.chat.annotations.SQLInjectionSafe;
import com.chat.exceptions.UserException;
import com.chat.services.ApplicationDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.version}")
public class UsersController {

    @Autowired
    private ApplicationDataService applicationDataService;

    @GetMapping(value = "${api.registration}/{userName}")
    public ResponseEntity<Void> register(@PathVariable @SQLInjectionSafe String userName) {
        System.out.println("Register request: " + userName);
        if (!applicationDataService.userExists(userName)) {
            try {
                applicationDataService.addNewUser(userName);
            } catch (UserException e) {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "${api.getAll}")
    public List<String> getAll() {
        return applicationDataService.getAllUsers();
    }
}
