import java.util.HashMap;

public class ModifyItemHandler {
	private HashMap<String, Command> rules = new HashMap<String, Command>();
	
	public ModifyItemHandler(ToDoList toDoList){
		rules.put("a", new AchieveItemCommand(toDoList));
		rules.put("d", new DeleteItemCommand(toDoList));
		rules.put("c", new ChangeItemCommand(toDoList));
	}
	
	public Command handle(String selection){
		return rules.get(selection);
	}
}
