package nongbang.hg.nongbang;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nongbang.hg.nongbang.MSQLite.ZWDBOpenHelper;
import nongbang.hg.nongbang.MSQLite.ZwDb;
import nongbang.hg.nongbang.MyAdapter.ZwListAdapter;
import nongbang.hg.nongbang.MyAdapter.ZyListAdapter;
import nongbang.hg.nongbang.tools.Vibration;
import nongbang.hg.nongbang.view.ScanDialog;

/**
 *整个应用
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener,AbsListView.OnScrollListener{

    private ViewPager viewPager;

    private ImageButton mZyImg;
    private ImageButton mZwImg;
    private ImageButton mWdImg;
    private ImageView scanimg;

    private TextView zwquanbu,zwhuahui,zwzhuzi,zwshu,zwcao;
    private ImageButton zwsousuo;


    private TextView wdzhendong,wdlingsheng,wdzhuangtailan,wdbanben,wdfangwen,wdfenxiang;
    private LinearLayout zynozw;

    private PagerAdapter mAdapter;
    private List<View> mviews=new ArrayList<View>();

    private  List<Map<String,Object>> zwalllisttemp=new ArrayList<Map<String,Object>>();
    private  List<Map<String,Object>> zyalllisttemp=new ArrayList<Map<String,Object>>();
    private  List<Map<String,Object>> zwalllist=new ArrayList<Map<String,Object>>();
    private  List<Map<String,Object>> zyalllist=new ArrayList<Map<String,Object>>();
    public int ZYtag=0,ZWTag=0;
    private ZwListAdapter zwadapter;
    private ZyListAdapter zyadapter;
    public ListView zwlistView,zylistView;
    public View  tab01;
    public View  tab02;
    public View  tab03;

    private ZWDBOpenHelper ZWDBOpenHelper;
    private SQLiteDatabase db,dbzw;
    private ZwDb zwDb;
    // 要申请的权限
    private String[] permissions = {Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
    private AlertDialog dialog;

    public static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mainActivity=this;
        zwDb=new ZwDb(this);
        ZWDBOpenHelper =new ZWDBOpenHelper(MainActivity.this,"mzw.db");
        db= ZWDBOpenHelper.getWritableDatabase();
        dbzw=zwDb.OpenZwdb();
        initView();
        initEvent();
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int x=0;x<permissions.length;x++) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permissions[x]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 如果没有授予该权限，就去提示用户请求
                    startRequestPermission();
                }
            }
        }*/

    }


   /* // 开始提交请求权限
    public void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }*/

   // 用户权限 申请 的回调方法
   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting();
                    } else
                        finish();
                }
            }
        }
    }*/

    // 提示用户去应用设置界面手动开启权限

   /* private void showDialogTipUserGoToAppSettting() {

        dialog = new AlertDialog.Builder(this)
                .setTitle("权限不可用")
                .setMessage("请在-应用设置-权限-中，允许本软件使用存储权限来保存用户数据，以及访问网络")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }*/

    // 跳转到当前应用的设置界面
  /*  public void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, 123);
    }*/

    //
 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSettting();
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }
        }else if(requestCode==124){
            zyadapter.notifyDataSetChanged();
        }
    }*/


    public void initView(){
        viewPager=(ViewPager)findViewById(R.id.VP);
        mZyImg=(ImageButton)findViewById(R.id.zy_img);
        mZwImg=(ImageButton)findViewById(R.id.zw_img);
        mWdImg=(ImageButton)findViewById(R.id.wd_img);


        LayoutInflater mlnflater=LayoutInflater.from(this);
        tab01=mlnflater.inflate(R.layout.zy_layout,null);
        scanimg=(ImageView)tab01.findViewById(R.id.zy_scanning) ;
        scanimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanDialog selectDialog = new ScanDialog(MainActivity.this,R.style.dialog);//创建Dialog并设置样式主题
                Window win = selectDialog.getWindow();
                WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                params.y = -60;//设置y坐标
                win.setAttributes(params);
                selectDialog.setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
                selectDialog.show();

            }
        });
        LinearLayout button=(LinearLayout)tab01.findViewById(R.id.zy_sousuo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FindZyActivity.class));
            }
        });


        tab02=mlnflater.inflate(R.layout.zw_layout,null);
        zwquanbu=(TextView)tab02.findViewById(R.id.zw_quanbu);
        zwhuahui=(TextView)tab02.findViewById(R.id.zw_huahui);
        zwcao=(TextView)tab02.findViewById(R.id.zw_cao);
        zwzhuzi=(TextView)tab02.findViewById(R.id.zw_zhuzi);
        zwshu=(TextView)tab02.findViewById(R.id.zw_shu);
        zwsousuo=(ImageButton)tab02.findViewById(R.id.zw_sousuo);
        zwquanbu.setOnClickListener(this);
        zwhuahui.setOnClickListener(this);
        zwcao.setOnClickListener(this);
        zwzhuzi.setOnClickListener(this);
        zwshu.setOnClickListener(this);
        zwsousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FindZwActivity.class));
            }
        });

        tab03=mlnflater.inflate(R.layout.wd_layout,null);
        zynozw=(LinearLayout)tab01.findViewById(R.id.zynozw);
        zwlistView=(ListView )tab02.findViewById(R.id.zwlistview);

        Cursor cursor= db.rawQuery("select * from mzw order by _id desc",null);//查询
        if (cursor!=null){
            while (cursor.moveToNext()){
                Map<String ,Object> map=new HashMap<String, Object>();
                map.put("mc",cursor.getString(cursor.getColumnIndex("name")));
                map.put("say",cursor.getString(cursor.getColumnIndex("say")));
                zyalllist.add(map);
            }
            cursor.close();//释放游标
        }
        if (zyalllist.size()==0){
            zynozw.setVisibility(View.VISIBLE);
        }
        db.close();//关闭数据库

        //创建数据库 sql语句操作
        Cursor cursor1= dbzw.rawQuery("select * from zw",null);//查询
        if (cursor1!=null){
            while (cursor1.moveToNext()){
                Map<String ,Object> map=new HashMap<String, Object>();
                map.put("mc",cursor1.getString(cursor1.getColumnIndex("name")));
                map.put("xm",cursor1.getString(cursor1.getColumnIndex("xueming")));
                map.put("fl",cursor1.getString(cursor1.getColumnIndex("fenlei")));
                zwalllist.add(map);
            }
            cursor1.close();//释放游标
        }



        zylistView=(ListView)tab01.findViewById(R.id.zylistview);
        zyalllisttemp.clear();
        if (zyalllist.size()>10){
            zyalllisttemp.addAll(zyalllist.subList(ZYtag,ZYtag+10));
            ZYtag=ZYtag+10;
        }else {
            zyalllisttemp.addAll(zyalllist.subList(ZYtag,ZYtag+zyalllist.size()));
            ZYtag=ZYtag+zyalllist.size();
        }
        zylistView.setOnScrollListener(this);
        zyadapter=new ZyListAdapter(zyalllisttemp,this);
        zylistView.setAdapter(zyadapter);


        zwalllisttemp.clear();
        if (zwalllist.size()>10){
            zwalllisttemp.addAll(zwalllist.subList(ZWTag,ZWTag+10));
            ZYtag=ZYtag+10;
        }else {
            zwalllisttemp.addAll(zwalllist.subList(ZWTag,ZWTag+zwalllist.size()));
            ZWTag=ZWTag+zwalllist.size();
        }
        zwlistView.setOnScrollListener(this);
        zwadapter=new ZwListAdapter(zwalllisttemp,this);
        zwlistView.setAdapter(zwadapter);


        zwlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,ZwJsActivity.class);
                intent.putExtra("name",((TextView)view.findViewById(R.id.mingcheng)).getText().toString());
                intent.putExtra("ACT","MainActivity");
                intent.putExtra("xueming",((TextView)view.findViewById(R.id.xueming)).getText().toString().split(":")[1]);
                startActivity(intent);

            }
        });
        zylistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,MyZWActivity.class);
                intent.putExtra("name",((TextView)view.findViewById(R.id.zymingzi)).getText().toString());
                startActivity(intent);
            }
        });
        //绑定我的界面组件并添加时间

        //震动通知按钮
        wdzhendong=(TextView)tab03.findViewById(R.id.wd_zhendong);
        wdzhendong.setOnClickListener(this);
        //铃声通知按钮
        wdlingsheng=(TextView)tab03.findViewById(R.id.wd_lingsheng);
        wdlingsheng.setOnClickListener(this);
        //状态栏通知按钮
        wdzhuangtailan=(TextView)tab03.findViewById(R.id.wd_zhuangtailan);
        wdzhuangtailan.setOnClickListener(this);
        //版本更新按钮
        wdbanben=(TextView)tab03.findViewById(R.id.wd_banben);
        wdbanben.setOnClickListener(this);
        //分享按钮
        wdfenxiang=(TextView)tab03.findViewById(R.id.wd_fenxiang);
        wdfenxiang.setOnClickListener(this);
        //访问我们按钮
        wdfangwen=(TextView)tab03.findViewById(R.id.wd_fangwen);
        wdfangwen.setOnClickListener(this);



        mviews.add(tab01);
        mviews.add(tab02);
        mviews.add(tab03);
        mAdapter=new PagerAdapter() {

            @Override
            public int getCount() {
                return mviews.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view=mviews.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mviews.get(position));
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
        };

        viewPager.setAdapter(mAdapter);
    }

    public void initEvent(){
        mZyImg.setOnClickListener(this);
        mZwImg.setOnClickListener(this);
        mWdImg.setOnClickListener(this);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               reSetImg();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Vibrator vibrator=(Vibrator) getSystemService(VIBRATOR_SERVICE);
        Vibration vibration=new Vibration(vibrator);
        switch (v.getId()){
            case R.id.zy_img:
                viewPager.setCurrentItem(0);
                break;
            case R.id.zw_img:
                viewPager.setCurrentItem(1);
                break;
            case R.id.wd_img:
                viewPager.setCurrentItem(2);
                break;
            case R.id.wd_zhendong:
                if (wdzhendong.getText().toString().compareTo("关 >")==0){
                    vibration.beginVibration();
                    wdzhendong.setText("开 >");
                    ShowText("震动提示已打开");
                }else {
                    vibration.closeVibration();
                    wdzhendong.setText("关 >");
                    ShowText("震动提示已关闭");
                }
                break;
            case R.id.wd_lingsheng:
                startActivity(new Intent(MainActivity.this,test.class));
                if (wdlingsheng.getText().toString().compareTo("关 >")==0){
                    wdlingsheng.setText("开 >");
                   ShowText("声音提示已打开");
                }else {
                    wdlingsheng.setText("关 >");
                   ShowText("声音提示已关闭");
                }
                break;
            case R.id.wd_zhuangtailan:
                if (wdzhuangtailan.getText().toString().compareTo("关 >")==0){
                    wdzhuangtailan.setText("开 >");
                   ShowText("状态栏信息已打开");
                }else {
                    wdzhuangtailan.setText("关 >");
                    ShowText("状态栏信息已关闭");
                }
                break;
            case R.id.wd_banben:
                break;
            case R.id.wd_fenxiang:
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                Uri uri = Uri.parse("http://39.108.70.152/"); intent.putExtra(Intent.EXTRA_STREAM,uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));
                break;
            case R.id.wd_fangwen:
                Intent intent1 = new Intent();
                intent1.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://39.108.70.152/");
                intent1.setData(content_url);
                startActivity(intent1);
                break;

            case R.id.zw_quanbu:
                reSetTextColor();
                zwquanbu.setTextColor(Color.argb(255,255,255,255));
                FLZhanShi("全部");
                break;
            case R.id.zw_huahui:
                reSetTextColor();
                zwhuahui.setTextColor(Color.argb(255,255,255,255));
                FLZhanShi("花卉");
                break;
            case R.id.zw_cao:
                reSetTextColor();
                zwcao.setTextColor(Color.argb(255,255,255,255));
                FLZhanShi("草");
                break;
            case R.id.zw_zhuzi:
                reSetTextColor();
                zwzhuzi.setTextColor(Color.argb(255,255,255,255));
                FLZhanShi("竹子");
                break;
            case R.id.zw_shu:
                reSetTextColor();
                zwshu.setTextColor(Color.argb(255,255,255,255));
                FLZhanShi("树");
                break;
        }
        reSetImg();
    }


    private void reSetTextColor(){
        zwquanbu.setTextColor(Color.argb(255,215,222,215));
        zwhuahui.setTextColor(Color.argb(255,215,222,215));
        zwzhuzi.setTextColor(Color.argb(255,215,222,215));
        zwcao.setTextColor(Color.argb(255,215,222,215));
        zwshu.setTextColor(Color.argb(255,215,222,215));
    }

    /*
    分类展示作物
     */
    private void FLZhanShi(String fl){
        zwalllisttemp.clear();
        zwalllist.clear();
        ZWTag=0;
        Cursor cursor=null;
        if (fl.compareTo("全部")!=0) {
            cursor = dbzw.rawQuery("select * from zw where leixing == '"+fl+"'", null);//查询
        }else {
            cursor = dbzw.rawQuery("select * from zw", null);//查询
        }
        if (cursor!=null){
            while (cursor.moveToNext()){
                Map<String ,Object> map=new HashMap<String, Object>();
                map.put("mc",cursor.getString(cursor.getColumnIndex("name")));
                map.put("xm",cursor.getString(cursor.getColumnIndex("xueming")));
                map.put("fl",cursor.getString(cursor.getColumnIndex("fenlei")));
                zwalllist.add(map);
            }
            cursor.close();//释放游标
        }
        if (zwalllist.size()>10){
            zwalllisttemp.addAll(zwalllist.subList(ZWTag,ZWTag+10));
            ZYtag=ZYtag+10;
        }else {
            zwalllisttemp.addAll(zwalllist.subList(ZWTag,ZWTag+zwalllist.size()));
            ZWTag=ZWTag+zwalllist.size();
        }
        zwadapter.onDataChange(zwalllisttemp);
    }

    /*
    重置底部tab
     */
    public void reSetImg(){
        mZwImg.setImageResource(R.drawable.zw);
        mZyImg.setImageResource(R.drawable.zy);
        mWdImg.setImageResource(R.drawable.wd);
        switch (viewPager.getCurrentItem()){
            case 0:
                mZyImg.setImageResource(R.drawable.zya);
                break;
            case 1:
                mZwImg.setImageResource(R.drawable.zwa);
                break;
            case 2:
                mWdImg.setImageResource(R.drawable.wda);
                break;
        }
    }


    /*
    弹出提示
     */
    public void ShowText(String s){
        Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
    }




    private int totalItemCount;//总数
    private int lastVisibleItem;//最后一个可见item
    private boolean isLoading;//是否处于加载

/*
加载线程
 */
    public void onLoad(int v) {
        final int ID=v;
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getLoadData(ID);
            }
        },500);

    }
/*
判断list然后获取数据
 */
    public void getLoadData(int v){
        switch (v){
            case R.id.zylistview:
                tab01.findViewById(R.id.zy_loadlayout).setVisibility(View.GONE);
                if (zyalllist.size()==zyalllisttemp.size()){
                    Toast.makeText(MainActivity.this,"没有更多了",Toast.LENGTH_SHORT).show();
                    break;
                }else {
                    if (zyalllist.size() - ZYtag > 10) {
                        zyalllisttemp.addAll(zyalllist.subList(ZYtag, ZYtag + 10));
                        ZYtag = ZYtag + 10;
                    } else {
                        zyalllisttemp.addAll(zyalllist.subList(ZYtag, zyalllist.size()));
                        ZYtag = ZYtag + zyalllist.size();
                    }
                    zyadapter.onDataChange(zyalllisttemp);
                }

                break;
            case R.id.zwlistview:
                tab02.findViewById(R.id.zw_loadlayout).setVisibility(View.GONE);
                if (zwalllist.size()==zwalllisttemp.size()){
                    Toast.makeText(MainActivity.this,"没有更多了",Toast.LENGTH_SHORT).show();
                    break;
                }else {
                    if (zwalllist.size() - ZWTag > 10) {
                        zwalllisttemp.addAll(zwalllist.subList(ZWTag, ZWTag + 10));
                        ZWTag = ZWTag + 10;
                    } else {
                        zwalllisttemp.addAll(zwalllist.subList(ZWTag, zwalllist.size()));
                        ZWTag = ZWTag + zwalllist.size();
                    }
                    zwadapter.onDataChange(zwalllisttemp);
                }

                break;
        }
        isLoading = false;
    }


    private boolean REF=true;
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (totalItemCount==lastVisibleItem && scrollState==SCROLL_STATE_IDLE && REF){
            if(!isLoading) {
                if (view.getId()==R.id.zylistview) {
                    isLoading = true;
                    tab01.findViewById(R.id.zy_loadlayout).setVisibility(View.VISIBLE);
                }
                if (view.getId()==R.id.zwlistview) {
                    isLoading = true;
                    tab02.findViewById(R.id.zw_loadlayout).setVisibility(View.VISIBLE);
                }
                onLoad(view.getId());
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem==0)
            REF=false;
        else
            REF=true;
        this.lastVisibleItem=firstVisibleItem+visibleItemCount;
        this.totalItemCount=totalItemCount;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbzw.close();//关闭作物数据库
    }
}