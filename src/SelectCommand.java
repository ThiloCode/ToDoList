import java.util.ArrayList;
import java.util.Scanner;
public class SelectCommand implements Command{
	
	private ToDoList toDoList;
	
	public SelectCommand(ToDoList toDoList){
		this.toDoList = toDoList;
	}
	
	public void print(){
		String block = "_______________________________________\nCurrently Selected\n" + toDoList.getCurrentlySelectedItem().toString() + "\n---------------------------------------";
		ScreenManager.displayBlock(block);
	}
	
	public void displayNoItemMessage(){
		ScreenManager.clearScreen();
		ScreenManager.displayStatusMessage("No Items to Select! Exiting Select Mode.");
	}
	
	public void execute(){
		String selection = "";
		if(toDoList.getItemList().size() > 0){
			SelectItemHandler handler = new SelectItemHandler(toDoList);
			
			while(!selection.equals("q") && toDoList.getItemList().size() > 0){
				ScreenManager.resetScreen(toDoList.getItemList(), null, toDoList.generateAchievementStatistics());
				print();
				
				selection = Application.getScanner().nextLine();
				try{
					Command handleCommand = handler.handle(selection);
					handleCommand.execute();
				}catch(NoRuleException e){
					//System.out.println(e);
				}
			}
			
			if(selection.equals("q")){
				ScreenManager.displayStatusMessage("Exiting Select Mode.");
			}else if(toDoList.getItemList().size() == 0){
				displayNoItemMessage();
			}	
		}else{
			displayNoItemMessage();
		}
	}
}
