package project.heko.ui.gallery;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import project.heko.models.Book;


public class LnViewModel extends ViewModel {

    private MutableLiveData<Book> mBook = new MutableLiveData<>();
    private MutableLiveData<Boolean> mId = new MutableLiveData<>();

    public LnViewModel() {
        mBook.setValue(new Book());
        mId.setValue(false);
    }
    public MutableLiveData<Book> getBook(){
        if (mBook == null){
            mBook = new MutableLiveData<>();
            mBook.setValue(new Book());
        }
        return mBook;
    }
    public MutableLiveData<Boolean> getId(){
        if (mId == null){
            mId = new MutableLiveData<>();
            mId.setValue(false);
        }
        return mId;
    }

}