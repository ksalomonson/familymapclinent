package tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import Requests.RegisterRequest;
import Responses.ResultPersonAll;
import Responses.ResultRegister;
import Utilities.DataCache;
import Utilities.EventsObject;
import Utilities.PeopleObject;
import Utilities.RegisterObject;
import Utilities.ServerProxy;
import model.AuthTokenModel;
import model.PersonsModel;

public class GetPeopleTask extends AsyncTask<String,Void, ResultPersonAll> {
    private Fragment fragment;
    private RegisterObject taskRequest;
    private ResultRegister resultFromRegister;
    private Context cameFrom;
    private String toastStringIfWorked;
    private ResultPersonAll personGetAllResponse = new ResultPersonAll();

    public GetPeopleTask(Fragment in, RegisterObject request, ResultRegister resultRegister, Context context, String toasty){
        this.fragment = in;
        taskRequest = request;
        resultFromRegister = resultRegister;
        cameFrom = context;
        toastStringIfWorked = toasty;
    }

    public ResultPersonAll doInBackground(String... urlAuth){
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();

        try {
            ServerProxy serverProxy = new ServerProxy(taskRequest.getHostName(), taskRequest.getPortNumber());
            URL url = new URL(urlAuth[0]);

            personGetAllResponse = serverProxy.getAllPeopleUrl(url, urlAuth[1]);

            return personGetAllResponse;

        } catch (MalformedURLException e){
            personGetAllResponse.setMessage("Bad URl");
            personGetAllResponse.setWorked(false);
            return personGetAllResponse;
        }
    }
    @Override
    protected void onPostExecute(ResultPersonAll response) {
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();

        if (response.getWorked()){ //was a successful GET
            DataCache dataCache = DataCache.getInstance();
            Map<String, PersonsModel[]> map = new HashMap<>();
            Gson gson = new Gson();
            PeopleObject personsModel = gson.fromJson(response.getMessage(), PeopleObject.class);
            dataCache.getAuthTokenModel().setUsername(resultFromRegister.getUsername());
            map.put(resultFromRegister.getUsername(), personGetAllResponse.getData());
            dataCache.setPeople(map);
            dataCache.setAuthTokenString(resultFromRegister.getAuthtoken());
            AuthTokenModel authTokenModel = new AuthTokenModel(resultFromRegister.getAuthtoken(), resultFromRegister.getUsername());
            dataCache.setAuthTokenModel(authTokenModel);
            //Get all of the events and store
            GetEventsTask eventTask = new GetEventsTask(fragment, taskRequest, cameFrom, toastStringIfWorked);
            String url = new String("http://" + taskRequest.getHostName() + ":" + taskRequest.getPortNumber() + "/event/");
            eventTask.execute(url, resultFromRegister.getAuthtoken().toString());


        } else {
            Toast.makeText(fragment.getContext(),
                    "Something went wrong during people registration",
                    Toast.LENGTH_SHORT).show();
        }

    }
}
