package com.example.exercise4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import java.util.List;

public class NotesAdapter extends BaseAdapter {
    private final Context context;
    private final List<NotesModel> noteList;
    private final int layout;

    // Constructor signature matches Kotlin call: NotesAdapter(this, arrayList, R.layout.row_notes)
    public NotesAdapter(Context context, List<NotesModel> noteList, int layout) {
        this.context = context;
        this.noteList = noteList;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return noteList == null ? 0 : noteList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteList == null ? null : noteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        TextView textViewNote;
        ImageView imageViewEdit;
        ImageView imageViewDelete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layout, parent, false);

            // IDs must match row_notes.xml
            viewHolder.textViewNote = convertView.findViewById(R.id.textView);
            viewHolder.imageViewEdit = convertView.findViewById(R.id.imageViewEdit);
            viewHolder.imageViewDelete = convertView.findViewById(R.id.imageViewDelete);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final NotesModel noteModel = noteList.get(position);
        viewHolder.textViewNote.setText(noteModel.getNameNote());

        viewHolder.imageViewEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof MainActivity) {
                    ((MainActivity) context).showDialogUpdate(noteModel.getNameNote(), noteModel.getIdNote());
                }
            }
        });

        viewHolder.imageViewDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof MainActivity) {
                    ((MainActivity) context).showDialogDelete(noteModel.getNameNote(), noteModel.getIdNote());
                }
            }
        });

        return convertView;
    }
}