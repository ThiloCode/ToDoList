
public class ModifyItemCommand implements Command{
	private ToDoList toDoList;
	
	public ModifyItemCommand(ToDoList toDoList){
		this.toDoList = toDoList;
	}
	
	public void print(){
		System.out.println("Change Item: " + toDoList.getCurrentlySelectedItem());
	}
	
	public void execute(){
		String selection = "";
		if(toDoList.getItemList().size() > 0){
			ModifyItemHandler handler = new ModifyItemHandler(toDoList);
			
			print();
				
			selection = toDoList.getScanner().nextLine();
			
			Command handleCommand = handler.handle(selection);
			handleCommand.execute();
		}
	}
}
