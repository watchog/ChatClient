package client.my.net;
import client.my.messagelinkedlist.MessageLinkedList;
import server.my.message.Message;
import java.lang.Thread;
import java.io.*;
import java.lang.Runnable;
import java.net.*;

class Receive implements Runnable{
	private MessageLinkedList mll;
	private int port = 22138;

	public Receive(MessageLinkedList mll){
		this.mll = mll;
	}

	public void run(){
		try{
			ServerSocket server = new ServerSocket(port);
			while(true){
			
				
				Socket socket = server.accept();
				InputStream is = socket.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				mll.pushi((Message)ois.readObject());
				System.out.println("信息成功接收！！！");
				socket.close();
				
				ois.close();
			
			}
		}catch(IOException e){
			e.printStackTrace();		
		}catch(ClassNotFoundException e){
			e.printStackTrace();	
		}
	}
}

public class ReceivePool implements Runnable{
	private Thread receive;
	
	public ReceivePool(MessageLinkedList mll){
		receive = new Thread(new Receive(mll));
	}

	public void run(){
		receive.start();
	}
}
