package project.heko.ui.chapter;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import project.heko.models.FontSetting;

public class SettingViewModel extends ViewModel {
    private final MutableLiveData<FontSetting> font;
    private final MutableLiveData<Boolean> reset_lastread;

    public SettingViewModel() {
        this.reset_lastread = new MutableLiveData<>(true);
        this.font = new MutableLiveData<>(new FontSetting());
    }

    public MutableLiveData<FontSetting> getFont() {
        return font;
    }

    public MutableLiveData<Boolean> getReset() {
        return this.reset_lastread;
    }
}
