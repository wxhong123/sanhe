package com.qhd;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.acc.sanheapp.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by androider on 2018/8/20.
 * 内容：
 */
public class WarnAdapter extends BaseAdapter {

    List<String> strings;
    Context context;

    public WarnAdapter(List<String> strings, Context context) {
        this.strings = strings;
        if (strings == null) {
            this.strings = new ArrayList<>();
        }
        this.context = context;
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public Object getItem(int position) {
        return strings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_warn, null);
        TextView textView = view.findViewById(R.id.tv);
        textView.setText(strings.get(position));
        return view;
    }

}
