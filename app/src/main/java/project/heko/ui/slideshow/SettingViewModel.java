package project.heko.ui.slideshow;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import project.heko.models.FontSetting;

public class SettingViewModel extends ViewModel {
    private final MutableLiveData<FontSetting> font;

    public SettingViewModel() {
        this.font = new MutableLiveData<>(new FontSetting());
    }

    public MutableLiveData<FontSetting> getFont() {
        return font;
    }
}
