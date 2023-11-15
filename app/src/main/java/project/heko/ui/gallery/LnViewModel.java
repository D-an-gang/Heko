package project.heko.ui.gallery;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import project.heko.models.Book;


public class LnViewModel extends ViewModel {

    private MutableLiveData<Book> mBook = new MutableLiveData<>();

    public LnViewModel() {
        mBook.setValue(new Book());
    }
    public MutableLiveData<Book> getBook(){
        if (mBook == null){
            mBook = new MutableLiveData<>();
            mBook.setValue(new Book());
        }
        return mBook;
    }
}