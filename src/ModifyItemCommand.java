
public class ModifyItemCommand implements Command{
	private ToDoList toDoList;
	
	public ModifyItemCommand(ToDoList toDoList){
		this.toDoList = toDoList;
	}
	
	public void execute(){
		String selection = "";
		if(toDoList.getItemList().size() > 0){
			ModifyItemHandler handler = new ModifyItemHandler(toDoList);
			
			ScreenManager.displayStatusMessage("Change Item: " + toDoList.getCurrentlySelectedItem().toString());
				
			selection = Application.getScanner().nextLine();
			
			Command handleCommand = handler.handle(selection);
			handleCommand.execute();
		}
	}
}
