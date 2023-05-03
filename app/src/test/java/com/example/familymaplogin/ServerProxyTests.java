package com.example.familymaplogin;

import org.junit.*;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import Responses.ResultEventAll;
import Responses.ResultPerson;
import Responses.ResultPersonAll;
import Utilities.*;
import static org.junit.Assert.*;

import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.net.URL;

import Responses.Result;
import Responses.ResultLogin;
import Responses.ResultRegister;
import Utilities.ServerProxy;

public class ServerProxyTests {
    ServerProxy serverProxy;
    @Before
    public void setUP(){
        serverProxy = new ServerProxy("localhost", "8080");
        serverProxy.clear();
    }
    @Test
    @DisplayName("Register Test")
    public void RegisterTest(){
        Result result = serverProxy.registerUser("Stevemaster9", "mypass", "email@email.com", "Steve", "Daniels",
                "M");
        assertTrue(result.getWorked());
    }
    @Test
    @DisplayName("Clear Test")
    public void ClearTest(){
        Result result = serverProxy.clear();
        assertTrue(result.getWorked());
    }

    @Test
    @DisplayName("Login Test")
    public void loginTest(){
        Result result = serverProxy.registerUser("Stevemaster9", "mypass", "email@email.com", "Steve", "Daniels",
                "M");
        Result resultLogin = serverProxy.loginUser("Stevemaster9","mypass");
        System.out.print(resultLogin.getMessage());
    }

    @Test
    @DisplayName("getAllUserPersons Test")
    public void getAllUserPersonsTest(){
        Result result = serverProxy.registerUser("Stevemaster9", "mypass", "email@email.com", "Steve", "Daniels",
        "M");
        Gson gson = new Gson();
        ResultRegister registerObject = gson.fromJson(result.getMessage(), ResultRegister.class);
        String tempString = serverProxy.getAllUserPersons(registerObject.getAuthtoken());
        assertNotNull(tempString);
    }

    @Test
    @DisplayName("getAllUserEvents Test")
    public void getAllUserEventsTest(){
        Result result = serverProxy.registerUser("Stevemaster9", "mypass", "email@email.com", "Steve", "Daniels",
                "M");
        Gson gson = new Gson();
        ResultRegister registerObject = gson.fromJson(result.getMessage(), ResultRegister.class);
        String tempString = serverProxy.getAllUserEvents(registerObject.getAuthtoken());
        assertNotNull(tempString);
    }

    @Test
    @DisplayName("getPersonURL Test")
    public void getPersonURLTest() throws MalformedURLException {
        Result result = serverProxy.registerUser("Stevemaster9", "mypass", "email@email.com", "Steve", "Daniels",
                "M");
        Gson gson = new Gson();
        ResultRegister registerObject = gson.fromJson(result.getMessage(), ResultRegister.class);

        URL url = new URL("http://localhost:8080/");
        ResultPerson resultPerson = serverProxy.getPersonURL(url, registerObject.getAuthtoken());
        assertTrue(resultPerson.getWorked());
        assertNotNull(resultPerson.getMessage());
    }

    @Test
    @DisplayName("getAllPeopleUrl Test")
    public void getAllPeopleUrlTest(){
        Result result = serverProxy.registerUser("Stevemaster9", "mypass", "email@email.com", "Steve", "Daniels",
                "M");
        Gson gson = new Gson();
        ResultRegister registerObject = gson.fromJson(result.getMessage(), ResultRegister.class);

        URL url = null;
        try {
            url = new URL("http://localhost:8080/person");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ResultPersonAll resultPerson = serverProxy.getAllPeopleUrl(url, registerObject.getAuthtoken());
        assertTrue(resultPerson.getWorked());
        assertNotNull(resultPerson.getMessage());
    }
    @Test
    @DisplayName("getAllEventsUrl Test")
    public void getAllEventsUrlTest(){
        Result result = serverProxy.registerUser("Stevemaster9", "mypass", "email@email.com", "Steve", "Daniels",
                "M");
        Gson gson = new Gson();
        ResultRegister registerObject = gson.fromJson(result.getMessage(), ResultRegister.class);

        URL url = null;
        try {
            url = new URL("http://localhost:8080/event");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ResultEventAll resultPerson = serverProxy.getAllEventsUrl(url, registerObject.getAuthtoken());
        assertTrue(resultPerson.getWorked());
        assertNotNull(resultPerson.getMessage());
    }
}
