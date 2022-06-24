package com.example.graduationproject;

public class IP {
    private String ip ;

//    public IP() {
//        this.ip = "172.20.10.2";
//    }
public IP() {
    this.ip = "172.27.224.1";
}
    public IP(String ip) {
        this.ip = ip;
    }


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
