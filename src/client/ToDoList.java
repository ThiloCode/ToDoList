package client;
import java.util.ArrayList;
import java.time.LocalDate;

import org.bson.Document;

public class ToDoList {
	
	private ArrayList<Item> itemList;
	
	private int achievedToday = 0;
	private int achievedAllTime = 0;
	private int tasksCreated = 0;
	private int currentlySelectedItemIndex = -1;
	
	private LocalDate initializationDate;
	
	private ModularIndex index;
	
	private Item currentlySelectedItem;
	
	public ToDoList(ArrayList<Item> itemList, AchievementStatistics statistics, LocalDate initializationDate){
		this.itemList = itemList;
		
		achievedToday = statistics.achievedToday;
		achievedAllTime = statistics.achievedAllTime;
		tasksCreated = statistics.tasksCreated;
		
		
		if(LocalDate.now().isAfter(initializationDate)){
			this.initializationDate = LocalDate.now();
			achievedToday = 0;
		}else{
			this.initializationDate = initializationDate;
		}
		
		index = new ModularIndex(itemList.size());
		updateCurrentlySelectedItem();
	}
	
	public ToDoList(String JSON){
		Document toDoListDocument = Document.parse(JSON);
		
		initializationDate = LocalDate.of(toDoListDocument.getInteger("year"), toDoListDocument.getInteger("month"), toDoListDocument.getInteger("day"));
		
		itemList = new ArrayList<Item>();
		ArrayList<String> itemContents = (ArrayList<String>)toDoListDocument.getList("list", String.class);
		for(String itemContent : itemContents){
			itemList.add(new Item(itemContent));
		}
		
		achievedToday = toDoListDocument.getInteger("achievedToday");
		achievedAllTime = toDoListDocument.getInteger("achievedAllTime");
		tasksCreated = toDoListDocument.getInteger("tasksCreated");
		
		index = new ModularIndex(itemList.size());
		updateCurrentlySelectedItem();
	}
	
	public ToDoList(){
		itemList = new ArrayList<Item>();
		
		achievedToday = 0;
		achievedAllTime = 0;
		tasksCreated = 0;
		
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
	
	public LocalDate getInitializationDate(){
		return initializationDate;
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
	
	//ensure that only one thread can modify the achievedToday field
	public synchronized void achieveItem(){
		itemList.remove(index.getValue());
		index.removeItem();
		updateCurrentlySelectedItem();
		achievedToday++;
		achievedAllTime++;
	}
	
	//ensure that only one thread can modify the achievedToday field
	public synchronized void newDayReset(){
		achievedToday = 0;
		initializationDate = LocalDate.now();
	}
	
	public Document convertToDocument(){
		ArrayList<String> itemContents = new ArrayList<String>();
		for(Item item : itemList){
			itemContents.add(item.toString());
		}
		
		Document toDoListDocument = new Document();
		
		toDoListDocument.append("list", itemContents);
		toDoListDocument.append("achievedToday", achievedToday);
		toDoListDocument.append("achievedAllTime", achievedAllTime);
		toDoListDocument.append("tasksCreated", tasksCreated);
		
		return toDoListDocument;
	}
}
