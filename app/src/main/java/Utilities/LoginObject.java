package Utilities;

import model.AuthTokenModel;
/*
This class had to be created for the sake of storing the Server port and such in the request
 */
public class LoginObject {
    private String username;
    private String password;
    private AuthTokenModel authTokenModel;
    private String hostName;
    private String portNumber;

    public LoginObject(){}
    public LoginObject(String hostName, AuthTokenModel authTokenModel, String portNumber){
        this.portNumber = portNumber;
        this.authTokenModel = authTokenModel;
        this.hostName = hostName;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthTokenModel getAuthTokenModel() {
        return authTokenModel;
    }

    public void setAuthTokenModel(AuthTokenModel authTokenModel) {
        this.authTokenModel = authTokenModel;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }
}
