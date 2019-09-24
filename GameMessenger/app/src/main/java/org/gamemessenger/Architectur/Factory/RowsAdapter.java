package org.gamemessenger.Architectur.Factory;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.ViewGroup;

public abstract class RowsAdapter extends RecyclerView.Adapter<ViewAdapters> {

    protected SparseBooleanArray mSelectedItemsIds;

    public RowsAdapter(){
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public abstract ViewAdapters onCreateViewHolder(@NonNull ViewGroup viewGroup, int i);

    @Override
    public  void onBindViewHolder(@NonNull ViewAdapters viewAdapters, int i)
    {
        viewAdapters.itemView.setBackgroundColor(mSelectedItemsIds.get(i) ? 0x9934B5E4
                : Color.TRANSPARENT);
    }

    @Override
    public abstract int getItemCount();

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }


    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }


    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }


    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }


}
