
public class GoDownCommand implements Command{
	private ToDoList toDoList;
	
	public GoDownCommand(ToDoList toDoList){
		this.toDoList = toDoList;
	}
	
	public void execute(){
		toDoList.getIndex().increment();
		toDoList.updateCurrentlySelectedItem();
	}
}
