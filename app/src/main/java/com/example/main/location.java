package com.example.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.adapter.CharacterParser;
import com.example.adapter.PinyinComparator;
import com.example.adapter.SortAdapter;
import com.example.adapter.SortModel;
import com.example.mytools.SideBar;
import com.example.mytools.my_Edit;
import com.example.mytools.staticTools;
import com.example.wangqiang.jianmi1.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wangqiang on 2016/6/15.
 * 高德地图key   8a25a13a8935c5452fb97ad8e92e4973
 */
public class location extends Activity implements AMapLocationListener
{
    private staticTools tools;
    private Button back;
    private TextView dialog,t1,t2;
    private ListView sortListView;
    private SideBar sideBar;
    private SortAdapter adapter;
    private my_Edit mClearEditText;
    private String city = null;
    private  View Header;
    private Boolean getcity = false;//判断是否获取当前定位信息
    /**
     * 定位
     */
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private String[] country = new String[]{"北京","天津","上海","南京","苏州","长沙","湖南","武汉","重庆","拉萨","乌鲁木齐"
            ,"银川","南宁","柳州","桂林","北海","崇左","百色","防城港","镇江","无锡","扬州","徐州","常州"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_location);
        Header = getLayoutInflater().inflate(R.layout.location_listitem,null);
        findview();
        setlistener();
        getLocation();
        initData();
    }
    private void findview(){
        back = (Button) findViewById(R.id.location_back);
        dialog = (TextView)findViewById(R.id.country_dialog);//显示对话的text
        sortListView = (ListView)findViewById(R.id.country_list);
        sideBar = (SideBar)findViewById(R.id.list_sidrbar);
        mClearEditText = (my_Edit)findViewById(R.id.location_editText);
        //头text
         t1 = (TextView)Header.findViewById(R.id.location_listItem_text1);
         t2 = (TextView)Header.findViewById(R.id.location_listItem_text2);
        t1.setText("GPS定位城市");
        t2.setText("暂无定位信息");
        //实例化自定义的工具类
        tools  = new staticTools();

        t2.setClickable(false);
        Header.setClickable(false);
        sortListView.addHeaderView(Header);
    }
    /**
     * 获取位置
     */
    private void getLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);

//        if (mLocationOption.isOnceLocationLatest()) {
//            mLocationOption.setOnceLocationLatest(true);
//            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
//            //如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。
//        }
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(false);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
    //可以通过类implement方式实现AMapLocationListener接口，也可以通过创造接口类对象的方法实现
    //以下为后者的举例：
    /**
     * 获取位置的回调方法
     */
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
               city =  amapLocation.getCity();//城市信息
                //显示定位信息
                t2.setText(city);
                getcity = true;//成功获取定位
                tools.setCity(city);//在自定义类中更新城市信息
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
        }
    };

    /**
     * 停止定位
     */
    private void removeLocation(){
            mLocationClient.stopLocation();
            mLocationClient = null;
            mLocationOption = null;
    }
    private void setlistener(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeLocation();
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
    });
        /**
         * 字母的监听
         */
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if(position != -1){
                    //加了头View才加下面一段代码
                    position++;
                    sortListView.setSelection(position);
                }

            }
        });
/**
 * 城市List的监听，返回所选择的城市到主界面
 */
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
                String locat = null;
                if(position>=1){
//                    Toast.makeText(getApplication(), ((SortModel)adapter.getItem(position -1)).getName(), Toast.LENGTH_SHORT).show();
                     locat = ((SortModel)adapter.getItem(position -1)).getName();
                }
                //如果获取到位置信息，则返回当前位置
                else if(position ==0&&getcity)
                {
                    locat = city;
                }
                if(null!=locat){
                    tools.setCity(locat);
                    //发送广播，通知更新位置信息
                    Intent intentG = new Intent("City_changed");
                    location.this.sendBroadcast(intentG);
                    //返回结果给main_fragment
                    Intent intent = new Intent();
                    intent.putExtra("city",locat);
                    setResult(2,intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
            }
        });
        mClearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    private void initData(){
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        //实例化拼音排序类
        pinyinComparator = new PinyinComparator();
        sideBar.setTextView(dialog);
        //获取城市的list集合
        SourceDateList = filledData(country);
        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);
    }
    private List<SortModel> filledData(String [] date){
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for(int i=0; i<date.length; i++){
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(sortString.toUpperCase());
            }else{
                sortModel.setSortLetters("#");
            }
            mSortList.add(sortModel);
        }
        return mSortList;
    }
    /**
     * 根据输入框中的值来过滤数据并更新ListView
     * @param filterStr
     */
    private void filterData(String filterStr){
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if(TextUtils.isEmpty(filterStr)){
            filterDateList = SourceDateList;
        }else{
            filterDateList.clear();
            for(SortModel sortModel : SourceDateList){
                String name = sortModel.getName();
                if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
                    filterDateList.add(sortModel);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }
    public void onBackPressed(){
        super.onBackPressed();
        removeLocation();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

    }
}
