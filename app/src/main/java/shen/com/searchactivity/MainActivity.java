package shen.com.searchactivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.fl)
    FlowLayout fl;
    @BindView(R.id.lv_view)
    ListView lvView;
    private SharedPreferences sp;
    private ArrayAdapter<String> arr_adapter;
    private String[] mHistory_arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        String[] mStrings = {"apple", "百度CEO", "阿里巴巴", "绩效股", "中国股市", "美团", "google", "淘宝", "雷军 小米公司", "大疆无人机"};
        setData(mStrings);
// 获取搜索记录文件内容
        sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        String history = sp.getString("history", "暂时没有搜索记录");
        // 用逗号分割内容返回数组
        mHistory_arr = history.split(",");
        Toast.makeText(MainActivity.this, mHistory_arr.length+"", Toast.LENGTH_SHORT).show();
        // 新建适配器，适配器数据为搜索历史文件内容
        arr_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, mHistory_arr);
        lvView.setAdapter(arr_adapter);
    }

    /**
     * 设置数据
     */
    public void setData(String[] strings) {
        LayoutInflater mInflater = LayoutInflater.from(this);
        int count = strings.length;
        for (int i = 0; i < count; i++) {
            final TextView tv = (TextView) mInflater.inflate(R.layout.flowlayout_textview, fl, false);
            tv.setText(strings[i]);
            tv.setTag(i);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, tv.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });


            fl.addView(tv);
        }
    }

    @OnClick({R.id.bt_search, R.id.iv_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_search:
                save();
                break;
            case R.id.iv_delete:
                mHistory_arr=new String[0];
                arr_adapter.notifyDataSetChanged();
                sp.edit().putString("history","暂时没有搜索记录" ).commit();
                break;
        }
    }

    private void save() {
        int n = 1;
        String text = etSearch.getText().toString().trim();
        String[] history_arr = new String[20];
        history_arr [0]=text;
        for (int i = 0; i < mHistory_arr.length; i++) {
            if (!text.equals(mHistory_arr[i])&&!"暂时没有搜索记录".equals(mHistory_arr[i])) {
                if(n<19) {
                    history_arr[n] = mHistory_arr[i];
                    n = n + 1;
                }
            }
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < history_arr.length; i++) {
             if(history_arr[i]==null) {
                 i=20;
             }else {
                 sb.append(history_arr[i] + ",");
             }
        }
        sp.edit().putString("history", sb.toString()).commit();
    }
}
