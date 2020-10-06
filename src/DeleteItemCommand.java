
public class DeleteItemCommand implements Command{
	private ToDoList toDoList;
	
	public DeleteItemCommand(ToDoList toDoList){
		this.toDoList = toDoList;
	}
	
	
	public void execute(){
		toDoList.deleteSelectedItem();
	}
}
