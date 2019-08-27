package com.elblasy.navigation.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.elblasy.navigation.R;

import java.util.ArrayList;

public class EditAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<EditText> listforview;
    private ArrayList<Boolean> check;
    private LayoutInflater inflator;
    private View v;
    private ViewHolder holder;

    //Constructor
    public EditAdapter(Context context) {
        super();
        this.context = context;
        listforview = new ArrayList<>();
        listforview.add(new EditText(context));
        inflator = LayoutInflater.from(context);
        check = new ArrayList<>();
        check.add(true);
    }

    @Override
    public int getCount() {
        return listforview.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        v = convertView;

        if (convertView == null) {
            //inflate the view for each row of listview
            v = inflator.inflate(R.layout.edit_list, parent, false);
            //ViewHolder object to contain myadapter.xml elements
            holder = new ViewHolder();

            holder.number = v.findViewById(R.id.editText2);
            holder.details = v.findViewById(R.id.editText1);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }


        holder.details.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (listforview.size() < 7) {
                    if (count == 1 && check.get(position)) {
                        EditText editText = new EditText(context);
                        editText.setTag(position);
                        listforview.add(editText);
                        check.set(position, false);
                        check.add(true);
                        notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                holder.number.setFocusable(false);
                holder.details.setFocusable(true);
            }
        });

        return v;
    }

    private class ViewHolder {
        EditText number;
        EditText details;

    }
}




