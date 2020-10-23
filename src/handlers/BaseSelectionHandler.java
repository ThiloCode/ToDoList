package handlers;
import java.util.HashMap;
import client.Application;
import client.ToDoList;
import commands.Command;
import commands.SelectCommand;
import commands.CreateItemCommand;
import commands.QuitCommand;

public class BaseSelectionHandler {
	private HashMap<String, Command> rules = new HashMap<String, Command>();
	
	public BaseSelectionHandler(ToDoList toDoList, Application app){
		rules.put("s", new SelectCommand(toDoList));
		rules.put("a", new CreateItemCommand(toDoList));
		rules.put("q", new QuitCommand(app));
	}
	
	public Command handle(String input) throws NoRuleException{
		if(rules.get(input) == null){
			throw new NoRuleException("BaseSelection", input);
		}else{
			return rules.get(input);
		}
	}
}
