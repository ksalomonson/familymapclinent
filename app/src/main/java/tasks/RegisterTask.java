package tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.familymaplogin.R;
import com.google.gson.Gson;

import java.net.*;

import Requests.*;
import Responses.*;
import Utilities.DataCache;
import Utilities.LoginObject;
import Utilities.LoginResultObject;
import Utilities.RegisterObject;
import Utilities.ServerProxy;

public class RegisterTask extends AsyncTask<RegisterObject, Void, Result> {
    private Fragment activityFragment;
    private RegisterObject registerRequest;
    private Context mainActivity;

    private Result resultRegister;
    private ResultPersonAll resultPersonAll;
    private ResultEventAll resultEventAll;

    public RegisterTask(Fragment fragAct, Context in){
        this.activityFragment = fragAct;
        this.mainActivity = in;
        resultRegister = new ResultRegister(null);
        resultPersonAll = new ResultPersonAll();
        resultEventAll = new ResultEventAll();
    }

    public Result doInBackground(RegisterObject... myRegisterRequests){
        registerRequest = myRegisterRequests[0];
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();
        RegisterObject register = myRegisterRequests[0];
        ServerProxy serverProxy = new ServerProxy(register.getHostName(), register.getPortNumber());
        resultRegister = serverProxy.registerUser(register.getUsername(), register.getPassword(), register.getEmail(), register.getFirstName(), register.getLastName(), register.getGender());
        return resultRegister;

    }
    @Override
    protected void onPostExecute(Result response) {
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();

        if (response.getWorked()){
            Gson gson = new Gson();
            LoginResultObject loginResultObject = gson.fromJson(response.getMessage(), LoginResultObject.class);
            ResultRegister formattedResult = new ResultRegister(loginResultObject.getAuthtoken(), loginResultObject.getUsername(), loginResultObject.getPersonID());
            String url = new String ("http://" + registerRequest.getHostName() + ":" + registerRequest.getPortNumber() + "/person/");
            String stringForToastIfSuccessful = new String("Register Success!" + "\n" + registerRequest.getFirstName() + "\n" + registerRequest.getLastName());
            GetPeopleTask peopleTask = new GetPeopleTask(activityFragment, registerRequest, formattedResult, mainActivity, stringForToastIfSuccessful);
            peopleTask.execute(url, loginResultObject.getAuthtoken());
            DataCache dataCache = DataCache.getInstance();
            dataCache.setUserPersonID(formattedResult.getPersonID());

        //failed registerRequest
        } else {
            Toast.makeText(activityFragment.getContext(),"Register Not Successful", Toast.LENGTH_SHORT).show();
        }

    }
}
