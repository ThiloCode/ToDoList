
public class ChangeItemCommand implements Command{
	private ToDoList toDoList;
	
	public ChangeItemCommand(ToDoList toDoList){
		this.toDoList = toDoList;
	}
	
	public void execute(){
		System.out.println("Change Item Contents");
		String newContent = toDoList.getScanner().nextLine();
		toDoList.getCurrentlySelectedItem().modifyContent(newContent);
		System.out.println("Content Changed");
	}
}
