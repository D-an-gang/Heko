package project.heko.ui.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import project.heko.models.User;

public class ProfileViewModel extends ViewModel {
    private MutableLiveData<User> mUser = new MutableLiveData<>();

    public ProfileViewModel() {
        mUser.setValue(new User());
    }
    public MutableLiveData<User> getUser(){
        if (mUser == null){
            mUser = new MutableLiveData<>();
            mUser.setValue(new User());
        }
        return mUser;
    }
}