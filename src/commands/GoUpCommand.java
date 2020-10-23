package commands;
import client.ToDoList;
public class GoUpCommand implements Command{
	private ToDoList toDoList;
	public GoUpCommand(ToDoList toDoList){
		this.toDoList = toDoList;
	}
	
	public void execute(){
		toDoList.getIndex().decrement();
		toDoList.updateCurrentlySelectedItem();
	}
}
