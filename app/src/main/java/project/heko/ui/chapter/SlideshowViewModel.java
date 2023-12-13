package project.heko.ui.chapter;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SlideshowViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SlideshowViewModel() {
        mText = new MutableLiveData<>();
    }

    public MutableLiveData<String> getText() {
        return mText;
    }
}