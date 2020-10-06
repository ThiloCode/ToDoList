import java.util.ArrayList;
import java.util.Scanner;
public class SelectCommand implements Command{
	
	private ToDoList toDoList;
	
	public SelectCommand(ToDoList toDoList){
		this.toDoList = toDoList;
	}
	
	public void print(){
		System.out.println("_______________________________________");
		System.out.println("Currently Selected");
		System.out.println(toDoList.getCurrentlySelectedItem());
		System.out.println("---------------------------------------");
	}
	
	public void displayNoItemMessage(){
		System.out.println("No Items to Select! Exiting Select Mode.");
	}
	
	public void setNoItemStatus(){
		toDoList.setStatusMessage("No Items to Select! Exiting Select Mode.");
	}
	
	public void execute(){
		String selection = "";
		if(toDoList.getItemList().size() > 0){
			SelectItemHandler handler = new SelectItemHandler(toDoList);
			
			while(!selection.equals("q") && toDoList.getItemList().size() > 0){
				toDoList.resetScreen();
				print();
				
				selection = toDoList.getScanner().nextLine();
				try{
					Command handleCommand = handler.handle(selection);
					handleCommand.execute();
				}catch(NoRuleException e){
					System.out.println(e);
				}
			}
			
			if(selection.equals("q")){
				System.out.println("Exiting Select Mode.");
			}else if(toDoList.getItemList().size() == 0){
				toDoList.clearScreen();
				//toDoList.displayItems();
				//displayNoItemMessage();
				setNoItemStatus();
			}	
		}else{
			toDoList.clearScreen();
			setNoItemStatus();
		}
	}
}
