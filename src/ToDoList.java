import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ToDoList {
	
	private ArrayList<Item> itemList;
	
	private int achievedToday = 0;
	private int tasksCreated = 0;
	private int currentlySelectedItemIndex = -1;
	
	private boolean quit = false;
	
	private String statusMessage = "";
	
	private ModularIndex index;
	
	private Item currentlySelectedItem;
	
	private Scanner userInput = new Scanner(System.in);
	
	public static void main(String[] args) {
		ToDoList todolist = new ToDoList(new ArrayList<Item>());
		todolist.start();
	}
	
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
	
	public Scanner getScanner(){
		return userInput;
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
	}
	
	public void createItem(){			
		System.out.println("Create New Item");

		String content = userInput.nextLine();
		addItem(content);
	}
	
	public void close(){
		quit = true;
		System.out.println("Saving...");
		save();
	}
	
	public void save(){
		try{
			FileOutputStream outputStream = new FileOutputStream("Data.txt");
			ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);
			
			objectStream.writeObject(itemList);
			objectStream.close();
		}catch(IOException e){
			System.out.println("Saving failed!");
		}
	}
	
	public void load(){
		try{
			//if the file exists already then we have to load it
			//if not we have to create it for saving later
			File data = new File("Data.txt");
			if(!data.createNewFile()){
				FileInputStream inputStream = new FileInputStream("Data.txt");
				ObjectInputStream objectStream = new ObjectInputStream(inputStream);
				
				itemList = (ArrayList<Item>)objectStream.readObject();
				index = new ModularIndex(itemList.size());
				updateCurrentlySelectedItem();
				
				objectStream.close();
			}
		}catch(IOException e){
			System.out.println("Error occurred loading!");
		}catch(ClassNotFoundException e){
			System.out.println("Error occurred loading!");
		}
	}
	
	public void handleBaseSelection(){
		BaseSelectionHandler handler = new BaseSelectionHandler(this);
		String selection = userInput.nextLine();
		
		try{
			Command handleCommand = handler.handle(selection);
			handleCommand.execute();
		}catch(NoRuleException e){
			//System.out.println(e);
		}
	}
	
	public void clearScreen(){
		try{
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		}catch(Exception e){
			System.out.println("Huge Problem!");
		}
	}
	
	public void resetScreen(){
		clearScreen();
		displayItems();
		displayStatusMessage();
	}
	
	public void start(){
		load();
		
		clearScreen();
		while(!quit){
			resetScreen();
			handleBaseSelection();
		}
	}

}
