package com.example.graduationproject;

public class IP {
    private String ip ;

//    public IP() {
//        this.ip = "192.168.1.100";
//    }
public IP() {
    this.ip = "10.0.2.2";
}
//    public IP(String ip) {
//        this.ip = ip;
//    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "IP{" +
                "ip='" + ip + '\'' +
                '}';
    }
}
