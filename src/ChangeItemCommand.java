
public class ChangeItemCommand implements Command{
	private ToDoList toDoList;
	
	public ChangeItemCommand(ToDoList toDoList){
		this.toDoList = toDoList;
	}
	
	public void execute(){
		ScreenManager.displayStatusMessage("Change Item Contents");
		String newContent = Application.getScanner().nextLine();
		toDoList.getCurrentlySelectedItem().modifyContent(newContent);
		ScreenManager.displayStatusMessage("Content Changed");
	}
}
