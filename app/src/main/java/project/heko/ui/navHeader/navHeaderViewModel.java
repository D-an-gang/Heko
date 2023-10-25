package project.heko.ui.navHeader;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import project.heko.models.User;

public class navHeaderViewModel extends ViewModel {
    private MutableLiveData<User> currentUser;
    public navHeaderViewModel() {
        currentUser = new MutableLiveData<>();
        currentUser.setValue(new User());
    }

    public MutableLiveData<User> getUser(){
        if (currentUser == null){
            currentUser = new MutableLiveData<>();
            currentUser.setValue(new User());
        }
        return currentUser;
    }
}
