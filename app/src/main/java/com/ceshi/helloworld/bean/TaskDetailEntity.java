package com.ceshi.helloworld.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kekex on 2018/10/9.
 */

public class TaskDetailEntity implements Serializable {

    /**
     * code : 100
     * taskinfo : {"onepercent":0,"submit":"","taskname":"测试发布任务3","status2text":"","totalmoney":90,"openurl":"15585355264841.jpg","number":10,"u_id":71,"top":1,"c_id":1,"paypercent":0,"text":"关注公众号Firestorms","info":[{"step_text":"测试测试1","step_pic":"15585355264841.jpg"},{"step_text":"测试测试2","step_pic":"15585355264841.jpg"}],"finishnumber":0,"image":"","createtime":1558964571,"lastnumber":10,"terminal":"","status2":1,"picture":"","paynum":"","t_id":878,"money":9,"paymoney":100,"twopercent":0,"money2":10,"user":"","opentype":1,"tasknum":"15589645718453","status":1,"toptime":1558964571}
     * rinfo : 查询成功
     * o_id : 0
     * taskstep : [{"t_id":878,"createtime":1558964571,"tp_id":1,"text":"测试测试1","picture":"15585355264841.jpg","status":1},{"t_id":878,"createtime":1558964571,"tp_id":2,"text":"测试测试2","picture":"15585355264841.jpg","status":1}]
     * orderinfo : 任务未领取
     * ordertype : 0
     */

    private String code;
    private TaskinfoBean taskinfo;
    private String rinfo;
    private String o_id;
    private String orderinfo;
    private String ordertype;
    private List<TaskstepBean> taskstep;
    private String tasktitle;
    private String tasktext;

    @Override
    public String toString() {
        return "TaskDetailEntity{" +
                "code='" + code + '\'' +
                ", taskinfo=" + taskinfo +
                ", rinfo='" + rinfo + '\'' +
                ", o_id='" + o_id + '\'' +
                ", orderinfo='" + orderinfo + '\'' +
                ", ordertype='" + ordertype + '\'' +
                ", taskstep=" + taskstep +
                ", tasktitle='" + tasktitle + '\'' +
                ", tasktext='" + tasktext + '\'' +
                '}';
    }

    public String getTasktitle() {
        return tasktitle;
    }

    public void setTasktitle(String tasktitle) {
        this.tasktitle = tasktitle;
    }

    public String getTasktext() {
        return tasktext;
    }

    public void setTasktext(String tasktext) {
        this.tasktext = tasktext;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public TaskinfoBean getTaskinfo() {
        return taskinfo;
    }

    public void setTaskinfo(TaskinfoBean taskinfo) {
        this.taskinfo = taskinfo;
    }

    public String getRinfo() {
        return rinfo;
    }

    public void setRinfo(String rinfo) {
        this.rinfo = rinfo;
    }

    public String getO_id() {
        return o_id;
    }

    public void setO_id(String o_id) {
        this.o_id = o_id;
    }

    public String getOrderinfo() {
        return orderinfo;
    }

    public void setOrderinfo(String orderinfo) {
        this.orderinfo = orderinfo;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public List<TaskstepBean> getTaskstep() {
        return taskstep;
    }

    public void setTaskstep(List<TaskstepBean> taskstep) {
        this.taskstep = taskstep;
    }

    public static class TaskinfoBean {
        @Override
        public String toString() {
            return "TaskinfoBean{" +
                    "onepercent=" + onepercent +
                    ", submit='" + submit + '\'' +
                    ", taskname='" + taskname + '\'' +
                    ", status2text='" + status2text + '\'' +
                    ", totalmoney=" + totalmoney +
                    ", openurl='" + openurl + '\'' +
                    ", number=" + number +
                    ", u_id=" + u_id +
                    ", top=" + top +
                    ", c_id=" + c_id +
                    '}';
        }

        /**
         * onepercent : 0
         * submit :
         * taskname : 测试发布任务3
         * status2text :
         * totalmoney : 90
         * openurl : 15585355264841.jpg
         * number : 10
         * u_id : 71
         * top : 1
         * c_id : 1
         * paypercent : 0
         * text : 关注公众号Firestorms
         * info : [{"step_text":"测试测试1","step_pic":"15585355264841.jpg"},{"step_text":"测试测试2","step_pic":"15585355264841.jpg"}]
         * finishnumber : 0
         * image :
         * createtime : 1558964571
         * lastnumber : 10
         * terminal :
         * status2 : 1
         * picture :
         * paynum :
         * t_id : 878
         * money : 9
         * paymoney : 100
         * twopercent : 0
         * money2 : 10
         * user :
         * opentype : 1
         * tasknum : 15589645718453
         * status : 1
         * toptime : 1558964571
         */



        private int onepercent;
        private String submit;
        private String taskname;
        private String status2text;
        private int totalmoney;
        private String openurl;
        private int number;
        private int u_id;
        private int top;
        private int c_id;
        private int paypercent;
        private String text;
        private int finishnumber;
        private String image;
        private int createtime;
        private int lastnumber;
        private String terminal;
        private int status2;
        private String picture;
        private String paynum;
        private int t_id;
        private int money;
        private int paymoney;
        private int twopercent;
        private int money2;
        private String user;
        private int opentype;
        private String tasknum;
        private int status;
        private int toptime;
        private Object info;

        public int getOnepercent() {
            return onepercent;
        }

        public void setOnepercent(int onepercent) {
            this.onepercent = onepercent;
        }

        public String getSubmit() {
            return submit;
        }

        public void setSubmit(String submit) {
            this.submit = submit;
        }

        public String getTaskname() {
            return taskname;
        }

        public void setTaskname(String taskname) {
            this.taskname = taskname;
        }

        public String getStatus2text() {
            return status2text;
        }

        public void setStatus2text(String status2text) {
            this.status2text = status2text;
        }

        public int getTotalmoney() {
            return totalmoney;
        }

        public void setTotalmoney(int totalmoney) {
            this.totalmoney = totalmoney;
        }

        public String getOpenurl() {
            return openurl;
        }

        public void setOpenurl(String openurl) {
            this.openurl = openurl;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getU_id() {
            return u_id;
        }

        public void setU_id(int u_id) {
            this.u_id = u_id;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getC_id() {
            return c_id;
        }

        public void setC_id(int c_id) {
            this.c_id = c_id;
        }

        public int getPaypercent() {
            return paypercent;
        }

        public void setPaypercent(int paypercent) {
            this.paypercent = paypercent;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getFinishnumber() {
            return finishnumber;
        }

        public void setFinishnumber(int finishnumber) {
            this.finishnumber = finishnumber;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getCreatetime() {
            return createtime;
        }

        public void setCreatetime(int createtime) {
            this.createtime = createtime;
        }

        public int getLastnumber() {
            return lastnumber;
        }

        public void setLastnumber(int lastnumber) {
            this.lastnumber = lastnumber;
        }

        public String getTerminal() {
            return terminal;
        }

        public void setTerminal(String terminal) {
            this.terminal = terminal;
        }

        public int getStatus2() {
            return status2;
        }

        public void setStatus2(int status2) {
            this.status2 = status2;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getPaynum() {
            return paynum;
        }

        public void setPaynum(String paynum) {
            this.paynum = paynum;
        }

        public int getT_id() {
            return t_id;
        }

        public void setT_id(int t_id) {
            this.t_id = t_id;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public int getPaymoney() {
            return paymoney;
        }

        public void setPaymoney(int paymoney) {
            this.paymoney = paymoney;
        }

        public int getTwopercent() {
            return twopercent;
        }

        public void setTwopercent(int twopercent) {
            this.twopercent = twopercent;
        }

        public int getMoney2() {
            return money2;
        }

        public void setMoney2(int money2) {
            this.money2 = money2;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public int getOpentype() {
            return opentype;
        }

        public void setOpentype(int opentype) {
            this.opentype = opentype;
        }

        public String getTasknum() {
            return tasknum;
        }

        public void setTasknum(String tasknum) {
            this.tasknum = tasknum;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getToptime() {
            return toptime;
        }

        public void setToptime(int toptime) {
            this.toptime = toptime;
        }

        public Object getInfo() {
            return info;
        }

        public void setInfo(Object info) {
            this.info = info;
        }

    }

    public static class TaskstepBean {
        /**
         * t_id : 878
         * createtime : 1558964571
         * tp_id : 1
         * text : 测试测试1
         * picture : 15585355264841.jpg
         * status : 1
         */

        private int t_id;
        private int createtime;
        private int tp_id;
        private String text;
        private String picture;
        private int status;
        private String flag;
        private String tj_url;

        @Override
        public String toString() {
            return "TaskstepBean{" +
                    "t_id=" + t_id +
                    ", createtime=" + createtime +
                    ", tp_id=" + tp_id +
                    ", text='" + text + '\'' +
                    ", picture='" + picture + '\'' +
                    ", status=" + status +
                    ", flag='" + flag + '\'' +
                    ", tj_url='" + tj_url + '\'' +
                    '}';
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getTj_url() {
            return tj_url;
        }

        public void setTj_url(String tj_url) {
            this.tj_url = tj_url;
        }

        public int getT_id() {
            return t_id;
        }

        public void setT_id(int t_id) {
            this.t_id = t_id;
        }

        public int getCreatetime() {
            return createtime;
        }

        public void setCreatetime(int createtime) {
            this.createtime = createtime;
        }

        public int getTp_id() {
            return tp_id;
        }

        public void setTp_id(int tp_id) {
            this.tp_id = tp_id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
