package Utilities;

import model.Event;

//This has to exist for the sake of JSON formatting. Again.
public class EventsObject {

    EventsObject(Event[] data, Boolean success){
        this.data = data;
        this.success = success;
    }
    private Event[] data;
    private Boolean success;

    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
