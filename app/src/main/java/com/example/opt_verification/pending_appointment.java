package com.example.opt_verification;

import java.util.Date;

public class pending_appointment {

    String id , cid , cname , work ;
    Date d;

    public pending_appointment() {
    }

    public pending_appointment(String id , String cid , String cname , String w , Date d) {
        this.id = id ;
        this.cid = cid ;
        this.cname = cname ;
        this.work = w ;
        this.d = d;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public Date getD() {
        return d;
    }

    public void setD(Date d) {
        this.d = d;
    }
}
