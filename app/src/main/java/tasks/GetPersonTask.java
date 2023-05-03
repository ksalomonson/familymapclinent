package tasks;

import android.app.Person;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.net.URL;

import Requests.LoginRequest;
import Requests.RegisterRequest;
import Responses.ResultLogin;
import Responses.ResultPerson;
import Responses.ResultRegister;
import Utilities.LoginObject;
import Utilities.RegisterObject;
import Utilities.ServerProxy;
import model.PersonsModel;

public class GetPersonTask extends AsyncTask<String, Void, ResultPerson> {
    ResultPerson person;
    Fragment fragment;
    Context cameFrom;
    LoginObject loginRequest;
    ResultLogin resultLogin;
    RegisterObject registerRequest;
    ResultRegister resultRegister;

    public GetPersonTask(Fragment in, Context context, LoginObject request, ResultLogin result){
        fragment = in;
        cameFrom = context;
        loginRequest = request;
        registerRequest = new RegisterObject();
        registerRequest.setHostName(loginRequest.getHostName());
        registerRequest.setPortNumber(loginRequest.getPortNumber());
        resultRegister = new ResultRegister(null);
        resultLogin = result;
    }

    public ResultPerson doInBackground(String... urlAuth){
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();

        try {
            ServerProxy serverProxy = new ServerProxy(loginRequest.getHostName(), loginRequest.getPortNumber());
            URL url = new URL(urlAuth[0]);

            person = serverProxy.getPersonURL(url, urlAuth[1]);
            Gson gson = new Gson();
            PersonsModel resultPerson = gson.fromJson(person.getMessage(), PersonsModel.class);
            person.setPerson(resultPerson);
            return person;

        } catch (MalformedURLException e){
            return null;
        }
    }
    @Override
    protected void onPostExecute(ResultPerson person){

        if (person != null){

            String stringForToastIfSuccessful = new String("Login Success!" + "\n" + person.getPerson().getFirstName() + "\n" + person.getPerson().getLastName());
            resultRegister.setAuthtoken(resultLogin.getAuthtoken().getAuthtoken());
            String url = new String ("http://" + registerRequest.getHostName() + ":" + registerRequest.getPortNumber() + "/person/");
            resultRegister.setUsername(resultLogin.getUsername());
            GetPeopleTask myPeopleTask = new GetPeopleTask(fragment, registerRequest, resultRegister, cameFrom, stringForToastIfSuccessful);
            myPeopleTask.execute(url, resultRegister.getAuthtoken().toString());


        }
        else {
            Toast.makeText(fragment.getContext(),
                    "Login Not Successful See PersonTask",
                    Toast.LENGTH_SHORT).show();
        }

    }
}
