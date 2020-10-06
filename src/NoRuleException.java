
public class NoRuleException extends Exception{
	public NoRuleException(String ruleHandler, String ruleName){
		super("No Rule Implemented for rule " + ruleName + " in " + ruleHandler);
	}
}
