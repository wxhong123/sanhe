package com.acc.common.listview;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.acc.common.myokhttp.builder.PostBuilder;
import com.acc.sanheapp.R;
import com.arong.swing.db.entity.KeyCar;
import com.arong.swing.db.entity.KeyPerson;
import com.data.BaseDicValueDao;


@SuppressWarnings("unused")
public class JsListViewAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private List<KeyPerson> listItems; //
    private LayoutInflater listContainer; // 视图容器
    private List<KeyCar> zbclList;
    private int type;
    BaseDicValueDao baseDicValueDao;

    public final class ListItemView { // 自定义控件集合
        public TextView js_id;
        public TextView js_lx;
        public TextView tv_from;
    }

    public JsListViewAdapter(Context context, List<KeyPerson> listItems) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = listItems;
        baseDicValueDao = new BaseDicValueDao(context);
    }

    public JsListViewAdapter(Context context, List<KeyCar> zbclList, int type) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.zbclList = zbclList;
        this.type = type;
        baseDicValueDao = new BaseDicValueDao(context);
    }

    public void clearData() {
        if (listItems != null) {
            listItems.clear();
        }
        if (zbclList != null) {
            zbclList.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (type == 1) {
            return zbclList.size();
        }
        return listItems.size();
    }

    @Override
    public Object getItem(int arg0) {
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    /**
     * ListView Item设置
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Log.e("method", "getView");
        // 自定义视图
        ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.js_listview, null);
            // 获取控件对象
            listItemView.js_id = (TextView) convertView.findViewById(R.id.js_id);
            listItemView.js_lx = (TextView) convertView.findViewById(R.id.js_lx);
            listItemView.tv_from = (TextView) convertView.findViewById(R.id.tv_from);
            // 设置控件集到convertView
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        if (type == 1) {
            listItemView.js_id.setText(zbclList.get(position).getRwid());
            listItemView.js_lx.setText(zbclList.get(position).getCllb());
            String pverid = zbclList.get(position).getPverid();
            String datasource = baseDicValueDao.queryLableBydictTypeAndValue("js_datasource", pverid, "");
            listItemView.tv_from.setText(datasource);
        } else {
            listItemView.js_id.setText(listItems.get(position).getRwid());
            listItemView.js_lx.setText(listItems.get(position).getRylb());
            String pverid = listItems.get(position).getPverid();
            String datasource = baseDicValueDao.queryLableBydictTypeAndValue("js_datasource", pverid, "");
            listItemView.tv_from.setText(datasource);

        }
        return convertView;
    }
}
