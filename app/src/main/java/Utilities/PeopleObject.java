package Utilities;

import model.Event;
import model.PersonsModel;

public class PeopleObject {
    private PersonsModel[] data;
    private Boolean success;

    public PersonsModel[] getData() {
        return data;
    }

    public void setData(PersonsModel[] data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public PeopleObject(PersonsModel[] data, Boolean success) {
        this.data = data;
        this.success = success;


    }
}
