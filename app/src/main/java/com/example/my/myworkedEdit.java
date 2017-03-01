package com.example.my;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.control.UserControl;
import com.example.mytools.CircleImageView;
import com.example.mytools.ResumeBirthdayDialog;
import com.example.mytools.ResumeEditDialog;
import com.example.mytools.ResumeHeightDialog;
import com.example.mytools.ResumeIntoTimeDialog;
import com.example.mytools.ResumeRealNameDialog;
import com.example.mytools.staticTools;
import com.example.object.User;
import com.example.wangqiang.jianmi1.R;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.mytools.ResumeRealNameDialog.ResumeLister;

/**
 * 编辑简历
 * Created by wangqiang on 2016/7/7.
 */
public class myworkedEdit extends Activity {
    private String TAG = "myworkedEdit";
    private AlertDialog.Builder alertDialogBuilder;
    private    AlertDialog alertDialog;
    private OkHttpClient mOkHttpClient ;
    private String ImageUrl = staticTools.url+"/upUserPic";//修改用户头像的api
    private String JsonUrl = staticTools.url+"/updateUser2";//修改用户信息的api
    private User user;
    private String birth = "";//出生日期,入学日期
    private String subject, Introduce;
    private int UHeight = 0;//用户的身高
    private Dialog dialog;
    private Button back;
    private TextView save;
    private CircleImageView userIcn;
    private int isStudent = 0;
    //拍照和图库的Intent请求码
    public final int TAKE_PHOTO_WITH_DATE = 200;
    public final int TAKE_PHOTO_FROM_IMAGE = 201;
    private final int TAKEA_PICTURE_COPY = 202;//剪切图片
    private Uri uri ;
    String filePath = Environment.getExternalStorageDirectory()+"/jianmi";
    private String savefile ;
    private TextView userBirth, userGender, userStudent,
            userHeight, userintoTime, userName, userSchool,
            userMajor, userIntroduce, userCity;
    private RelativeLayout changeIcn, gender, student, birthday,
            height, into, RealName, school, major,
            introduce, city;
    private Handler handler = new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case 0x1:
                    alertDialog.cancel();
                    finish();
                    break;
                case 0x2:
                    alertDialog.cancel();
                    Toast.makeText(myworkedEdit.this,"提交失败，请重试！",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_my_myworked_edit);
        mOkHttpClient = new OkHttpClient();
        user =  UserControl.getUserControl().getUser();
        savefile = Environment.getExternalStorageDirectory() +
                "/jianmi/"+user.getUsername()+".png";
        findId();
        initView();
        setlister();
    }

    private void findId() {
        userIcn = (CircleImageView) findViewById(R.id.main_my_resume_UserIcn);
        back = (Button) findViewById(R.id.myWorked_Edit_back);
        changeIcn = (RelativeLayout) findViewById(R.id.main_my_resume_changeIcn);
        gender = (RelativeLayout) findViewById(R.id.main_my_resume_gender);
        student = (RelativeLayout) findViewById(R.id.main_my_resume_student);
        birthday = (RelativeLayout) findViewById(R.id.main_my_resume_birthday);
        height = (RelativeLayout) findViewById(R.id.main_my_resume_height);
        into = (RelativeLayout) findViewById(R.id.main_my_resume_intotime);
        RealName = (RelativeLayout) findViewById(R.id.main_my_resume_RealName);
        school = (RelativeLayout) findViewById(R.id.main_my_resume_school);
        major = (RelativeLayout) findViewById(R.id.main_my_resume_major);
        introduce = (RelativeLayout) findViewById(R.id.main_my_resume_introduce);
        city = (RelativeLayout) findViewById(R.id.main_my_resume_city);

        userBirth = (TextView) findViewById(R.id.main_my_resume_UserBirth);
        userGender = (TextView) findViewById(R.id.main_my_resume_usergender);
        userStudent = (TextView) findViewById(R.id.main_my_resume_isStudent);
        userHeight = (TextView) findViewById(R.id.main_my_resume_myHeight);
        userintoTime = (TextView) findViewById(R.id.main_my_resume_myintoTime);
        userName = (TextView) findViewById(R.id.main_my_resume_userName);
        userSchool = (TextView) findViewById(R.id.main_my_resume_userSchool);
        userMajor = (TextView) findViewById(R.id.main_my_resume_userMajor);
        save = (TextView) findViewById(R.id.main_my_resumeSave);
        userIntroduce = (TextView) findViewById(R.id.main_my_resume_UserIntroduce);
        userCity = (TextView) findViewById(R.id.main_my_resume_UserCity);

    }

    private void initView() {
        if (user.getRealname().length() != 0) {
            userName.setText(user.getRealname());
        }
        if (user.getGender().length() != 0) {
            userGender.setText(user.getGender());
        }
        if (user.getBirthday().length() != 0) {
            userBirth.setText(user.getBirthday());
        }
        if (user.getHeight() != 0) {
            UHeight = user.getHeight();
            userHeight.setText(UHeight + "cm");
        }
        if (user.getCity().length() != 0) {
            userCity.setText(user.getCity());
        }
        if (user.getStudent()==1) {
            userStudent.setText("是");
        }
        if (user.getSchool().length() != 0) {
            userSchool.setText(user.getSchool());
        }
        if (user.getCollege().length() != 0) {
            userMajor.setText(user.getCollege());
        }
        if (user.getIntroduction().length() != 0) {
            userIntroduce.setText(user.getIntroduction());
        }
        if (user.getIntoyear().length() != 0) {
            userintoTime.setText(user.getIntoyear());
        }
        initUserIcn();
    }
    private void initUserIcn(){
        File file = new File( user.getIcnFile());
        Log.w(TAG," 用户的头像路径为： "+file);
        if(file.exists()){
            Log.w(TAG,"从本地获取头像 ");
            userIcn.setImageDrawable(Drawable.createFromPath(user.getIcnFile()));
        }
    }

    private void setlister() {
        back.setOnClickListener(new mylister());
        changeIcn.setOnClickListener(new mylister());
        gender.setOnClickListener(new mylister());
        student.setOnClickListener(new mylister());
        birthday.setOnClickListener(new mylister());
        height.setOnClickListener(new mylister());
        into.setOnClickListener(new mylister());
        RealName.setOnClickListener(new mylister());
        school.setOnClickListener(new mylister());
        major.setOnClickListener(new mylister());
        save.setOnClickListener(new mylister());
        introduce.setOnClickListener(new mylister());
        city.setOnClickListener(new mylister());

    }
    private class mylister implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.myWorked_Edit_back:
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    break;
                case R.id.main_my_resume_changeIcn:
                    changeIcn();
                    break;
                case R.id.main_my_resume_gender:
                    changeGender();
                    break;
                case R.id.main_my_resume_student:
                    isStudent();
                    break;
                case R.id.main_my_resume_birthday:
                    getBirthday();
                    break;
                case R.id.main_my_resume_height:
                    getHeight();
                    break;
                case R.id.main_my_resume_intotime:
                    getIntoTime();
                    break;
                case R.id.main_my_resume_RealName:
                    getRealName();
                    break;
                case R.id.main_my_resume_school:
                    getSchool();
                    break;
                case R.id.main_my_resume_major:
                    getMajor();
                    break;
                case R.id.main_my_resumeSave:
                    save();
                    break;
                case R.id.main_my_resume_introduce:
                    getIntroduce();
                    break;
                case R.id.main_my_resume_city:
                    getCity();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 提交
     */
    private void save() {
        alertDialogBuilder =new AlertDialog.Builder(myworkedEdit.this);
        View view =  getLayoutInflater().from(this).inflate(R.layout.jianzhi_message_dialog,null);
        alertDialog = alertDialogBuilder
                .setView(view)
                .create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        postImage();
        SaveUserMes();
        JSONObject jsonObject = ToJson();
        postjson(jsonObject);
    }
    /**
     *本地保存用户信息
     */

    private void SaveUserMes() {
        user.setRealname(userName.getText().toString());
        user.setGender(userGender.getText().toString());
        user.setBirthday(userBirth.getText().toString());
        user.setHeight(UHeight);
        user.setSchool(userSchool.getText().toString());
        user.setStudent(isStudent);
        user.setCollege(userMajor.getText().toString());
        user.setIntoyear(userintoTime.getText().toString());
        user.setIntroduction(userIntroduce.getText().toString());
        user.setCity(userCity.getText().toString());
    }
    /**
     * 将用户信息转换为json
    */

    @NonNull
    private JSONObject ToJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("realname", user.getRealname());
        map.put("uid",user.getUid());
        map.put("gender", user.getGender());
        map.put("birthday",user.getBirthday());
        map.put("height",user.getHeight());
        map.put("school", user.getSchool());
        map.put("isStu",user.getStudent());
        map.put("college", user.getCollege());
        map.put("goodas",100+"");
        map.put("intoyear", user.getIntoyear());
        map.put("introduction", user.getIntroduction());
        map.put("city", user.getCity());
        JSONObject jsonObject = new JSONObject(map);
        Log.e("Edit","user json is "+jsonObject.toString());
        return jsonObject;
    }

    /**
     * 提交json
     * @param object
     */
    private void postjson(JSONObject object){
        Log.e(TAG,"postJson now !");
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                object.toString());
        Request request = new Request.Builder()
                .url(JsonUrl)
                .post(requestBody)
                .build();
           mOkHttpClient.newCall(request).enqueue(new Callback(){
                @Override
                public void onFailure(Call call, IOException e) {

                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.i(TAG," postJson response code is "+response.code()+" body is  "+response.body().string());
//                    if(response.code()==200){
//                        handler.sendEmptyMessage(0x1);
//                    }
                }
            });
    }

    /**
     * 上传图片
     * @return
     */
    private String postImage() {
        String result = "error";
        Log.e(TAG,"postFile is "+savefile);
        File file = new File(savefile);
        if(!file.exists()) return result;
        user.setIcnFile(savefile);

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("file", "\\"+user.getUsername()+".jpg",
                RequestBody.create(MediaType.parse("image/jpeg"), new File(savefile)))
        .addFormDataPart("uid",user.getUid()+"");
        RequestBody requestBody = builder.build();
        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(ImageUrl)
                .post(requestBody)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
                 @Override
                 public void onFailure(Call call, IOException e) {
                     Log.e(TAG," postImage have error ");
                     e.printStackTrace();
                     handler.sendEmptyMessage(0x2);
                 }
                 @Override
                 public void onResponse(Call call, Response response) throws IOException {
                     Log.e(TAG," post image have response "+ response.toString());
                     handler.sendEmptyMessage(0x1);
                 }
             });
//        try{
//            Response response = mOkHttpClient.newCall(request).execute();
//            Log.d("responseCode", "响应码 " + response.code());
//            if (response.isSuccessful()) {
//                String resultValue = response.body().string();
//                Log.d("response", "响应体 " + resultValue);
//                return resultValue;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return result;
    }
    //获取姓名的对话框
    private void getRealName() {
        ResumeRealNameDialog Namedialog = new ResumeRealNameDialog(this);
        dialog = Namedialog.getNameDialog("请输入你的真实姓名");
        Namedialog.setlister(new ResumeLister() {
            @Override
            public void getName(String name) {
                userName.setText(name);
            }
        });
        dialog.show();

    }
    //获取学校的对话框
    private void getSchool() {
        ResumeRealNameDialog Namedialog = new ResumeRealNameDialog(this);
        dialog = Namedialog.getNameDialog("请输入所在学校");
        Namedialog.setlister(new ResumeLister() {
            @Override
            public void getName(String name) {
                userSchool.setText(name);
            }
        });
        dialog.show();
    }

    //修改性别的对话框
    private void changeGender() {
        ResumeEditDialog edit = new ResumeEditDialog(myworkedEdit.this, "男", "女");
        dialog = edit.getEditDialog();
        edit.setlister(new ResumeEditDialog.ResumeLister() {
            @Override
            public void Item1() {
                userGender.setText("男");
            }

            @Override
            public void Item2() {
                userGender.setText("女");
            }
        });
        dialog.show();
    }

    //是否为学生对话框
    public void isStudent() {
        ResumeEditDialog edit = new ResumeEditDialog(myworkedEdit.this, "是", "否");
        dialog = edit.getEditDialog();
        edit.setlister(new ResumeEditDialog.ResumeLister() {
            @Override
            public void Item1() {
                isStudent = 1;
                userStudent.setText("是");
            }

            @Override
            public void Item2() {
                isStudent = 0;
                userStudent.setText("否");
            }
        });
        dialog.show();
    }
    /**
     * 获取生日对话框
     */
    private void getBirthday() {
        ResumeBirthdayDialog edit = new ResumeBirthdayDialog(this);
        edit.setlister(new ResumeBirthdayDialog.ResumeLister() {
            @Override
            public void getTime(int year, int monthOfYear, int dayOfMonth) {
                birth = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                userBirth.setText(birth);
            }
        });
        dialog = edit.getEditDialog();
        dialog.show();
    }

    /**
     * 获取身高对话框
     */
    private void getHeight() {
        ResumeHeightDialog edit = new ResumeHeightDialog(this);
        edit.setlister(new ResumeHeightDialog.ResumeLister() {
            @Override
            public void getHeight(Boolean isChange, int height) {
                    UHeight = height;
                userHeight.setText(height + "cm");
            }
        });
        dialog = edit.getEditDialog();
        dialog.show();
    }

    /**
     * 获取专业信息对话框
     */
    private void getMajor() {
        ResumeRealNameDialog Namedialog = new ResumeRealNameDialog(this);
        dialog = Namedialog.getNameDialog("请输入你的专业");
        Namedialog.setlister(new ResumeLister() {
            @Override
            public void getName(String name) {
                subject = name;
                userMajor.setText(name);
            }
        });
        dialog.show();
    }

    /**
     * 获取入学时间对话框
     */
    private void getIntoTime() {
        ResumeIntoTimeDialog edit = new ResumeIntoTimeDialog(this);
        edit.setlister(new ResumeIntoTimeDialog.ResumeLister() {
            @Override
            public void getTime(int year, int monthOfYear, int dayOfMonth) {
                userintoTime.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        });
        dialog = edit.getEditDialog();
        dialog.show();
    }
    //获取自我介绍的对话框
    private void getIntroduce() {
        ResumeRealNameDialog Namedialog = new ResumeRealNameDialog(this);
        dialog = Namedialog.getNameDialog("自我介绍");
        Namedialog.setlister(new ResumeLister() {
            @Override
            public void getName(String name) {
                userIntroduce.setText(name);
            }
        });
        dialog.show();
    }
    //获取城市的对话框
    private void getCity() {
        ResumeRealNameDialog Namedialog = new ResumeRealNameDialog(this);
        dialog = Namedialog.getNameDialog("所在城市");
        Namedialog.setlister(new ResumeLister() {
            @Override
            public void getName(String name) {
                userCity.setText(name);
            }
        });
        dialog.show();
    }
    //修改头像的对话框
    private void changeIcn() {
        ResumeEditDialog edit = new ResumeEditDialog(myworkedEdit.this, "拍照", "从本地相册选择");
        dialog = edit.getEditDialog();
        edit.setlister(new ResumeEditDialog.ResumeLister() {
            @Override
            public void Item1() {
                //拍照
                //判断SD卡是否存在
                String SDState = Environment.getExternalStorageState();
                if (SDState.equals(Environment.MEDIA_MOUNTED)) {
                    Log.i(TAG,"-->进入拍照");
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri imageUri = Uri.fromFile(new File(savefile));
                    //设置输出路径
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    //设置输出格式
                    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                    //是否以bitmap 形式返回照片
                    intent.putExtra("return-data", false);
                    startActivityForResult(intent, TAKE_PHOTO_WITH_DATE);
                }
            }
            @Override
            public void Item2() {
                String SDState = Environment.getExternalStorageState();
                if (SDState.equals(Environment.MEDIA_MOUNTED)) {
                    Log.i(TAG,"-->进入图库");
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra("return_data", true);
                    startActivityForResult(intent, TAKE_PHOTO_FROM_IMAGE);
                }
            }
        });
        dialog.show();
    }
    /**
     * 获取系统拍照或图库的回调函数
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        Log.i(TAG,"Result Coode is "+resultCode+" requestCode is "+requestCode);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            //在拍照中获取图片
            case TAKE_PHOTO_WITH_DATE:
                getphoto();
                break;
            case TAKEA_PICTURE_COPY:
                getSavePic();
                break;
            //在图库中获取图片
            case TAKE_PHOTO_FROM_IMAGE:
                pickphoto(data);
                break;
        }
    }

    /**
     * 在拍照中获取图片
     */
    private void getphoto(){
        //由file获取拍照的图片
        uri = Uri.fromFile(new File(savefile));
        Log.i(TAG,"-->获取拍照图片 file is :\n "+savefile+ " translate uri is ：\n "+uri);
        cropRawPhoto(uri);
    }

    /**
     * 获取剪切过的图片
     */
    private void getSavePic(){
        Log.w(TAG,"-->>获取剪切过的图片 \n " + savefile);
         Bitmap bitmap1 = BitmapFactory.decodeFile(savefile);
//        //进行压缩
        zoomBitmap(bitmap1, bitmap1.getWidth() / 10 , bitmap1.getHeight() / 10);
        userIcn.setImageDrawable(Drawable.createFromPath(savefile));
//        if (bitmap!= null) {
//            Log.i(TAG,"-->ImageView加载图片");
//            imageView.setImageBitmap(bitmap);
////            Log.i(TAG,"bitmap 已经被recycle");
////            bitmap.recycle();
//        }
    }

    /**
     * 在图库中获取图片
     * @param data
     */
    private void pickphoto(Intent data){
        if(data==null){
            return;
        }
        uri = data.getData();
        cropRawPhoto(uri);
        /**
         * 有些手机不能直接获取正确的路径，如魅族手机，由以下方法可以解决
         */
//        String picturePath = "";
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
//        if (cursor != null && cursor.moveToFirst()) {
//            int columnIndex = cursor.getColumnIndex(proj[0]);
//            picturePath = cursor.getString(columnIndex);
//            System.out.println("-=-魅族手机file==->>picturePath = " + picturePath);
//        }
    }

    /**
     * 使用系统进行剪切图片
     * @param uri
     */
    public void cropRawPhoto(Uri uri) {
        //不存在文件所在的包就创建 filePath is 保存图片的包路径
        File localFile = new File(filePath);
        if (localFile.exists()) {
            localFile.delete();
        }
        localFile.mkdir();

        //创建文件路径
//        File file = new File(savefile);
//        if(file.exists()){
//            file.delete();
//            Log.w(TAG,"savefile :" + file +" is exists and  be deleted");
//        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        //根据uri 打开图片
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("scale", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //将存储到指定路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri.fromFile(new File(savefile)));
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, TAKEA_PICTURE_COPY);
    }
    /**
     * 由uri转换为file
     * @param uri
     * @return
     */
//    private String UriToFile(Uri uri){
//        String[] proj = { MediaStore.Images.Media.DATA };
//        Cursor actualimagecursor = this.managedQuery(uri,proj,null,null,null);
//        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        actualimagecursor.moveToFirst();
//        String img_path = actualimagecursor.getString(actual_image_column_index);
//        return img_path;
//    }
    /**
     * 压缩图片
     * 大于1M 时默认缩小10倍
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public void zoomBitmap(Bitmap bitmap, int width, int height) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        int quality = 1;
//        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
//        while (baos.toByteArray().length > 45 * 1024) {
//            baos.reset();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
//            quality -= 0.2;
//            System.out.println(baos.toByteArray().length);
//        }
//        Log.e("zoombitmap","zoombitmap bitmap size is "+baos.toByteArray().length+"k");
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Log.e(TAG,"bitmap size is "+(w * h /1024)+"k");

        if((w * h )/1024<1000){
            Log.w(TAG,"size small then 1000k 缩小4倍 ");
            height =  width = w/4;
        }

        if((w * h )/1024<500){
            Log.w(TAG,"size small then 500k 缩小2倍 ");
            height =  width = w/2;
        }
        if( (w * h )/1024<300) {
            Log.w(TAG,"size small then 300k  return ");
            return ;
        }

        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        bitmap.recycle();
        Log.e("bitmap","new bitmap size is "+(newbmp.getWidth() * newbmp.getHeight()/1024)+"k");
        saveImage(newbmp);
    }

    /**
     * b保存压缩后的图片
     * @param bitmap
     */
    private void saveImage(Bitmap bitmap){
        //存储压缩后的图片
        try {
            FileOutputStream out = new FileOutputStream(savefile);
            if(bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)){
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
