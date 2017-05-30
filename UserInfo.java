/*
 * UserInfo.java程序文件保存用户信息
*/

package server.my.user;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

class FriendInfo implements Serializable{			//保存用户好友信息
	private static final long serialVersionUID = 1L;
	private String account;					//好友账号
	private String name;					//好友昵称

	public FriendInfo(String account,String name){
		this.account = account;
		this.name = name;
	}

	public String getAccount(){
		return account;
	}
	public String getName(){
		return name;
	}

}

class GroupInfo implements Serializable{			//用户所在群的信息
	private static final long serialVersionUID = 1L;
	private String account;					//群帐号
	private String name;					//群昵称
	private ArrayList<String> gl;

	public GroupInfo(String account,String name){
		this.account = account;
		this.name = name;
	}

	public String getAccount(){
		return this.account;
	}

	public String getName(){
		return this.name;
	}
	public void setGroupMember(ArrayList<String> gl){
		this.gl = gl;
	}
	public ArrayList<String> getGroupMember(){
		return gl;
	}
}

public class UserInfo implements Serializable{			//用户信息
	private static final long serialVersionUID = 1L;
	private Map<String,FriendInfo> friend ;			//用户所有好友列表
	private Map<String,String> searchFriendAccount;		//用户好友账户列表
	private Map<String,GroupInfo> group;			//用户群列表
	private Map<String,String> searchGroupAccount;		//群帐号列表
	private String password;				//用户密码
	private String ipAddress;				//用户客户端登陆时机器所在网络的ip地址
	private String account;					//用户帐号
	private String name;					//用户昵称
	private boolean isOnline;				//用户是否在线
	private int friendNum = 0;
	private int groupNum = 0;
	
	public UserInfo(String account,String name,String password){
		this.account = account;
		isOnline = false;
		this.name = name;
		this.password = password;
		searchFriendAccount = new HashMap<String,String>();
		searchGroupAccount = new HashMap<String,String>();
		friend = new HashMap<String,FriendInfo>();
		group = new HashMap<String,GroupInfo>();
	}
	public synchronized boolean getIsOnline(){
		return isOnline;
	}
	public synchronized void setIsOnline(){
		if(!this.isOnline)
			this.isOnline = true;
		else 
			this.isOnline = false;
	}
	public synchronized void offLine(){
		if(this.isOnline)
			this.isOnline = false;
	}
	public synchronized void setGroupMember(String groupAccount,ArrayList<String> gl){
		group.get(groupAccount).setGroupMember(gl);
	}

	public synchronized ArrayList<String> getGroupMember(String groupAccount){
		return group.get(groupAccount).getGroupMember();
	}
	public synchronized void setAccount(String account){
		this.account = account;
	}
	public synchronized String getAccount(){
		return account;
	}
	public synchronized String getName(){
		return name;
	}
	public synchronized void setName(String name){
		this.name = name;
	}

	public synchronized String getPassword(){
		return password;
	}
	public synchronized void setPassword(String password){
		this.password = password;
	}

	public synchronized int getFriendNum(){
		return friendNum;
	}

	public synchronized int getGroupNum(){
		return groupNum;
	}
	public synchronized void addFriend(String account,String name){
		FriendInfo fi = new FriendInfo(account,name);
		searchFriendAccount.put(name,account);
		friend.put(account,fi);
		this.friendNum++;
	}

	public synchronized boolean removeFriend(String account){
		if(!friend.containsKey(account))
			return false;
		friend.remove(account);
		this.friendNum--;
		return true;
	}

	public synchronized void addGroup(String account,String name){
		GroupInfo gi = new GroupInfo(account,name);
		searchGroupAccount.put(name,account);
		group.put(account,gi);
		this.groupNum++;
	}
	public synchronized boolean removeGroup(String account){
		if(!group.containsKey(account))
			return false;
		group.remove(account);
		this.groupNum--;
		return true;
	}

	public synchronized boolean containGroup(String account){
		return group.containsKey(account);
	}

	public synchronized boolean containFriend(String account){
		return friend.containsKey(account);
	}

	public synchronized String getFriendInfo(String account){
		return friend.get(account).getName();
	}

	public synchronized String getGroupInfo(String account){
		return group.get(account).getName();
	}

	public synchronized void setIpAddress(String ipAddress){
		this.ipAddress = ipAddress;
	}

	public synchronized String getIpAddress(){
		return this.ipAddress;
	}
	public synchronized String getFriendAccount(String name){
		return searchFriendAccount.get(name);
	}
	public synchronized String getGroupAccount(String name){
		return searchGroupAccount.get(name);
	}
	public synchronized ArrayList<String> getAllFriendName(){
		ArrayList<String> al = new ArrayList<String>();

		Set<String> keys = friend.keySet();
		Iterator<String> iter = keys.iterator();
		while(iter.hasNext()){
			String account = iter.next();
			al.add(this.getFriendInfo(account));
		}
		return al;
	}
	public synchronized ArrayList<String> getAllGroupName(){
		ArrayList<String> al = new ArrayList<String>();

		Set<String> keys = group.keySet();
		Iterator<String> iter = keys.iterator();
		while(iter.hasNext()){
			String account = iter.next();
			System.out.println(account);
			al.add(this.getGroupInfo(account));
		}
		return al;
	}
	public synchronized int getSize(){
		return friend.size();
	}
}
