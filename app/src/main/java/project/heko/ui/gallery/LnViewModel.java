package project.heko.ui.gallery;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import project.heko.models.Book;
import project.heko.models.Chapter;
import project.heko.models.Volume;


public class LnViewModel extends ViewModel {

    private MutableLiveData<Book> mBook = new MutableLiveData<>();
    private MutableLiveData<Volume> mVolume = new MutableLiveData<>();
    private MutableLiveData<Chapter> mChapter = new MutableLiveData<>();

    public LnViewModel() {
        mVolume.setValue(new Volume());
        mChapter.setValue(new Chapter());
        mBook.setValue(new Book());
    }
    public MutableLiveData<Book> getBook(){
        if (mBook == null){
            mBook = new MutableLiveData<>();
            mBook.setValue(new Book());
        }
        return mBook;
    }

    public MutableLiveData<Volume> getVolume(){
        if (mVolume == null){
            mVolume = new MutableLiveData<>();
            mVolume.setValue(new Volume());
        }
        return mVolume;
    }
    public MutableLiveData<Chapter> getChapter(){
        if (mChapter == null){
            mChapter = new MutableLiveData<>();
            mChapter.setValue(new Chapter());
        }
        return mChapter;
    }
}