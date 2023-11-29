package project.heko.ui.search;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.Query;

public class SearchQueryModel extends ViewModel {
    private final MutableLiveData<Query> query;

    public SearchQueryModel() {
        query = new MutableLiveData<>();
    }

    public MutableLiveData<Query> getQuery() {
        return query;
    }
}