import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;

public class ToDoList {
	
	private ArrayList<Item> itemList;
	
	private int achievedToday = 10;
	private int achievedAllTime = 0;
	private int tasksCreated = 0;
	private int currentlySelectedItemIndex = -1;
	
	private boolean quit = false;
	
	private LocalDate initializationDate;
	
	private ModularIndex index;
	
	private Item currentlySelectedItem;
	
	public ToDoList(ArrayList<Item> itemList){
		this.itemList = itemList;
		initializationDate = LocalDate.now();
		index = new ModularIndex(itemList.size());
		updateCurrentlySelectedItem();
	}
	
	public ArrayList<Item> getItemList(){
		return itemList;
	}
	
	public ModularIndex getIndex(){
		return index;
	}
	
	public AchievementStatistics generateAchievementStatistics(){
		return new AchievementStatistics(tasksCreated, achievedToday, achievedAllTime);
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
	
	public void newDayReset(){
		achievedToday = 0;
	}
}
