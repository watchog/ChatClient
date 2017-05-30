package client.my.gui;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Set;
import java.util.Iterator;
import java.lang.Thread;
import java.lang.Runnable;
import java.util.ArrayList;
import java.util.HashMap;
import server.my.user.UserInfo;
import server.my.message.Message;
import client.my.messagelinkedlist.MessageLinkedList;
import client.my.guimanager.GUIManager;

public class ChatGUI implements Runnable{
	private MessageLinkedList mll;
	private Message me;
	private UserInfo ui;
	private GUIManager gr;
	private ArrayList<String> al;
	public ChatGUI(MessageLinkedList mll){
		this.mll = mll;
		gr = new GUIManager();
	}
	public void run(){
		LogIn li = new LogIn(mll);
		while(true){
			if(!mll.isEmptyi()){
				me = mll.popi();
				if(me.type == 1){
					if(me.ui == null){
						li.setText("登录失败");
						continue;
					}
					this.ui = me.ui;
					break;//显示帐号
				}
			}
		}
		li.setUnvisible();
		ArrayList<String> friendName = ui.getAllFriendName();
		ArrayList<String> groupName = ui.getAllGroupName();
		MainMenu mm = new MainMenu(friendName,groupName,gr,ui,mll);
		while(true){
			if(mll.isEmptyi())
				continue;
			me = mll.popi();
			switch(me.type){
				case 4:
					mm.addMessage(me.data);
					mm.addMessage(me);
					mll.pushw(me);
					break;
				case 5:
					this.al = me.al;
					this.ui = me.ui;
					mm.addMessage(me.data);
					groupName = ui.getAllGroupName();
					mm.flushGroup(groupName);
					break;
				case 6:
					mm.addMessage(me.data);
					this.ui = me.ui;
					groupName = ui.getAllGroupName();
					mm.flushGroup(groupName);
					break;
				case 8:
					mm.addMessage(me.data);
					if(me.ui == null)		  //拒绝
						break;
					this.ui = me.ui;
					friendName = ui.getAllFriendName();
					mm.flushFriend(friendName);
					break;
				case 7:
					mm.addMessage(me.data);
					mll.pushw(me);
					break;
				case 9:
					mm.addMessage(me.data);
					this.ui = me.ui;
					friendName = ui.getAllFriendName();
					mm.flushFriend(friendName);
					break;
				case 10:
					mm.addMessage(me.data);
					this.ui = me.ui;
					groupName = ui.getAllGroupName();
					mm.flushGroup(groupName);
					break;
				case 11:
					mm.addMessage(me.data);
					this.ui = ui;
					break;
				case 12:
					mm.addMessage(me.name+": 发来消息");
					String friendKey = "f"+" "+ui.getAccount()+" "+me.sendAccount;
					if(gr.containsKey(friendKey)){
						Chat cht = (Chat)gr.get(friendKey);
						cht.setText(me.data);
					}else{
						mll.pushw(me);
					}
					break;
				case 13:
					if(me.sendAccount.equals(ui.getAccount()))
						break;
					mm.addMessage(me.groupName+": 发来消息");
					String groupKey = "g"+" "+ui.getAccount()+" "+me.groupAccount;
					System.out.println("13"+groupKey);
					if(gr.containsKey(groupKey)){
						Chat cht = (Chat)gr.get(groupKey);
						cht.setText(me.data);
					}else{
						mll.pushw(me);
					}
					break;

			}
			
		}
	}
}

class Comfirm{
	private static final int DEFAULT_WIDTH = 300;
	private static final int DEFAULT_HEIGHT = 200;
	private Message me;
	private UserInfo ui;
	private GUIManager gr;
	private JFrame frame = new JFrame();
	private MessageLinkedList mll;
	private JButton agree = new JButton("同意");
	private JButton refuse = new JButton("拒绝");
	private JTextField field = new JTextField();

	public Comfirm(MessageLinkedList mll,UserInfo ui,GUIManager gr){
		this.mll = mll;
		this.me = mll.popw();
		this.ui = ui;
		this.gr = gr;
		frame.add(agree);
		frame.add(refuse);
		frame.setLayout(null);
		frame.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);

		agree.addActionListener(new Agree());
		refuse.addActionListener(new Refuse());

		agree.setBounds(40,140,60,33);
		refuse.setBounds(190,140,60,33);
		field.setBounds(30,90,200,30);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private class Agree implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Message met = new Message();
			if(me.type == 12 || me.type == 13){
				mll.pushw(me);
				return;
			}
			if(me.type == 4){
				met.type = 5;
				met.sendAccount = ui.getAccount();
				met.groupAccount = me.groupAccount;
				met.receiveAccount = me.sendAccount;
				met.acknowledge = true;
				mll.pusho(met);
				field.setText(me.data);
				frame.setVisible(false);
				return ;
			}else if(me.type == 7){
				met.type = 8;
				met.name = ui.getName();
				met.sendAccount = ui.getAccount();
				met.receiveAccount = me.sendAccount;
				met.acknowledge = true;
				mll.pusho(met);
				field.setText(me.data);
				gr.size();
				frame.setVisible(false);
				return ;
			}
			
		}
	}
	private class Refuse implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Message met = new Message();
			if(me.type == 4){
				met.type = 5;
				met.acknowledge = false;
			}else if(me.type == 7){
				met.type = 8;
				met.acknowledge = false;
			}else
				return;
			mll.pusho(met);
			frame.setVisible(false);
		}
	}

}
class LogIn{
	private static final int DEFAULT_WIDTH = 300;
	private static final int DEFAULT_HEIGHT = 200;
	private JButton set;
	private JLabel label;
	private JLabel label1;
	private JButton send;
	private JButton sign;
	private Message me;
	private MessageLinkedList mll;
	private JPasswordField jf;
	private JTextField field;
	private JFrame frame;

	public LogIn(MessageLinkedList mll){
		frame = new JFrame();
		this.mll = mll;
		set = new JButton("设置");
		label = new JLabel("帐号");
		label1 = new JLabel("密码");
		send = new JButton("登录");
		sign = new JButton("注册");
		jf = new JPasswordField("",12);
		field = new JTextField("",20);
		me = new Message();

		frame.setTitle("登录");
		frame.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
		frame.setLayout(null);

		frame.add(set);
		set.setBounds(10,10,60,33);
		Setting setting = new Setting();
		set.addActionListener(setting);

		frame.add(label);
		label.setBounds(10,60,60,40);

		frame.add(field);
		field.setBounds(50,60,200,30);

		frame.add(label1);
		label1.setBounds(10,100,60,40);

		jf.setEchoChar('*');
		frame.add(jf);
		jf.setBounds(50,100,200,30);
		                             
		frame.add(send);
		send.setBounds(10,140,60,33);
		Send sendAC = new Send();
		send.addActionListener(sendAC);

		frame.add(sign);
		SignCp signAC = new SignCp();
		sign.setBounds(90,140,60,33);
		sign.addActionListener(signAC);
		
		frame.addWindowListener(new Closing());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
	public void setText(String account){
		field.setText(account);
	}
	public void setUnvisible(){
		frame.setVisible(false);
	}
	private class Send implements ActionListener{

		public void actionPerformed(ActionEvent e){
			me.sendAccount = field.getText();
			char[] password = jf.getPassword();
			me.password = new String(password);
			System.out.println(me.sendAccount);
			if(me.sendAccount == null){
				field.setText("帐号不能为空");
				return ;
			}else if(me.password == null){
				jf.setText("密码不能为空");
				return ;
			}
			me.type = 1;
			mll.pusho(me);
		}
	}
	private class Closing extends WindowAdapter{
		public void windowClosing(WindowEvent e){
			System.exit(0);
		}
	}
	private class SignCp implements ActionListener{
		public void actionPerformed(ActionEvent e){
			SignIn  s = new SignIn(mll);
		}
	}
	private class Setting implements ActionListener{
		public void actionPerformed(ActionEvent e){
			IpAddress ip = new IpAddress();
		}
	}
}

class SignIn{
	private static final int DEFAULT_WIDTH = 300;
	private static final int DEFAULT_HEIGHT = 300;
	private JLabel label;                           
	private JTextField field;
	private JPasswordField jf;
	private JPasswordField jf2;
	private MessageLinkedList mll;
	private JLabel label1;
	private JLabel label2;
	private JButton sigin;
	private JFrame frame;
	private Message me;

	public SignIn(MessageLinkedList mll){
		this.mll = mll;
		frame = new JFrame();
		me = new Message();
		frame.setTitle("注册");
		frame.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
		frame.setLayout(null);

	        label = new JLabel("用户名");                              
		frame.add(label);
		label.setBounds(10,60,60,40);

		field = new JTextField("",20);
		frame.add(field);
		field.setBounds(50,60,200,30);

		label1 = new JLabel("密码");
		frame.add(label1);
		label1.setBounds(10,100,60,40);
		jf = new JPasswordField("",12);
		jf.setEchoChar('*');
		frame.add(jf);
		jf.setBounds(50,100,200,30);

		label2 = new JLabel("密码");
		frame.add(label2);
		label2.setBounds(10,140,60,40);
		jf2 = new JPasswordField("",12);
		jf2.setEchoChar('*');
		frame.add(jf2);
		jf2.setBounds(50,140,200,30);

		sigin = new JButton("注册");
		frame.add(sigin);
		sigin.setBounds(90,200,60,33);
		SignInCp sic = new SignInCp();
		sigin.addActionListener(sic);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
	private class SignInCp implements ActionListener{
		public void actionPerformed(ActionEvent e){
			me.type = 2;
			char[] p1 = jf.getPassword();
			char[] p2 = jf2.getPassword();
			String s1 = new String(p1);
			String s2 = new String(p2);
			if(s1.equals(s2)){
				me.name = field.getText();
				if(me.name == null){
					field.setText("名称不能为空");
					return ;
				}
				if(s1 == null){
					field.setText("密码不能为空");
					return;
				}
				me.password = s1;
				mll.pusho(me);
				frame.setVisible(false);
			}else{
				field.setText("密码错误");
			}
		}
	}
}

class Chat{
	private static final int DEFAULT_WIDTH = 450;
	private static final int DEFAULT_HEIGHT = 330;
	private JTextArea field = new JTextArea(10,60);
	private JTextArea field1 = new JTextArea(7,60);
	private MessageLinkedList mll; 
	private JButton sigin = new JButton("发送");
	private String key;
	private UserInfo ui;
	private Message me;
	private GUIManager gr;
	private JFrame frame;

	public Chat(MessageLinkedList mll,UserInfo ui,GUIManager gr){
		this.mll = mll;
		this.ui = ui;
		this.gr = gr;
		frame = new JFrame();
		me = new Message();
		frame.setTitle("");
		frame.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
		frame.setLayout(null);

		field = new JTextArea(10,60);
		field.setEditable(false);
		JScrollPane scrollPane1 = new JScrollPane(field);      
		scrollPane1.setBounds(10,10,430,150);
		frame.add(scrollPane1);

		field1 = new JTextArea(7,60);
		JScrollPane scrollPane = new JScrollPane(field1);      
		scrollPane.setBounds(10,170,430,100);
		frame.add(scrollPane);

		sigin = new JButton("发送");
		frame.add(sigin);
		sigin.setBounds(170,280,60,33);
		ChatCp cc = new ChatCp();
		sigin.addActionListener(cc);
		
		WindowListener listener = new ChatClosing();
		frame.addWindowListener(listener);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);

	}
	public void setText(String s){
		field.append(s+"\n");
	}
	public void setKey(String key){
		this.key = key;
	}
	public void setVisible(){
		frame.setVisible(true);
	}
	private class ChatClosing extends WindowAdapter{
		public void windowClosing(WindowEvent e){
			gr.remove(key);
		}
	}
	private class ChatCp implements ActionListener{
		public void actionPerformed(ActionEvent e){
			me.data = ui.getName()+": "+field1.getText();
			field.append(me.data+"\n");
			String[] account = key.split(" ");
			if(account[0].equals("g")){
				me.sendAccount = account[1];
				me.groupAccount = account[2];
				me.name = ui.getName();
				me.groupName = ui.getGroupInfo(account[2]);
				me.type = 13;
			}else{
				me.sendAccount = account[1];
				me.name = ui.getName();
				me.receiveAccount = account[2];
				me.type = 12;
			}
			field1.setText("");
			mll.pusho(me);
		}
	}
		
}

class MainMenu{
	private static final int DEFAULT_WIDTH = 170;
	private static final int DEFAULT_HEIGHT = 370;
	private GUIManager gr;
	private Message me;
	private UserInfo ui;
	private MessageLinkedList mll;
	private HashMap<String,JMenuItem> friend = new HashMap<String,JMenuItem>();
	private HashMap<String,JMenuItem> group = new HashMap<String,JMenuItem>();
	private JFrame frame = new JFrame();
	private JMenuBar menuBar = new JMenuBar();
	private JMenu friendMenu = new JMenu("朋友");
	private JMenu groupMenu = new JMenu("群");
	private JMenu otherMenu = new JMenu("其它");
	private JButton button = new JButton("清空");
	private JButton button1 = new JButton("信息");
	private JTextArea message = new JTextArea();
	private JMenuItem item = new JMenuItem("加好友");                       
	private JMenuItem item1 = new JMenuItem("加群");
	private JMenuItem item2 = new JMenuItem("消息处理");
	private JMenuItem item3 = new JMenuItem("创建群");	
	private JButton button2 = new JButton("消息");

	public MainMenu(ArrayList<String> f,ArrayList<String> g,GUIManager gr,UserInfo ui,MessageLinkedList mll){
		this.ui = ui;
		this.gr = gr;
		this.mll = mll;

		frame.setJMenuBar(menuBar);
		frame.setLayout(null);
		frame.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
		menuBar.add(friendMenu);
		menuBar.add(groupMenu);
		menuBar.add(otherMenu);

		otherMenu.add(item);			
		otherMenu.addSeparator();
		ActionListener listener = new OtherCp("加好友");
		item.addActionListener(listener);
		otherMenu.add(item1);			
		otherMenu.addSeparator();
		otherMenu.add(item2);
		otherMenu.addSeparator();
		otherMenu.add(item3);

		listener = new OtherCp("加群");
		item1.addActionListener(listener);
		listener = new OtherCp("消息处理");
		item2.addActionListener(listener);
		listener = new OtherCp("创建群");
		item3.addActionListener(listener);
			
		flushFriend(f);
		flushGroup(g);

		frame.add(message);
		message.setBounds(10,10,150,280);
		frame.add(button);
		button.setBounds(5,290,60,40);
		frame.add(button1);

		button1.setBounds(55,290,60,40);
		button1.addActionListener(new InfoButton());
		message.setEditable(false);
		button.addActionListener(new ClearButton());

		frame.add(button2);
		button2.addActionListener(new ChatCp());
		button2.setBounds(105,290,60,40);

		frame.addWindowListener(new LogOut());

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	public void flushFriend(ArrayList<String> s){
		if(s == null)
			return;
		Set<String> item = friend.keySet();
		Iterator<String> iter = item.iterator();
		boolean isExists = false;
		while(iter.hasNext()){
			String t = iter.next();
			for(int i = 0;i < s.size();i++){
				if(s.get(i).equals(t))
					isExists = true;
			}
			if(isExists == false){
				friendMenu.remove(friend.get(t));
				friend.remove(t);
			}else
				isExists = false;
		}
		for(int i = 0;i < s.size();i++){
			if(!friend.containsKey(s.get(i))){
				JMenuItem item2 = new JMenuItem(s.get(i));
				friendMenu.add(item2);
				friendMenu.addSeparator();
				ActionListener listener = new FriendCp(s.get(i),this);
				item2.addActionListener(listener);
				friend.put(s.get(i),item2);
			}
		}
		if(friend.containsKey(null)){
			friendMenu.remove(friend.get(null));
			friend.remove(null);
		}
	}
	public void addMessage(Message me){
		mll.pushw(me);
	}
	public void flushGroup(ArrayList<String> s){
		if(s == null)
			return ;

		Set<String> item = group.keySet();
		Iterator<String> iter = item.iterator();
		boolean isExists = false;
		while(iter.hasNext()){
			String t = iter.next();
			for(int i = 0;i < s.size();i++){
				if(s.get(i).equals(t))
					isExists = true;
			}
			if(isExists == false){
				friendMenu.remove(group.get(t));
				friend.remove(t);
			}else
				isExists = false;
		}
		for(int i = 0;i < s.size();i++){
			if(!group.containsKey(s.get(i))){
				JMenuItem item2 = new JMenuItem(s.get(i));
				groupMenu.add(item2);
				groupMenu.addSeparator();
				ActionListener listener = new GroupCp(s.get(i),this);
				item2.addActionListener(listener);
				group.put(s.get(i),item2);
			}
		}
		if(group.containsKey(null)){
			groupMenu.remove(group.get(null));
			group.remove(null);
		}
	}

	public void modifyFriend(String s, boolean b){
		if(b == true){
			JMenuItem item2 = new JMenuItem(s);
			friendMenu.add(item2);
			friendMenu.addSeparator();
			ActionListener listener = new FriendCp(s,this);
			item2.addActionListener(listener);
			friend.put(s,item2);
		}else{
			friendMenu.remove(friend.get(s));
			friend.remove(s);
		}
	}
	public void modifyGroup(String s, boolean b){
		if(b == true){
			JMenuItem item2 = new JMenuItem(s);
			groupMenu.add(item2);
			groupMenu.addSeparator();
			ActionListener listener = new GroupCp(s,this);
			item2.addActionListener(listener);
			group.put(s,item2);
		}else{
			groupMenu.remove(group.get(s));
			group.remove(s);
		}
	}

	public void addMessage(String me){
		message.append(me+"\n");
	}
	private class LogOut extends WindowAdapter{
		public void windowClosing(WindowEvent e){
			Message me = new Message();
			me.sendAccount = ui.getAccount();
			me.type = 0;
			mll.pusho(me);
			frame.setVisible(false);
			while(true){
				if(mll.isEmptyo())
					break;
			}
			System.exit(0);
		}
	}
	private class FriendCp implements ActionListener{
		private String userName;
		private MainMenu mm;
		public FriendCp(String name,MainMenu mm){
			this.userName = name;
			this.mm = mm;
		}
		public void actionPerformed(ActionEvent e){
			String key = "f"+" "+ui.getAccount()+" "+ui.getFriendAccount(userName);
			System.out.println("key--"+key);
			new Operate(key,gr,mll,ui,mm);
		}
	}
	private class ChatCp implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(mll.isEmptyw()){
				return ;
			}
			Message me = mll.popw();
			if(me.sendAccount.equals(ui.getAccount()))
				return ;
			if(me.type != 12 && me.type != 13){
				mll.pushw(me);
				return ;
			}
			if(me.type == 12){
				String friendKey = "f"+" "+ui.getAccount()+" "+me.sendAccount;
				Chat cht = new Chat(mll,ui,gr);
				cht.setText(me.data);
				cht.setKey(friendKey);
				gr.push(friendKey,cht);
			}else if(me.type == 13){
				String groupKey = "g"+" "+ui.getAccount()+" "+me.groupAccount;
				System.out.println("group: "+groupKey);
				Chat cht = new Chat(mll,ui,gr);
				cht.setText(me.data);
				cht.setKey(groupKey);
				gr.push(groupKey,cht);
			}
				
		}
	}
	private class GroupCp implements ActionListener{
		private String groupName;
		private MainMenu mm;
		public GroupCp(String name,MainMenu mm){
			this.groupName = name;
			this.mm = mm;
		}
		public void actionPerformed(ActionEvent e){
			String key = "g"+" "+ui.getAccount()+" "+ui.getGroupAccount(groupName);
			System.out.println("MainMenu "+key);
			new Operate(key,gr,mll,ui,mm);
		}
	}
	private class OtherCp implements ActionListener{
		String operate;
		public OtherCp(String operate){
			this.operate = operate;
		}
		public void actionPerformed(ActionEvent e){
			Message met = new Message();
			if(operate.equals("消息处理")){
				if(mll.isEmptyw()){
					System.out.println("消息队列为空！！！");
					return ;
				}
				new Comfirm(mll,ui,gr);
			}else if(this.operate.equals("加好友")){
				new AddFriendOrGroup(ui,true,mll);
			}else if(this.operate.equals("加群")){
				new AddFriendOrGroup(ui,false,mll);
			}else if(this.operate.equals("创建群")){
				new CreateNewGroup(ui,mll);
			}


		}
	}
	private class ClearButton implements ActionListener{
		public void actionPerformed(ActionEvent e){
			message.setText("");
		}
	}
	private class InfoButton implements ActionListener{
		public void actionPerformed(ActionEvent e){
			new MyInfo(ui);
		}
	}
}
class MyInfo{
	private static final int DEFAULT_WIDTH = 300;
	private static final int DEFAULT_HEIGHT = 200;
	private JTextField jf = new JTextField();
	private JTextField jf1 = new JTextField();
	private JFrame frame;
	private UserInfo ui;

	public MyInfo(UserInfo ui){
		this.ui = ui;
		frame = new JFrame();
		frame.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
		frame.setLayout(null);

		JButton set = new JButton("确定");
		frame.add(set);
		set.setBounds(120,120,60,33);
		Send send = new Send();
		set.addActionListener(send);

		frame.add(jf);
		jf.setBounds(50,60,200,30);
		jf.setText("昵称: "+ui.getName());
		jf.setEditable(false);
		frame.add(jf1);
		jf1.setBounds(50,20,200,30);
		jf1.setText("帐号: "+ui.getAccount());
		jf1.setEditable(false);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	private class Send implements ActionListener{
		public void actionPerformed(ActionEvent e){
			frame.setVisible(false);
		}
	}
}

class Operate{
	private static final int DEFAULT_WIDTH = 200;
	private static final int DEFAULT_HEIGHT = 100;
	private String key;
	private GUIManager gr;
	private MessageLinkedList mll;
	private UserInfo ui;
	private MainMenu mm;
	private JButton send = new JButton("发消息");
	private JButton delete = new JButton("删除");
	private JMenuBar groupMember = new JMenuBar();
	private JFrame frame;

	public Operate(String key,GUIManager gr,MessageLinkedList mll,UserInfo ui,MainMenu mm){
		frame = new JFrame();
		this.mm = mm;
		this.key = key;
		this.gr = gr;
		this.ui = ui;
		this.mll = mll;
		String[] s = key.split(" ");
		frame.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
		frame.setLayout(null);

		frame.add(send);
		send.setBounds(10,10,80,40);
		Send sendAc = new Send();
		send.addActionListener(sendAc);
		                             
		frame.add(delete);
		delete.setBounds(80,10,80,40);
		Delete deleteAC = new Delete();
		delete.addActionListener(deleteAC);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	private class Send implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Chat chat = (Chat)gr.get(key);
			if(chat == null){
				chat = new Chat(mll,ui,gr);
				gr.push(key,chat);
				chat.setKey(key);
			}

			frame.setVisible(false);
		}
	}
	private class Delete implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Message me = new Message();
			String[] s = key.split(" ");
			if(s[0].equals("g")){
				//String groupName = ui.getGroupInfo(s[2]);
				me.sendAccount = s[1];
				me.groupAccount = s[2];
				me.type = 6;
			}else{
				me.sendAccount = s[1];
				me.receiveAccount = s[2];
				me.type = 9;
			}
			mll.pusho(me);
			frame.setVisible(false);
		}
	}
}

class CreateNewGroup{
	private static final int DEFAULT_WIDTH = 300;
	private static final int DEFAULT_HEIGHT = 200;
	private JTextField field = new JTextField("",20);
	private JFrame frame;
	private MessageLinkedList mll;
	private UserInfo ui;
	public CreateNewGroup(UserInfo ui,MessageLinkedList mll){
		this.ui = ui;
		this.mll = mll;
		frame = new JFrame();
		frame.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
		frame.setLayout(null);

		frame.add(field);
		field.setBounds(50,60,200,30);
		JLabel jl = new JLabel("输入群名称");
		frame.add(jl);
		jl.setBounds(50,10,140,60);
		                             
	     	JButton send = new JButton("确定");
		frame.add(send);
		send.setBounds(120,120,60,33);
		Send sendAC = new Send();
		send.addActionListener(sendAC);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	private class Send implements ActionListener{
		public void actionPerformed(ActionEvent s){
			Message me = new Message();
			me.sendAccount = ui.getAccount();
			me.groupName = field.getText();
			me.type = 10;
			mll.pusho(me);	
			frame.setVisible(false);
		}
	}
}
class IpAddress{
	private static final int DEFAULT_WIDTH = 300;
	private static final int DEFAULT_HEIGHT = 200;
	private JTextField field = new JTextField("",20);
	private JFrame frame;
	public IpAddress(){
		frame = new JFrame();
		frame.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
		frame.setLayout(null);

		frame.add(field);
		field.setBounds(50,60,200,30);
		JLabel jl = new JLabel("请输入服务器ip地址");
		frame.add(jl);
		jl.setBounds(50,10,140,60);
		                             
	     	JButton send = new JButton("确定");
		frame.add(send);
		send.setBounds(120,120,60,33);
		Send sendAC = new Send();
		send.addActionListener(sendAC);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	private class Send implements ActionListener{
		public void actionPerformed(ActionEvent s){
			try{
				File f = new File("ipAddress");
				if(!f.exists()){
					f.createNewFile();
				}
				FileWriter out = new FileWriter(f);
				out.write(field.getText());
				out.close();
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
			frame.setVisible(false);
		}
	}
}


class AddFriendOrGroup{
	private static final int DEFAULT_WIDTH = 300;
	private static final int DEFAULT_HEIGHT = 200;
	private JTextField jf = new JTextField();
	private MessageLinkedList mll;
	private boolean friendOrGroup;
	private Message me = new Message();
	private JFrame frame;
	private UserInfo ui;

	public AddFriendOrGroup(UserInfo ui,boolean b,MessageLinkedList mll){
		frame = new JFrame();
		this.mll = mll;
		this.ui = ui;
		friendOrGroup = b;
		frame.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
		frame.setLayout(null);

		JButton set = new JButton("确定");
		frame.add(set);
		set.setBounds(120,120,60,33);
		Send send = new Send();
		set.addActionListener(send);

		frame.add(jf);
		jf.setBounds(50,60,200,30);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	private class Send implements ActionListener{
		public void actionPerformed(ActionEvent e){
			me.sendAccount = ui.getAccount();
			if(friendOrGroup == true){
				me.name = ui.getName();	
				me.receiveAccount = jf.getText();
				me.type = 7;
			}else{
				me.name = ui.getName();
				me.groupAccount = jf.getText();
				me.type = 4;
			}
			mll.pusho(me);
			frame.setVisible(false);
		}
	}
}

