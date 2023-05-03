package tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Debug;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.google.gson.Gson;
import com.example.familymaplogin.R;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import Requests.LoginRequest;
import Responses.Result;
import Responses.ResultLogin;
import Responses.ResultPerson;
import Utilities.DataCache;
import Utilities.LoginObject;
import Utilities.LoginResultObject;
import Utilities.ServerProxy;
import model.AuthTokenModel;

public class LoginTask extends AsyncTask<LoginObject, Void, Result> {
    private Result resultLogin = new Result(null);
    private LoginObject loginRequest;
    private Fragment myFrag;
    private Context mainActivity;

    public LoginTask(Fragment in, Context inActivity){
        this.myFrag = in;
        mainActivity = inActivity;
    }



    public Result doInBackground(LoginObject... loginObjects) {
        loginRequest = loginObjects[0];
        ServerProxy myProxy = new ServerProxy(loginRequest.getHostName(), loginRequest.getPortNumber());
        resultLogin = myProxy.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
        return resultLogin;
    }
    @Override
    protected void onPostExecute(Result response) {
        if(Debug.isDebuggerConnected())
            Debug.waitForDebugger();

        if (resultLogin.getWorked()){
            Gson g = new Gson();
            LoginResultObject formattedResult = g.fromJson(resultLogin.getMessage(), LoginResultObject.class);
            String url = new String("http://" + loginRequest.getHostName() + ":" + loginRequest.getPortNumber() + "/person/" + formattedResult.getPersonID());
            AuthTokenModel authTokenModel = new AuthTokenModel(formattedResult.getAuthtoken(), formattedResult.getUsername());
            authTokenModel.setUsername(loginRequest.getUsername());
            ResultLogin resultLogin1 = new ResultLogin(authTokenModel, formattedResult.getUsername(), formattedResult.getPersonID());
            GetPersonTask personTask = new GetPersonTask(myFrag, mainActivity, loginRequest, resultLogin1);
            personTask.execute(url, formattedResult.getAuthtoken());
            DataCache dataCache = DataCache.getInstance();
            dataCache.setUserPersonID(formattedResult.getPersonID());

        } else {
            Toast.makeText(myFrag.getContext(),"Login Not Successful", Toast.LENGTH_SHORT).show();
        }
    }
}
