package client.my.guimanager;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import javax.swing.*;
import java.awt.*;

public class GUIManager{
	private HashMap<String,Object> gui = new HashMap<String,Object>();

	public synchronized void push(String s,Object jf){
		gui.put(s,jf);
	}
	public synchronized Object get(String s){
		if(gui.containsKey(s))
			return gui.get(s);
		return null;
	}
	public synchronized void remove(String s){
		gui.remove(s);
	}
	public synchronized boolean containsKey(String key){
		if(gui.containsKey(key))
			return true;
		else 
			return false;
	}
	public synchronized int size(){
		Set<String> s = gui.keySet();
		Iterator<String> iter = s.iterator();
		System.out.println("**************************");
		while(iter.hasNext())
			System.out.println(iter.next());
		System.out.println("**************************");
		return gui.size();
	}
}
