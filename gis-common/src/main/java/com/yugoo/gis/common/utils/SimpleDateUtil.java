package com.yugoo.gis.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nihao on 16/10/10.
 */
public class SimpleDateUtil {
    private static final String dateFormatString="yyyy-MM-dd HH:mm:ss";
    private static final String dateFormatShortString="yyyy-MM-dd";
    private static final String dateFormatCNString="yyyy年MM月dd日";
    private static ThreadLocal<DateFormat> threadLocal=new ThreadLocal<>();
    private static ThreadLocal<DateFormat> threadLocalShort=new ThreadLocal<>();
    private static ThreadLocal<DateFormat> threadLocalCN=new ThreadLocal<>();

    public static DateFormat getDateFormat(){
        DateFormat df=threadLocal.get();
        if(df==null){
            df=new SimpleDateFormat(dateFormatString);
            threadLocal.set(df);
        }
        return df;
    }
    public static DateFormat getShortDateFormat(){
        DateFormat df=threadLocalShort.get();
        if(df==null){
            df=new SimpleDateFormat(dateFormatShortString);
            threadLocalShort.set(df);
        }
        return df;
    }
    public static DateFormat getCNDateFormat(){
        DateFormat df=threadLocalCN.get();
        if(df==null){
            df=new SimpleDateFormat(dateFormatCNString);
            threadLocalCN.set(df);
        }
        return df;
    }

    /**
     * Date转String
     * @param date
     * @return
     */
    public static String format(Date date){
        return getDateFormat().format(date);
    }

    /**
     * String转Date
     * @param strDate
     * @return
     */
    public static Date parse(String strDate){
        try {
            return getDateFormat().parse(strDate);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Date转String
     * @param date
     * @return
     */
    public static String shortFormat(Date date){
        return getShortDateFormat().format(date);
    }

    /**
     * String转Date
     * @param strDate
     * @return
     */
    public static Date shortParse(String strDate){
        try {
            return getShortDateFormat().parse(strDate);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String cnFormat(Date date){
        return getCNDateFormat().format(date);
    }
}
