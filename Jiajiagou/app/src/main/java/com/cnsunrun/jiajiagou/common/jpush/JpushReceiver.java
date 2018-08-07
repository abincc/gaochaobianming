package com.cnsunrun.jiajiagou.common.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.utils.LogUtils;
import com.cnsunrun.jiajiagou.MainActivity;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.forum.PostsDetailActicity;
import com.cnsunrun.jiajiagou.personal.MessageDetailActivity;
import com.cnsunrun.jiajiagou.personal.chat.ChatActivity;
import com.cnsunrun.jiajiagou.personal.information.InformationActivity;
import com.google.gson.Gson;


import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

/**
 * Description:
 * Data：2016/12/14 0014-下午 12:31
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class JpushReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction()))
        {
//            Logger.d(TAG, "JPush用户注册成功");
            LogUtils.d("JPush用户注册成功");
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction()))
        {
//            Logger.d(TAG, "接受到推送下来的自定义消息");
            LogUtils.d("接受到推送下来的自定义消息");
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction()))
        {
//            Logger.d(TAG, "接受到推送下来的通知");

            LogUtils.d("接受到推送下来的通知");
            resoveNotidication(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()))
        {
//            Logger.d(TAG, "用户点击打开了通知");
            LogUtils.d("用户点击打开了通知");

            openNotidication(context, bundle);

        }
    }


    private void resoveNotidication(Context context, Bundle bundle){
        LogUtils.d(bundle + "");
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        LogUtils.d(extras);
//        Intent intent;
        try
        {
            JpushBean jpushBean = new Gson().fromJson(extras, JpushBean.class);
            LogUtils.d("-----" + jpushBean.getId() + "---------");
            if (jpushBean != null)
            {
                switch (jpushBean.getType())
                {
                    case 5:
                        //聊天信息广播
                        if(jpushBean.getTime() != null || jpushBean.getTime() != ""){
                            chatMessage(context, jpushBean.getMid(), jpushBean.getContent(), jpushBean.getTime());
                        }else{
                            chatMessage(context, jpushBean.getMid(), jpushBean.getContent());
                        }
                        break;
                    default:
                        break;
                }
                return;
            }
        } catch (Exception e)
        {
            LogUtils.d("---------JpushReceiver.resoveNotidication方法---聊天模块报错-----------");
        }
    }

    private void openNotidication(Context context, Bundle bundle)
    {
        LogUtils.d(bundle + "");
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        LogUtils.d(extras);
        Intent intent;
        try
        {
            JpushBean jpushBean = new Gson().fromJson(extras, JpushBean.class);
            LogUtils.d("-----" + jpushBean.getId() + "---------");
            if (jpushBean != null)
            {
                if (jpushBean.getId() == 0)
                {
                    intent = new Intent(context, InformationActivity.class);
                    intent.putExtra("pos", jpushBean.getType());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else
                {
                    //跳转对应详情
                    switch (jpushBean.getType())
                    {
                        case 0:
                            //论坛
                            enterDetail(context,"论坛",jpushBean.getId());
                            break;
                        case 1:
                            //物业
                            enterDetail(context,"物业",jpushBean.getId());
                            break;
                        case 2:
                            //订单
                            enterDetail(context,"订单",jpushBean.getId());
                            break;
                        case 3:
                            //欠费
                            enterDetail(context,"欠费",jpushBean.getId());
                            break;
                        case 5:
                            enterChat(context, jpushBean.getRid());
                            break;
                    }
                }
                return;
            }
        } catch (Exception e)
        {
            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(new Intent(context, MainActivity.class));
        }
        intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(new Intent(context, MainActivity.class));
    }


    private void chatMessage(Context context, String mid, String content){

        //广播聊天消息--让聊天界面更新数据
        Intent chatIntent = new Intent(ChatActivity.CHAT_RECEIVER_ACTION);
        chatIntent.putExtra("mid", mid);
        chatIntent.putExtra("content", content);
        context.sendBroadcast(chatIntent);
    }

    private void chatMessage(Context context, String mid, String content, String time){
        //广播聊天消息--让聊天界面更新数据
        Intent chatIntent = new Intent(ChatActivity.CHAT_RECEIVER_ACTION);
        chatIntent.putExtra("mid", mid);
        chatIntent.putExtra("content", content);
        chatIntent.putExtra("time", time);
        context.sendBroadcast(chatIntent);
    }

    private void enterChat( Context context, String rid){
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("rid", Integer.valueOf(rid));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void enterDetail(Context context,String type, int id){
        Bundle bundle=new Bundle();
        Intent intent;
        if (type.equals("论坛")) {
            bundle.putString(ConstantUtils.POSTS_ITEM_ID, id+"");
            intent = new Intent(context, PostsDetailActicity.class);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            bundle.clear();
            return;
        }
        bundle.putString(ConstantUtils.MESSAGE_ID,id+"");
        bundle.putString(ConstantUtils.MESSAGE_TYPE,type);
        intent = new Intent(context, MessageDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        context.startActivity(intent);
        bundle.clear();
    }
}
