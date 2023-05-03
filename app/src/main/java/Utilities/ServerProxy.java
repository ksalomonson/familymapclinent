package Utilities;

import android.util.Log;

import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import Responses.*;
import model.PersonsModel;

public class ServerProxy {
    private final String serverHost;
    private final String serverPort;

    public ServerProxy(String serverHost, String serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public Result registerUser(String username, String password, String email,
                                       String firstName, String lastName, String gender) {

        // request data
        String jStringReq = "{\"username\":\"" + username + "\", \"password\":\"" + password +
                "\", \"email\":\"" + email + "\", \"firstName\":\"" + firstName +
                "\", \"lastName\":\"" + lastName + "\", \"gender\":\"" + gender + "\"}";

        return serverPost(jStringReq, "register", ResultRegister.class);
    }

    public Result loginUser(String username, String password) {

        //request data
        String jRequestString = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";

        return serverPost(jRequestString, "login", ResultLogin.class);
    }
    public Result clear() {
        return (Result) serverPost(null, "clear", Result.class);
    }

    public String getAllUserPersons(String authToken) {
        return serverGet(authToken, "person");
    }

    public String getAllUserEvents(String authToken) {
        return serverGet(authToken, "event");
    }

    private Result serverPost(String reqData, String apiName, Class resultClass) {
        Result result = new Result(null);

        try {
            HttpURLConnection http = openHttpURLConnection("user/" + apiName);
            http.setRequestMethod("POST");
            if(reqData != null) {
                // There is a request body
                http.setDoOutput(true);

                http.connect();

                OutputStream outputStream = http.getOutputStream();

                writeString(reqData, outputStream);

                outputStream.close();
            } else{
                // There is not a request body
                //This should only be called when clearing the database. Really only left in for testing
                http.setDoOutput(true);

                http.connect();
                http = openHttpURLConnection(apiName);
                http.setRequestMethod("POST");          //this redundancy fixes an error

            }
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                result.setWorked(true);
                result.setMessage(respData);
            } else {
                System.out.print("serverPost ERROR: " + http.getResponseMessage());
                result.setMessage(http.getResponseMessage().toString());
                result.setWorked(false);
            }
            return result;
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String serverGet(String authToken, String apiName) {
        String result = null;

        try {
            HttpURLConnection http = openHttpURLConnection(apiName);
            http.setRequestMethod("GET");

            // This is a get request. There is no response body
            http.setDoOutput(false);

            http.addRequestProperty("Authorization", authToken);

            // Connect to the server
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream reqBody = http.getInputStream();
                result = readString(reqBody);
            } else {
                System.out.println("serverGet ERROR: " + http.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
    //Needs work
    private HttpURLConnection openHttpURLConnection(String apiName) throws IOException {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/" + apiName);
            return (HttpURLConnection)url.openConnection();
        } catch(Exception e) {
            throw e;
        }
    }

    private static String readString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        char[] buf = new char[1024];
        int length;
        while ((length = inputStreamReader.read(buf)) > 0) {
            stringBuilder.append(buf, 0, length);
        }

        return stringBuilder.toString();
    }
    //This gets it's own class because it needs a custom return.
    public ResultPerson getPersonURL(URL url, String auth){
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.addRequestProperty("Authorization", auth);
            connection.addRequestProperty("Accept", "application/json");
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                Reader reader = new InputStreamReader(connection.getInputStream());
                BufferedReader in = new BufferedReader(reader);
                String line = null;
                StringBuilder rslt = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    rslt.append(line);
                }
                ResultPerson out = new ResultPerson(rslt.toString());
                out.setWorked(true);
                return out;

            } else {
                ResultPerson out = new ResultPerson(connection.getResponseMessage());
                out.setWorked(false);
                return out;
            }

        } catch (Exception e){
            Log.e("Httpclient", e.getMessage(), e);
        }
        return null;
    }

    public ResultPersonAll getAllPeopleUrl(URL url, String auth){
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.addRequestProperty("Authorization", auth);
            connection.addRequestProperty("Accept", "application/json");
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                Reader reader = new InputStreamReader(connection.getInputStream());
                BufferedReader in = new BufferedReader(reader);
                String line = null;
                StringBuilder rslt = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    rslt.append(line);
                }
                Gson gson = new Gson();
                ResultPersonAll out = gson.fromJson(rslt.toString(), ResultPersonAll.class);
                out.setMessage(rslt.toString());
                out.setWorked(true);

                return out;

            } else {
                ResultPersonAll out = new ResultPersonAll();
                out.setWorked(false);
                out.setMessage(connection.getResponseMessage());
                return out;
            }

        } catch (Exception e){
            Log.e("Httpclient", e.getMessage(), e);
        }
        return null;
    }
    public ResultEventAll getAllEventsUrl(URL url, String auth){
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.addRequestProperty("Authorization", auth);
            connection.addRequestProperty("Accept", "application/json");
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                Reader reader = new InputStreamReader(connection.getInputStream());
                Gson gson = new Gson();
                ResultEventAll out = gson.fromJson(reader, ResultEventAll.class);
                out.setMessage(connection.getResponseMessage());
                out.setWorked(true);
                return out;

            } else {
                ResultEventAll out = new ResultEventAll();
                out.setWorked(false);
                out.setMessage(connection.getResponseMessage());
                return out;
            }

        } catch (Exception e){
            Log.e("Httpclient", e.getMessage(), e);
            ResultEventAll badResponse = new ResultEventAll();
            badResponse.setMessage("Bad Request");
            badResponse.setWorked(false);
            return badResponse;
        }
    }
    private static void writeString(String str, OutputStream ostream) throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(ostream);
        outputStreamWriter.write(str);
        outputStreamWriter.flush();
    }
}
