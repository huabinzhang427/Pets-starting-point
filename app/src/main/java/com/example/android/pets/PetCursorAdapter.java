package com.example.android.pets;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.pets.data.PetContract;

/**
 * author: zhanghuabin
 * date: 2018/7/3 15:10
 * github: https://github.com/zhanghuabin-sh
 * email: 2908882095@qq.com
 */
public class PetCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link PetCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public PetCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flag */);
    }

    /**
     *
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     *
     * @param parent  The parent to which the new view is attached to
     *
     * @return the newly created list item view.
     * */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     *
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     * correct row.
     * */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView  = (TextView) view.findViewById(R.id.name);
        TextView summaryTextView  = (TextView) view.findViewById(R.id.summary);

        // Find the columns of pet attributes that we're intersted in
        int nameColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
        int breedColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED);

        // Read the pet attributes from the Cursor for the current pet
        String petName = cursor.getString(nameColumnIndex);
        String petBreed = cursor.getString(breedColumnIndex);

        // Update the TextView with the attributes for the current pet
        nameTextView.setText(petName);
        summaryTextView.setText(petBreed);
    }
}
