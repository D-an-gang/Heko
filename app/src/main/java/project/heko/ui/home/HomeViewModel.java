package project.heko.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    public static class Banner {
        String announcement;
        String imgUrl;

        public Banner() {
            this.imgUrl = "";
            this.announcement = "";
        }

        public Banner(String announcement, String imgUrl) {
            this.announcement = announcement;
            this.imgUrl = imgUrl;
        }

        public String getAnnouncement() {
            return announcement;
        }

        public void setAnnouncement(String announcement) {
            this.announcement = announcement.replace("\\n", "\n");
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }

    private final MutableLiveData<Banner> banner;
    private final MutableLiveData<Boolean> loading;

    public HomeViewModel() {
        banner = new MutableLiveData<>();
        loading = new MutableLiveData<>();
    }

    public MutableLiveData<Banner> getBanner() {
        return banner;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }
}