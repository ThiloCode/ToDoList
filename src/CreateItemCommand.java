import java.util.ArrayList;
import java.util.Scanner;

public class CreateItemCommand implements Command{
	private ToDoList toDoList;
	
	public CreateItemCommand(ToDoList toDoList){
		this.toDoList = toDoList;
	}
	
	public void execute(){
		toDoList.clearScreen();
		toDoList.displayItems();
		
		System.out.println("Create New Item");

		String content = toDoList.getScanner().nextLine();
		toDoList.addItem(content);
		
		toDoList.clearScreen();
	}
}
