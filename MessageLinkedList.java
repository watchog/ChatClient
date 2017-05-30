package client.my.messagelinkedlist;
import server.my.message.Message;
import java.util.LinkedList;
import java.util.Deque;

public class MessageLinkedList{
	private Deque<Message> input;
	private Deque<Message> wait;
	private Deque<Message> output;

	public MessageLinkedList(){
		input = new LinkedList<Message>();
		output = new LinkedList<Message>();
		wait = new LinkedList<Message>();
	}
	public synchronized void pushw(Message me){
		wait.offer(me);
	}
	public synchronized Message popw(){
		return wait.poll();
	}
	public synchronized void pushi(Message me){
		input.offer(me);
	}

	public synchronized void pusho(Message me){
		output.offer(me);
	}

	public synchronized Message popi(){
		return input.poll();
	}

	public synchronized Message popo(){
		return output.poll();
	}

	public synchronized boolean isEmptyi(){
		if(input.size() > 0)
			return false;
		else 
			return true;
	}

	public synchronized boolean isEmptyo(){
		if(output.size() > 0)
			return false;
		else
			return true;
	}
	public synchronized boolean isEmptyw(){
		if(wait.size() > 0)
			return false;
		else
			return true;
	}
	public synchronized int sizeo(){
		return output.size();
	}
}
