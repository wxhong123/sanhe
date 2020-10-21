package com.qhd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.acc.common.util.StringUtil;
import com.acc.sanheapp.R;

import java.util.ArrayList;
import java.util.Map;


/**
 * Created by androider on 2018/8/19.
 * 内容：
 */
public class CheckRecordAdapter extends BaseAdapter {

    private ArrayList<Map<String, String>> list;
    private Context context;

    public CheckRecordAdapter(ArrayList<Map<String, String>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setList(ArrayList<Map<String, String>> list) {
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Viewholder viewholder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item, null);
            viewholder = new Viewholder();
            viewholder.tvXh = convertView.findViewById(R.id.tv_xh);
            viewholder.tvTitle = convertView.findViewById(R.id.tv_title);
            viewholder.tvContent = convertView.findViewById(R.id.tv_content);
            viewholder.tvCheckTime = convertView.findViewById(R.id.tv_checktime);
            viewholder.iv = convertView.findViewById(R.id.iv);
            viewholder.highlight = convertView.findViewById(R.id.tv_highlight);
            convertView.setTag(viewholder);
        } else {
            viewholder = (Viewholder) convertView.getTag();
        }
        Map<String, String> map = list.get(position);
        viewholder.tvXh.setText(nullToString(map.get("xh")));
        viewholder.tvTitle.setText(nullToString(map.get("ItemTitle")));
        viewholder.tvContent.setText(nullToString(map.get("ItemText")));
        viewholder.tvCheckTime.setText(nullToString(map.get("checktime")));
        viewholder.highlight.setVisibility(View.INVISIBLE);
        try {
            int i = Integer.parseInt(map.get("zbxxnumber"));
            if (i > 0) {
                viewholder.highlight.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }
        String zp = map.get("zp");
        if (StringUtil.isNotNull(zp)) {
            try {
                byte[] ZpData = Base64.decode(zp, Base64.DEFAULT);
                if (ZpData != null) {
                    Bitmap bm = BitmapFactory.decodeByteArray(ZpData, 0, ZpData.length);
                    viewholder.iv.setImageBitmap(bm);
                } else {
                    String type = map.get("type");
                    if (StringUtil.isNotNull(type) && type.equals("car")) {
                        viewholder.iv.setImageResource(R.mipmap.ic_jingche);
                    } else {
                        viewholder.iv.setImageResource(R.drawable.tmp);
                    }
                }
            } catch (Exception e) {

            }
        } else {
            String type = map.get("type");
            if (StringUtil.isNotNull(type) && type.equals("car")) {
                viewholder.iv.setImageResource(R.mipmap.ic_jingche);
            } else {
                viewholder.iv.setImageResource(R.drawable.tmp);
            }
        }
        return convertView;
    }

    class Viewholder {
        TextView tvXh, tvTitle, tvContent, tvCheckTime, highlight;
        ImageView iv;
    }

    private String nullToString(String str) {
        if (str == null || "".equals(str) || "null".equals(str)) {
            return "";
        }
        return str;
    }


}
