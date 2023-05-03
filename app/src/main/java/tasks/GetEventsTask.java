package tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.familymaplogin.MainActivity;
import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import Responses.ResultEventAll;
import Utilities.DataCache;
import Utilities.EventsObject;
import Utilities.RegisterObject;
import Utilities.ServerProxy;
import model.AuthTokenModel;
import model.Event;

public class GetEventsTask extends AsyncTask<String,Void, ResultEventAll> {
    private Fragment fragment;
    private RegisterObject taskRequest;
    private Context cameFrom;
    private String toastSuccessString;
    private ResultEventAll resultEventAll = new ResultEventAll();

    public GetEventsTask(Fragment in, RegisterObject registerObject, Context context, String toasty){
        this.fragment = in;
        taskRequest = registerObject;
        cameFrom = context;
        toastSuccessString = toasty;
    }

    public ResultEventAll doInBackground(String... urlAuth){
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();

        try {
            ServerProxy myServerProxy = new ServerProxy(taskRequest.getHostName(), taskRequest.getPortNumber());
            URL url = new URL(urlAuth[0]);

            DataCache dataCache = DataCache.getInstance();
            dataCache.setAuthTokenString(urlAuth[1]);
            resultEventAll = myServerProxy.getAllEventsUrl(url, urlAuth[1]);

            return resultEventAll;

        } catch (MalformedURLException e){
            resultEventAll.setWorked(false);
            resultEventAll.setMessage("Bad URl");
            return resultEventAll;
        }
    }
    @Override
    protected void onPostExecute(ResultEventAll response) {
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();

        if (response.getWorked()){ //was a successful EventGetAll
            DataCache dataCache = DataCache.getInstance();
            Map<String, Event[]> map = new HashMap<>();
            Map<String, Event> mapIndiviualdEvents = new HashMap<>();
            map.put(dataCache.getAuthTokenModel().getUsername(), response.getData());
            for (int i = 0; i < response.getData().length; i++) {
                mapIndiviualdEvents.put(response.getData()[i].getEventID(), response.getData()[i]);
            }
            dataCache.setPersonEvents(map);
            dataCache.setEvents(mapIndiviualdEvents);



            Toast.makeText(fragment.getContext(),
                    toastSuccessString,
                    Toast.LENGTH_SHORT).show();

            //basically a return statement
            MainActivity source = (MainActivity) cameFrom;
            source.onLoginRegisterSuccess();


        } else { //was not a successful eventGetAll
            //display failed eventGetAll toast
            Toast.makeText(fragment.getContext(),
                    "Event all Failed",
                    Toast.LENGTH_SHORT).show();
        }

    }
}
