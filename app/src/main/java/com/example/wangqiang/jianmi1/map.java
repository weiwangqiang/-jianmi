package com.example.wangqiang.jianmi1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.overlay.BusRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangqiang on 2016/6/30.
 */
public class map extends Activity  implements LocationSource , AMapLocationListener {
    private MapView mMapView = null;
    private AMap aMap;
    //起点终点的经纬度
    private LatLonPoint start,ends;
    private AMapLocation aMapLocation ;
    /**
     * 定位
     */
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private Boolean isFirstLoc = true;
    private RelativeLayout back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
//        MapsInitializer.sdcardDir = getSdCacheDir(map.this);
        findid();
//        AMapNavi  mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);
        init();
        setlister();
    }
    private void findid(){
        mMapView = (MapView) findViewById(R.id.map_view);
        back = (RelativeLayout) findViewById(R.id.mapView_back);
    }
    private void init(){
        //由地址获取经纬度
        GeocodeQuery query = new GeocodeQuery("江苏科技大学西校区", "镇江");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        GeocodeSearch geocodeSearch = new GeocodeSearch(map.this);
        geocodeSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
        geocodeSearch.setOnGeocodeSearchListener(new geocodeSearchLister());
//        经度32.205651纬度119.513006

        if (null!=mMapView) {
            aMap = mMapView.getMap();
            //设置显示定位按钮 并且可以点击
            UiSettings settings = aMap.getUiSettings();
            aMap.setLocationSource(this);//设置了定位的监听,这里要实现LocationSource接口
//            settings.setMyLocationButtonEnabled(true);// 是否显示定位按钮
            aMap.setMyLocationEnabled(true);//显示定位层并且可以触发定位,默认是flase
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.location_icon);
//         //定位的style
            MyLocationStyle myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationIcon(bitmapDescriptor);//设置位置图标
            aMap.setMyLocationStyle(myLocationStyle);

            settings.setZoomGesturesEnabled(true);//是否可以手势控制缩放
            View child = mMapView.getChildAt(1);
            if (child != null && (child instanceof ImageView || child instanceof ZoomControls)){
                child.setVisibility(View.INVISIBLE);
            }
        }
        mLocationClient = new AMapLocationClient(getApplicationContext());

        // 设置定位回调监听，这里要实现AMapLocationListener接口，
        // AMapLocationListener接口只有onLocationChanged方法可以实现，
        // 用于接收异步返回的定位结果，参数是AMapLocation类型。
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
    private void setlister(){
            back.setOnClickListener(new lister());
    }
//LocationSource的方法
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }
    //LocationSource的方法
    @Override
    public void deactivate() {

    }
    //   AMapLocationListener的监听方法，在此方法中获取位置信息
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                //获取当前位置
                start = new LatLonPoint(aMapLocation.getLatitude(),aMapLocation.getLongitude());
                //        将一张图片绘制为Marker
                LatLng latLng = new LatLng(start.getLatitude(),start.getLongitude());

                Log.e(" my jiangda",""+"经度"+start.getLatitude()+"纬度"+start.getLongitude());

                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.my_location);
    //                MarkerOptions option = new MarkerOptions();
    //                option.position(latLng);//设置经纬度
    //                option.title("韦王强");
    //                option.icon(bitmapDescriptor);
    //                option.period(0);
    //                aMap.addMarker(option);
                View view = getLayoutInflater().inflate(R.layout.map_markerbg, null);
                TextView tv = (TextView) view.findViewById(R.id.map_markerText);
                tv.setText("1");
                tv.setPadding(0, 0, 0, 25);
                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(view);

//                Marker marker = aMap.addMarker(new MarkerOptions()
//                        .position(latLng)
//                        .title("I am here")
//                        .icon(bitmap)
//                        .draggable(true));
//                marker.showInfoWindow();// 设置默认显示一个infowinfow
//                //获取屏幕的长宽
//                WindowManager wmanager = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
//                int width = wmanager.getDefaultDisplay().getWidth();
//                int height = wmanager.getDefaultDisplay().getHeight();
//                Circle circle = new Circle(ICircleDelegate);
//                GroundOverlay(IGroundOverlayDelegate iGroundOverlayDelegate)//图片层，可以添加BitmapDescriptor
                /**(纬度，经度)
                 * public LatLonPoint(double latitude,double longitude)
                 */
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(),
                            aMapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    isFirstLoc = false;
                }
                RouteSearch   routeSearch = new RouteSearch(this);
                routeSearch.setRouteSearchListener(new myResultlister());
                // fromAndTo包含路径规划的起点和终点，RouteSearch.BusLeaseWalk表示公交查询模式
                /**
                 * BusRouteQuery(RouteSearch.FromAndTo ft, int mode, java.lang.String city, int nightflag)
                 * 设置搜索条件。参数 ft，路径的起终点；mode，计算路径的模式，可选，默认为最快捷；city，
                 * 城市名称/城市区号/电话区号，此项不能为空；nightflag，是否计算夜班车，默认为不计算，0：
                 * 不计算，1：计算，可选。如果选择计算夜班车（nightflag=1），
                 * 返回的夜班车数据将会排列在结果的前边。如果存在地铁换乘出行，
                 * 返回结果中还包括地铁的入站口和出站口信息。
                 */
                RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(start,ends);
                RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, RouteSearch.BusSaveMoney, "镇江",0);
                routeSearch.calculateBusRouteAsyn(query);//开始规划路径
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
//                Log.e("AmapError", "location Error, ErrCode:"
//                        + aMapLocation.getErrorCode() + ", errInfo:"
//                        + aMapLocation.getErrorInfo());
                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }
    /**
     * 逆向编码的回调方法
     */
    private class geocodeSearchLister implements GeocodeSearch.OnGeocodeSearchListener{
    //        根据给定的经纬度和最大结果数返回逆地理编码的结果列表。
        @Override
        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        }
    //        根据给定的地理名称和查询城市，返回地理编码的结果列表。1000为成功
        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
            if (1000==i){
                List<GeocodeAddress> list = new ArrayList<GeocodeAddress>();
                list = geocodeResult.getGeocodeAddressList();
                //创建目的地的纬度经度
                ends = new LatLonPoint(list.get(0).getLatLonPoint().getLatitude(),list.get(0).getLatLonPoint().getLongitude());

//                MarkerOptions option = new MarkerOptions();
//                option.position(latLng);//设置经纬度
//                option.title("韦王强");
//                option.icon(bitmap);
//                option.period(0);
//                aMap.addMarker(option);
            }
        }
    }

    /**
     * 路径规划搜索结果监听
     */
    private class myResultlister implements RouteSearch.OnRouteSearchListener {
        @Override
        public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
            //返回i是1000为成功，其他为失败
            //清除覆盖物
            if(i==1000){
                aMap.clear();
                AMapOptions options = new AMapOptions();
                BusRouteOverlay busRouteOverlay = new BusRouteOverlay(map.this,aMap,busRouteResult.getPaths().get(0),start,ends);
//            busRouteOverlay.removeFromMap();
                //将路径规划添加到map
                busRouteOverlay.addToMap();
                busRouteOverlay.setNodeIconVisibility(true);
                options.zoomControlsEnabled(true);
//                  busRouteOverlay.zoomToSpan();
            }
        }
        @Override
        public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        }
        @Override
        public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

        }
    }
    private class lister implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case  R.id.mapView_back:
                    mLocationClient.stopLocation();//停止定位
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    break;
                default:
                    break;
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=mMapView){
            mLocationClient.stopLocation();//停止定位
            mMapView.onDestroy();
            mLocationClient = null;
            mLocationOption = null;
        }
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
    }
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }

}
