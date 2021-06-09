package com.chat.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.chat.exceptions.UserException;

@Service
public class UserDataService {

    private final UserDataRepository userDataService;

    @Autowired
    public UserDataService(UserDataRepository userDataService) {
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

    public boolean userExists(String name) {
        return userDataService.isUserNameTaken(name);
    }
}
