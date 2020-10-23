package handlers;
import java.util.HashMap;
import client.ToDoList;
import commands.Command;
import commands.GoUpCommand;
import commands.GoDownCommand;
import commands.AchieveItemCommand;
import commands.DeleteItemCommand;
import commands.ModifyItemCommand;

public class SelectItemHandler {
	private HashMap<String, Command> rules = new HashMap<String, Command>();
	
	public SelectItemHandler(ToDoList toDoList){
		rules.put("w", new GoUpCommand(toDoList));
		rules.put("s", new GoDownCommand(toDoList));
		rules.put("a", new AchieveItemCommand(toDoList));
		rules.put("d", new DeleteItemCommand(toDoList));
		rules.put("c", new ModifyItemCommand(toDoList));
	}
	
	public Command handle(String input) throws NoRuleException{
		if(rules.get(input) == null){
			throw new NoRuleException("SelectItem", input);
		}else{
			return rules.get(input);
		}
	}
}