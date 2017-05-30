package client.my.net;
import java.net.*;
import java.io.*;
import server.my.message.Message;
import java.lang.Thread;
import java.lang.Runnable;
import client.my.messagelinkedlist.MessageLinkedList;

public class SendPool implements Runnable{
	private MessageLinkedList mll;
	private Message me;
	private String ipAddress;
	private String localIpAddress;

	public SendPool(MessageLinkedList mll,String ipAddress){
		this.mll = mll;
		this.ipAddress = ipAddress;
		System.out.println("hello");
		 
	}

	public void run(){
		try{
			File file = new File("localIpAddress");
			FileReader fr = new FileReader(file);
			char[] buf = new char[48];
			int len = fr.read(buf);
			localIpAddress = new String(buf,0,len);

			while(true){
				if(!mll.isEmptyo()){
					me = mll.popo();
					me.ipAddress = localIpAddress;
					System.out.println(me.ipAddress);
					Socket socket = new Socket(InetAddress.getByName(ipAddress),12138);
					OutputStream os = socket.getOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(os);
					oos.writeObject(me);
					System.out.println("信息成功发送！！！");
					oos.close();
					socket.close();
				}
			}
		}catch(ConnectException e){
			System.out.println("服务端不在线 3秒后客户端自动关闭");
			for(int i = 3;i > 0;i--){
				try{
					Thread.sleep(1000);
					System.out.println(i);
				}catch(InterruptedException w){
					w.printStackTrace();
				}
			}
			System.exit(0);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
