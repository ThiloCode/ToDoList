package commands;
import client.Application;
import client.ToDoList;
import client.ScreenManager;

public class CreateItemCommand implements Command{
	private ToDoList toDoList;
	
	public CreateItemCommand(ToDoList toDoList){
		this.toDoList = toDoList;
	}
	
	public void execute(){
		ScreenManager.clearScreen();
		ScreenManager.displayItems(toDoList.getItemList());
		ScreenManager.displayPrompt("Create New Item");

		String content = Application.getScanner().nextLine();
		toDoList.addItem(content);
		
		ScreenManager.clearScreen();
	}
}
