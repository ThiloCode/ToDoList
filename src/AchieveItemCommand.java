
public class AchieveItemCommand implements Command{
	private ToDoList toDoList;
	
	public AchieveItemCommand(ToDoList toDoList){
		this.toDoList = toDoList;
	}
	
	public void execute(){
		toDoList.setStatusMessage("Woohoo!");
		toDoList.achieveItem();
	}
}
