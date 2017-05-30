package server.my.message;
import server.my.user.UserInfo;
import java.util.ArrayList;
import java.io.*;

public class Message implements Serializable{
	private static final long serialVersionUID = 1L;
	public Message(){
		this.acknowledge = false;
		this.isSend = false;
	}
	public String sendAccount;
	public String receiveAccount;
	public String name;
	public String password;
	public String groupName;
	public String groupAccount;
	public String ipAddress;
	public boolean acknowledge;
	public boolean isSend;
	public int type;
	public UserInfo ui;
	public String data;
	public ArrayList<String> al;
}
/*
1.登录
2.注销
3.注册
4.修改名称
5.请求加群
5.允许加群
6.删群
7.请求加好友
8.允许加好友
8.删好友
9.创建群
10.群删除成员
11.发送好友消息
12.发群消息
13.信息修改确认
*/
