import client.my.gui.ChatGUI;
import java.io.*;
import client.my.net.ReceivePool;
import client.my.net.SendPool;
import client.my.messagelinkedlist.MessageLinkedList;
import server.my.message.Message;
import java.lang.Thread;
import java.lang.Runnable;

public class ChatClient{
	public static void main(String[] args){
		File file = new File("ipAddress");
		String ipAddress;
		MessageLinkedList mll = new MessageLinkedList();
		Thread receivePool = new Thread(new ReceivePool(mll));
		Thread chatGui = new Thread(new ChatGUI(mll));
		Thread sendPool;

		receivePool.start();
		chatGui.start();
		while(true){
			try{
				if(!file.exists()){
					file.createNewFile();
					continue;
				}else if(file.length() == 0){
					Thread.sleep(1);
					continue;
				}

				FileReader fr = new FileReader(file);
				char[] buf = new char[48]; 
				int len = fr.read(buf);
				ipAddress = new String(buf,0,len);
				break;
			}catch(IOException e){
				e.printStackTrace();
			}catch(Throwable e){
				e.printStackTrace();
			}
		}
		sendPool = new Thread(new SendPool(mll,ipAddress));
		sendPool.start();
	}
}
