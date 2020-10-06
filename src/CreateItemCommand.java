import java.util.ArrayList;
import java.util.Scanner;

public class CreateItemCommand implements Command{
	private ToDoList toDoList;
	
	public CreateItemCommand(ToDoList toDoList){
		this.toDoList = toDoList;
	}
	
	public void execute(){
		ScreenManager.clearScreen();
		ScreenManager.displayItems(toDoList.getItemList());
		ScreenManager.displayStatusMessage("Create New Item");

		String content = Application.getScanner().nextLine();
		toDoList.addItem(content);
		
		ScreenManager.clearScreen();
	}
}
