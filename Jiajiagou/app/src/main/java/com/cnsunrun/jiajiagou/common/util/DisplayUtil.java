package com.cnsunrun.jiajiagou.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sunny on 2016/11/17.
 */

public class DisplayUtil {
  public static final int DOT_ONE = 1;//保留一位小数
  public static final int DOT_TWO = 2;//保留二位小数
  //public static final String REGIONDATA = "regiondata";
  public static final String BASEDATA = "basedata";
  private static int[] sizeTable =
          {9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE};

  private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();

  public static int dip2px(Context context, float dipValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dipValue * scale + 0.5f);
  }

  public static int sp2px(Context context, float spValue) {
    final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
    return (int) (spValue * fontScale + 0.5f);
  }

  /**
   * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
   */
  public static int px2dip(Context context, float pxValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);
  }


  /**
   * 保留小数点几位
   */
  public static String doubleDot(Double value, int dot) {
    return String.format("%." + dot + "f", value);
  }

  /**
   * 把毫秒转化成日期
   */
  public static String transferLongToDate(Long millSec) {
    if (millSec != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date date = new Date(millSec);
      return sdf.format(date);
    }
    return "";
  }

  /**
   * 把毫秒转化成日期
   */
  public static String transferLongToDateTwo(Long millSec) {
    if (millSec != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      Date date = new Date(millSec);
      return sdf.format(date);
    }
    return "";
  }

  /**
   * 把毫秒转化成日期
   */
  public static String transferLongToDateThree(Long millSec) {
    if (millSec != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
      Date date = new Date(millSec);
      return sdf.format(date);
    }
    return "";
  }

  /**
   * 把毫秒转化成时分
   */
  public static String transferLongToHourMinute(Long millSec) {
    if (millSec != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
      Date date = new Date(millSec);
      return sdf.format(date);
    }
    return "";
  }

  /**
   * 把毫秒转化成年月日
   */
  public static String transferLongToYearMonthDay(Long millSec) {
    if (millSec != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
      Date date = new Date(millSec);
      return sdf.format(date);
    }
    return "";
  }

  /**
   * 把毫秒转化成年月日
   */
  public static String transferLongToYearMonth(Long millSec) {
    if (millSec != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
      Date date = new Date(millSec);
      return sdf.format(date);
    }
    return "";
  }

  public static int getScreenWidth(Context context) {
    DisplayMetrics dm = new DisplayMetrics();
    ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
    return dm.widthPixels;
  }

  public static int getScreenHeight(Context context) {
    DisplayMetrics dm = new DisplayMetrics();
    ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
    return dm.heightPixels;
  }

  public static int getActionBarHeight(Context context) {
    TypedValue tv = new TypedValue();
    int actionBarHeight = 0;
    if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
      actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
              context.getResources().getDisplayMetrics());
    }
    return actionBarHeight;
  }

  public static int getStatusBarHeight(Context context) {
    int statusBarHeight = 0;
    int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      //根据资源ID获取响应的尺寸值
      statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
    }
    return statusBarHeight;
  }

  /**
   * 返回当前程序版本名
   */
  public static String getAppVersionName(Context context) {
    String versionName = "";
    try {
      // ---get the package info---
      PackageManager pm = context.getPackageManager();
      PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
      versionName = pi.versionName;
      if (versionName == null || versionName.length() <= 0) {
        return "";
      }
    } catch (Exception e) {
    }
    return versionName;
  }

  public static int getVersionCode(Context context) {
    try {
      PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      return pi.versionCode;
    } catch (Exception e) {
      return 0;
    }
  }

  /**
   * 设置EditText只能输入金额的格式
   */
  public static void setMoneyInpunt(EditText et) {
    setPricePoint(et);
  }

  private static void setPricePoint(final EditText editText) {
    editText.addTextChangedListener(new TextWatcher() {

      @Override
      public void onTextChanged(CharSequence s, int start, int before,
                                int count) {
        if (s.toString().contains(".")) {
          if (s.length() - 1 - s.toString().indexOf(".") > 2) {
            s = s.toString().subSequence(0,
                    s.toString().indexOf(".") + 3);
            editText.setText(s);
            editText.setSelection(s.length());
          }
        }
        if (s.toString().trim().substring(0).equals(".")) {
          s = "0" + s;
          editText.setText(s);
          editText.setSelection(2);
        }

        if (s.toString().startsWith("0")
                && s.toString().trim().length() > 1) {
          if (!s.toString().substring(1, 2).equals(".")) {
            editText.setText(s.subSequence(0, 1));
            editText.setSelection(1);
            return;
          }
        }
      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count,
                                    int after) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

      }
    });
  }

  public static long getDistanceDays(long beforeDate, long afterDate) {
    return (afterDate - beforeDate) / (1000 * 60 * 60 * 24);
  }

  /**
   * 去除字符串的换行符
   */
  public static String removeN(String str) {
    return str.replaceAll("\n", "");
  }

  public static String forUTF(String txt) {
    String result = "";
    try {
      result = URLDecoder.decode(txt, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return result;
  }

  public static String toUTF(String txt) {
    String result = "";
    try {
      result = URLEncoder.encode(txt, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return result;
  }

  /***
   * 判断 String 是否是 int
   *
   * @param input
   * @return
   */
  public static boolean isInteger(String input) {
    Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(input);
    return mer.find();
  }

  /**
   * 获取当前的时间
   *
   * @return 格式化之后的时间字符串
   */
  public static String getCurrentTime() {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//得到当前的时间
    Date curDate = new Date(System.currentTimeMillis());
    return formatter.format(curDate);
  }

  /**
   * 判断时间字符串是否是今天
   *
   * @throws ParseException
   */
  public static boolean isToday(String day) {
    try {
      Calendar pre = Calendar.getInstance();
      Date predate = new Date(System.currentTimeMillis());
      pre.setTime(predate);

      Calendar cal = Calendar.getInstance();
      Date date = getDateFormat().parse(day);
      cal.setTime(date);

      if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
        int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                - pre.get(Calendar.DAY_OF_YEAR);

        if (diffDay == 0) {
          return true;
        }
      }
    } catch (ParseException e) {
      e.printStackTrace();
      return false;
    }
    return false;
  }

  /**
   * 获取时间格式
   */
  public static SimpleDateFormat getDateFormat() {
    if (null == DateLocal.get()) {
      DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
    }
    return DateLocal.get();
  }

  /**
   * 判断时间字符串是否是今天
   *
   * @throws ParseException
   */
  public static int getDaysFromToday(long timeStamp) {
    int days = -1;
    Calendar pre = Calendar.getInstance();
    Date predate = new Date(System.currentTimeMillis());
    pre.setTime(predate);

    Calendar cal = Calendar.getInstance();
    Date date = new Date(timeStamp);
    cal.setTime(date);

    if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
      days = pre.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR);
    }

    return days;
  }

  /**
   * 转化消息中的时间
   *
   * @param date 时间戳
   * @param isMessageCategory 是否是消息分类列表
   */
  public static String transferMessageDate(long date, boolean isMessageCategory) {
    if (date > 0) {
      long day = getDaysFromToday(date);
      if (day == 0) {
        /**小于一天  显示几时几分*/
        return transferLongToHourMinute(date);
      } else if (day > 0 && day < 3) {
        /**小于三天  显示前天，昨天*/
        return (day == 1) ? "昨天" : "前天";
      } else {
        /**大于三天  显示年-月-日*/
        return (isMessageCategory) ? transferLongToYearMonth(date)
                : transferLongToYearMonthDay(date);
      }
    }
    return "";
  }

  /**
   * 判断int数值的位数
   */
  public static int getIntFigures(int count) {
    for (int i = 0; ; i++)
      if (count <= sizeTable[i]) {
        return i + 1;
      }
  }

  /**
   * 根据数字的位数返回对应的字体大小
   *
   * @param count 数字
   * @return 字体大小
   */
  public static int getTextSizeByIntFigures(int count) {
    int size;
    int leng = getIntFigures(count);
    switch (leng) {
      case 1:
        size = 9;
        break;
      case 2:
        size = 7;
        break;
      case 3:
        size = 5;
        break;
      default:
        size = 4;
        break;
    }
    return size;
  }

  /**
   * 通过评分的值获取对应的等级
   *
   * @param stars 评分值
   * @return 对应的等级名称
   */
  public static String getStartTextByValue(int stars) {
    String data = "";
    switch (stars) {
      case 1:
        data = "差";
        break;
      case 2:
        data = "一般";
        break;
      case 3:
        data = "好";
        break;
      case 4:
        data = "很好";
        break;
      case 5:
        data = "非常好";
        break;
      default:
        data = "";
        break;
    }
    return data;
  }


}
