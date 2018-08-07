package com.cnsunrun.jiajiagou.personal.chat;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.LogUtils;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Call;

public class ChatActivity extends BaseHeaderActivity {

    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private EditText et;
    private TextView tvSend;
    private String content;
    private TextView tvTitle1;

    //房号
    int rid = 0;
    //me 用户
    int mid = 0;
    String mnickname = "";
    String mmobile = "";
    String mheadimg = "";

    //other 用户
    int oid = 0;
    String onickname = "";
    String omobile = "";
    String oheadimg = "";
    ArrayList<ItemModel> models = new ArrayList<>();
    String dataTime = "";

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("欢迎进入");
        tvTitle1 = tvTitle;
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.chat_activity;
    }

    @Override
    protected void init()
    {
        adapter = new ChatAdapter();
        getChatData();
        recyclerView = (RecyclerView) findViewById(R.id.recylerView);
        et = (EditText) findViewById(R.id.et);
        tvSend = (TextView) findViewById(R.id.tvSend);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        initData();
        registMessageReceiver();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //销毁广播接受器
        mContext.unregisterReceiver(mChatActivityReceiver);
    }

    //初始化聊天信息  获取两个用户聊天的信息
    private void getChatData(){
        rid = getIntent().getIntExtra("rid", 0);
        HashMap<String, String> map = new HashMap();
        map.put("rid", String.valueOf(rid));
        map.put("token", JjgConstant.getToken(mContext));
        HttpUtils.post(NetConstant.GET_ROOM_USER_INFO, map, new DialogCallBack((Refreshable) ChatActivity.this) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }
            @Override
            public void onResponse(String response, int id) {
                RLBean rlBean = new Gson().fromJson(response, RLBean.class);
                if (handleResponseCode(rlBean)) {
                    if (rlBean.getStatus() == 1) {
                        //初始化聊天信息
                        mid = Integer.valueOf(rlBean.getInfo().getMe().getId());
                        mmobile = rlBean.getInfo().getMe().getMobile();
                        mnickname = rlBean.getInfo().getMe().getNickname();
                        mheadimg = rlBean.getInfo().getMe().getHeadimg();

                        oid = Integer.valueOf(rlBean.getInfo().getOther().getId());
                        omobile = rlBean.getInfo().getOther().getMobile();
                        onickname = rlBean.getInfo().getOther().getNickname();
                        oheadimg = rlBean.getInfo().getOther().getHeadimg();

                        if(rlBean.getInfo().getOther().getNickname() != null){
                            tvTitle1.setText(rlBean.getInfo().getOther().getNickname());
                        }else{
                            tvTitle1.setText(getNum(rlBean.getInfo().getOther().getMobile()));
                        }

                        LogUtils.d("----------" + oheadimg + "=====" + mheadimg);

                        /**
                         * 获取聊天记录
                         */
                        getAdData();
                    }
                }
            }
        });
    }

    /**
     * 获取聊天记录
     */
    private void getAdData(){

        rid = getIntent().getIntExtra("rid", 0);
        HashMap<String, String> map = new HashMap();
        map.put("rid", String.valueOf(rid));
        map.put("token", JjgConstant.getToken(mContext));
        HttpUtils.get(NetConstant.GET_CHAT_MESSAGE, map, new DialogCallBack((Refreshable) ChatActivity.this) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }
            @Override
            public void onResponse(String response, int id) {
                MessageBean messageBean = new Gson().fromJson(response, MessageBean.class);
                if (handleResponseCode(messageBean)) {
                    if (messageBean.getStatus() == 1) {
                        for(int i=0; i<messageBean.getInfo().size(); i++){
                            dataTime = messageBean.getInfo().get(i).getTime();
                            switch (messageBean.getInfo().get(i).getType()){
                                case 0:
                                    if(messageBean.getInfo().get(i).getUid() == Integer.valueOf(mid)){
                                        ChatModel model = new ChatModel();
                                        model.setContent(messageBean.getInfo().get(i).getContent());
                                        model.setIcon(mheadimg);
                                        models.add(new ItemModel(ItemModel.CHAT_B, model));
                                    }else{
                                        ChatModel model = new ChatModel();
                                        model.setContent(messageBean.getInfo().get(i).getContent());
                                        model.setIcon(oheadimg);
                                        models.add(new ItemModel(ItemModel.CHAT_A, model));
                                    }
                                    break;
                                case 1:
                                    break;
                                case 2:
                                    break;
                                case 3:
                                    break;
                                case 4:
                                    break;
                                case 5:
                                    ChatModel model = new ChatModel();
                                    model.setContent(getNewChatTime(getTimeStamp(messageBean.getInfo().get(i).getTime())));
                                    models.add(new ItemModel(ItemModel.CHAT_T, model));
                                    break;
                                default:
                                    break;
                            }
                        }

                        LogUtils.d("============" + dataTime);
                        // 聊天记录加入 adapter
                        adapter.replaceAll(models);
                        // 是的聊天记录置底
                        recyclerView.scrollToPosition(models.size()-1);
                    }
                }
            }
        });
    }

    private void initData() {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                content = s.toString().trim();
            }
        });

        tvSend.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if(content == null || content.equals("")){
                    Toast ts = Toast.makeText(mContext,"发送内容不能为空!", Toast.LENGTH_LONG);
                    ts.show() ;
                }else if(content.length() > 250){
                    Toast ts = Toast.makeText(mContext,"超出"+ (content.length() - 250) +"字数!", Toast.LENGTH_LONG);
                    ts.show() ;
                }else{

                    /**
                     * 极光推送消息
                     * @param userid  content  name{nickname, mobile}
                     * @author abincc
                     * @time 2018-06-12
                     */

                    HashMap<String, String> map = new HashMap();
                    map.put("uid", String.valueOf(oid));
                    map.put("token", JjgConstant.getToken(ChatActivity.this));
                    map.put("mname", mnickname == null?getNum(mmobile):mnickname);
                    map.put("rid", String.valueOf(rid));
                    map.put("content", content);
                    map.put("type", "0");
                    map.put("dataTime", dataTime);
                    HttpUtils.get(NetConstant.CHAT_SEND_CONTENT, map, new DialogCallBack((Refreshable) ChatActivity.this) {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            ChatBean chatBean = new Gson().fromJson(response, ChatBean.class);
                            if (handleResponseCode(chatBean)) {

                                if (chatBean.getStatus() == 1) {
                                    if (Integer.valueOf(chatBean.getInfo().getStatus()) == 1) {
                                        ArrayList<ItemModel> data = new ArrayList<>();
                                        ChatModel model = new ChatModel();
                                        model.setIcon(mheadimg);
                                        model.setContent(content);
                                        data.add(new ItemModel(ItemModel.CHAT_B, model));
                                        adapter.addAll(data);
                                        et.setText("");
                                        hideKeyBorad(et);
                                        models.add(new ItemModel(ItemModel.CHAT_B, model));
                                        recyclerView.scrollToPosition(models.size()-1);
                                    }else if(Integer.valueOf(chatBean.getInfo().getStatus()) == 2){
                                        ArrayList<ItemModel> data = new ArrayList<>();
                                        ChatModel modelt = new ChatModel();
                                        modelt.setContent(getNewChatTime(getTimeStamp(getSystemTime())));
                                        data.add(new ItemModel(ItemModel.CHAT_T, modelt));
                                        models.add(new ItemModel(ItemModel.CHAT_T, modelt));

                                        ChatModel model = new ChatModel();
                                        model.setIcon(mheadimg);
                                        model.setContent(content);
                                        data.add(new ItemModel(ItemModel.CHAT_B, model));

                                        adapter.addAll(data);
                                        et.setText("");
                                        hideKeyBorad(et);
                                        models.add(new ItemModel(ItemModel.CHAT_B, model));

                                        recyclerView.scrollToPosition(models.size()-1);
                                    }
                                    dataTime = getSystemTime();

                                    LogUtils.d("-----ggggg-----" + dataTime);
                                }
                            }
                        }
                    });
                }
            }
        });

    }

    private void hideKeyBorad(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    private String getNum(String mobile) {
        String str = mobile;
        //字符串截取
        String bb =str.substring(3,7);
        //字符串替换
        String cc = str.replace(bb,"****");
        return cc;
    }


    /**
     *  时间戳格式转换
     */
    static String dayNames[] = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    public static String getNewChatTime(long timesamp) {
        String result = "";
        Calendar todayCalendar = Calendar.getInstance();
        Calendar otherCalendar = Calendar.getInstance();
        otherCalendar.setTimeInMillis(timesamp);

        String timeFormat="M月d日 HH:mm";
        String yearTimeFormat="yyyy年M月d日 HH:mm";
        String am_pm="";
        int hour=otherCalendar.get(Calendar.HOUR_OF_DAY);
        if(hour>=0&&hour<6){
            am_pm="凌晨";
        }else if(hour>=6&&hour<12){
            am_pm="早上";
        }else if(hour==12){
            am_pm="中午";
        }else if(hour>12&&hour<18){
            am_pm="下午";
        }else if(hour>=18){
            am_pm="晚上";
        }
        timeFormat="M月d日 "+ am_pm +"HH:mm";
        yearTimeFormat="yyyy年M月d日 "+ am_pm +"HH:mm";

        boolean yearTemp = todayCalendar.get(Calendar.YEAR)==otherCalendar.get(Calendar.YEAR);
        if(yearTemp){
            int todayMonth=todayCalendar.get(Calendar.MONTH);
            int otherMonth=otherCalendar.get(Calendar.MONTH);
            if(todayMonth==otherMonth){//表示是同一个月
                int temp=todayCalendar.get(Calendar.DATE)-otherCalendar.get(Calendar.DATE);
                switch (temp) {
                    case 0:
                        result = getHourAndMin(timesamp);
                        break;
                    case 1:
                        result = "昨天 " + getHourAndMin(timesamp);
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        int dayOfMonth = otherCalendar.get(Calendar.WEEK_OF_MONTH);
                        int todayOfMonth=todayCalendar.get(Calendar.WEEK_OF_MONTH);
                        if(dayOfMonth==todayOfMonth){//表示是同一周
                            int dayOfWeek=otherCalendar.get(Calendar.DAY_OF_WEEK);
                            if(dayOfWeek!=1){//判断当前是不是星期日     如想显示为：周日 12:09 可去掉此判断
                                result = dayNames[otherCalendar.get(Calendar.DAY_OF_WEEK)-1] + getHourAndMin(timesamp);
                            }else{
                                result = getTime(timesamp,timeFormat);
                            }
                        }else{
                            result = getTime(timesamp,timeFormat);
                        }
                        break;
                    default:
                        result = getTime(timesamp,timeFormat);
                        break;
                }
            }else{
                result = getTime(timesamp,timeFormat);
            }
        }else{
            result=getYearTime(timesamp,yearTimeFormat);
        }
        return result;
    }

    /**
     * 当天的显示时间格式
     * @param time
     * @return
     */
    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

    /**
     * 不同一周的显示时间格式
     * @param time
     * @param timeFormat
     * @return
     */
    public static String getTime(long time,String timeFormat) {
        SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        return format.format(new Date(time));
    }

    /**
     * 不同年的显示时间格式
     * @param time
     * @param yearTimeFormat
     * @return
     */
    public static String getYearTime(long time,String yearTimeFormat) {
        SimpleDateFormat format = new SimpleDateFormat(yearTimeFormat);
        return format.format(new Date(time));
    }

    /**
     * 获取指定日期时间戳
     */
    public long getTimeStamp(String time){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = (Date) df.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long timestamp = cal.getTimeInMillis();
        return timestamp;
    }

    /**
     *  获取系统时间
     */
    public String getSystemTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }


    //自定义广播接受器
    private ChatActivityReceiver mChatActivityReceiver;
    public static final String CHAT_RECEIVER_ACTION = "com.cnsunrun.jiajiagou.CHAT_RECEVIED_ACTION";

    public void registMessageReceiver(){
        mChatActivityReceiver = new ChatActivityReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(CHAT_RECEIVER_ACTION);
        mContext.registerReceiver(mChatActivityReceiver, filter);
    }

    public class ChatActivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (CHAT_RECEIVER_ACTION.equals(intent.getAction())) {

                String mid1 = intent.getStringExtra("mid");
                String content = intent.getStringExtra("content");
                if(Integer.valueOf(mid1).equals(Integer.valueOf(oid))){

                    if(intent.getStringExtra("time") != null){
                        ChatModel model = new ChatModel();
                        model.setContent(getNewChatTime(getTimeStamp(intent.getStringExtra("time"))));
                        models.add(new ItemModel(ItemModel.CHAT_T, model));
                    }

                    ChatModel model = new ChatModel();
                    model.setContent(content);
                    model.setIcon(oheadimg);
                    models.add(new ItemModel(ItemModel.CHAT_A, model));
                }
                adapter.replaceAll(models);
                recyclerView.scrollToPosition(models.size()-1);
            }
        }
    }

}
