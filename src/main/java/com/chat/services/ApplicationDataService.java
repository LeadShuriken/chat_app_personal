package com.chat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.chat.exceptions.UserException;

@Service
public class ApplicationDataService {

    private final UserDataService userDataService;

    @Autowired
    public ApplicationDataService(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    public List<String> getAllUsers() {
        return userDataService.getUsers();
    }

    public void addNewUser(String name) throws UserException {
        if (userDataService.isUserNameTaken(name)) {
            throw new UserException("User exists");
        }
        userDataService.setUser(name);
    }
}
