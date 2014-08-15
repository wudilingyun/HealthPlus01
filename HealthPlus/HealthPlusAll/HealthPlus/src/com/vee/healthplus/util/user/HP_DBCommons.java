package com.vee.healthplus.util.user;

/**
 * Created by wangjiafeng on 13-11-1.
 */
public class HP_DBCommons {

    //数据库名字
    public static  final String DBNAME="myuser.db";
    public static  final String DBPWD="";
    //表名
    public static final String USERINFO_TABLENAME = "USER_HP";
    public static final String USERWEIGHT_TABLENAME = "USERWEIGHT_HP";
    public static final String USERCOLLECT_TABLENAME ="USERCOLLECT_NEWS";//收藏宝典内容
    public static final String USERTEST_TABLENAME ="USERTEST";//测试列表 ---可重复的
    public static final String USERTEST_TABLENAME_COVER ="USERTEST_COVER";//测试列表 ---可重复的
    public static final String JPUSH_TABLENAME ="JPUSH";//激光推送
    public static final String NEWFRIEND_TABLENAME = "NEWFRIEND";//新康友
    
    //字段名
    public static final String USERID = "USER_ID";
    public static final String USERNAME = "USER_NAME";
    public static final String USERNICK = "USER_NICK";
    public static final String USERAGE = "USER_AGE";
    public static final String USERHEIGHT = "USER_HEIGHT";
    public static final String USERWEIGHT = "USER_WEIGHT";
    public static final String USERSEX = "USER_SEX";
    public static final String EMAIL = "EMAIL";
    public static final String PHONE = "PHONE";
    public static final String REMARK = "REMARK";
    public static final String UPDATETIME = "UPDATE_TIME";
    public static final String PHOTO = "PHOTO";
    
    /* 宝典字段
     *   user_id  索引
     */
   
    public static final String TITLE = "TITLE";
    public static final String IMGURL ="IMGURL";
    public static final String WEBURL = "WEBURL";
    
    /*
     * 测试列表字段
     */
    public static final String TESTNAME = "TESTNAME";
    public static final String TESTRESULT ="TESTRESULT";
    public static final String TESTTIME = "TESTTIME";
    
    /*
     * 极光推送
     */
    public static final String JPUSHTITLE = "JPUSHTITLE";
    public static final String JPUSHCONTENT ="JPUSHCONTENT";
    public static final String JPUSHIMG ="JPUSHIMG";
    public static final String JPUSHTIME = "JPUSHTIME";
    public static final String JPUSHREADFLAG = "JPUSHREADFLAG";
    
}
