package commands;
import client.ToDoList;
import client.ScreenManager;
public class AchieveItemCommand implements Command{
	private ToDoList toDoList;
	
	private String[] praise = {"Good Job!", "Nice work!", "Woohoo!"};
	
	
	public AchieveItemCommand(ToDoList toDoList){
		this.toDoList = toDoList;
	}
	
	public void execute(){
		
		ScreenManager.displayStatusMessage(praise[(int)(Math.random() * 3)]);
		toDoList.achieveItem();
	}
}
