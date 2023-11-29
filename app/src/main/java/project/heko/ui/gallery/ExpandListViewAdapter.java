package project.heko.ui.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.Inflater;

import project.heko.R;
import project.heko.databinding.LayoutRowChapterBinding;
import project.heko.databinding.LayoutRowVolumeBinding;
import project.heko.databinding.LightnovelDetailBinding;
import project.heko.dto.LightNovelDto;
import project.heko.dto.LnDto;
import project.heko.models.Chapter;
import project.heko.models.Volume;

public class ExpandListViewAdapter extends BaseExpandableListAdapter {
    private ArrayList<Volume> mListGroup;
    private Map<Volume,ArrayList<Chapter>> mListItem;

    public ExpandListViewAdapter(ArrayList<Volume> list,Map<Volume,ArrayList<Chapter>> item) {
        this.mListGroup = list;
        this.mListItem = item;
    }

    @Override
    public int getGroupCount() {
        return mListGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return Objects.requireNonNull(mListItem.get(mListGroup.get(groupPosition))).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mListGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return Objects.requireNonNull(mListItem.get(mListGroup.get(groupPosition))).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row_volume, parent,false);
        }
        TextView volume_title = convertView.findViewById(R.id.volume_title);
        Volume volume = mListGroup.get(groupPosition);
        volume_title.setText(volume.getTitle());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row_chapter, parent,false);
        }
        TextView chapter_title = convertView.findViewById(R.id.chapter_title);
        Chapter chapter = Objects.requireNonNull(mListItem.get(mListGroup.get(groupPosition))).get(childPosition);
        chapter_title.setText(chapter.getTitle());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
