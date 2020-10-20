import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;

public class ToDoList {
	
	private ArrayList<Item> itemList;
	
	private int achievedToday = 0;
	private int achievedAllTime = 0;
	private int tasksCreated = 0;
	private int currentlySelectedItemIndex = -1;
	
	private boolean quit = false;
	
	private String statusMessage = "";
	
	private ModularIndex index;
	
	private Item currentlySelectedItem;
	
	public ToDoList(ArrayList<Item> itemList){
		this.itemList = itemList;
		index = new ModularIndex(itemList.size());
		updateCurrentlySelectedItem();
	}
	
	public ArrayList<Item> getItemList(){
		return itemList;
	}
	
	public ModularIndex getIndex(){
		return index;
	}
	
	public int getCurrentlySelectedItemIndex(){
		return currentlySelectedItemIndex;
	}
	
	public void setCurrentlySelectedItemIndex(int index){
		currentlySelectedItemIndex = index;
	}
	
	public Item getCurrentlySelectedItem(){
		return currentlySelectedItem;
	}
	
	public void updateCurrentlySelectedItem(){
		if(itemList.size() == 0){
			currentlySelectedItem = null;
		}else{
			currentlySelectedItem = itemList.get(index.getValue());
		}
	}
	
	public void setStatusMessage(String status){
		statusMessage = status;
	}
	
	public void displayStatusMessage(){
		if(!statusMessage.equals("")){
			System.out.println();
			System.out.println("_______________________________________");
			System.out.println();
			System.out.println(statusMessage);
			System.out.println("_______________________________________");
			System.out.println();
			statusMessage = "";
		}
	}
	
	public void displayItems(){
		System.out.println("_______________________________________");
		System.out.println("---------------------------------------");
		for(int i = 0; i < itemList.size(); i++){
			System.out.println(itemList.get(i));
			System.out.println("---------------------------------------");
		}
	}
	
	public void addItem(String content){
		itemList.add(new Item(content));
		index.addItem();
		updateCurrentlySelectedItem();
		tasksCreated++;
	}
	
	public void deleteSelectedItem(){
		if(currentlySelectedItem != null){
			itemList.remove(index.getValue());
			index.removeItem();
			updateCurrentlySelectedItem();
			
			tasksCreated--;
		}
	}
	
	public void achieveItem(){
		itemList.remove(index.getValue());
		index.removeItem();
		updateCurrentlySelectedItem();
		
		achievedToday++;
		achievedAllTime++;
	}
}
