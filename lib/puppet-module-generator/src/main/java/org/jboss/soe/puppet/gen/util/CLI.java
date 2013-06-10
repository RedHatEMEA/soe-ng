package org.jboss.soe.puppet.gen.util;

import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Lists.transform;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static org.apache.commons.lang3.StringUtils.repeat;

import java.util.List;

import com.google.common.base.Function;

/**
 * CLI command container 
 *  
 * @author ssadeghi
 */
public class CLI {
	private static final String COMMENT = "# %s";
	
	private List<String> commands = newLinkedList();
	
	public List<String> getCommands() {
		return unmodifiableList(commands);
	}
	
	private List<String> indent(final int indent, final List<String> list) {
		return transform(list, new Function<String, String>() {
			public String apply(String input) {
				return repeat(' ', 4*indent) + input;
			}
		});
	}
	
	public CLI addNewLine() {
		commands.add("");
		return this;
	}
	
	public CLI addCommand(String cmd) {
		commands.add(cmd);
		return this;
	}

	public CLI addCommands(List<String> commands) {
		this.commands.addAll(commands);
		return this;
	}
	
	public CLI addComment(String comment) {
		addNewLine();
		addCommand(format(COMMENT, comment));
		return this;
	}
	
	public CLI addIfBlock(IfBlock ifBlock) {
		addCommand("if (outcome == success) of " + ifBlock.test());
		addCommands(indent(1, ifBlock.ifSuccess()));
		addCommand("else");
		addCommands(indent(1, ifBlock.ifFail()));
		addCommand("end-if");
		addNewLine();
		
		return this;
	}
	
	static interface IfBlock {
		String test();
		List<String> ifSuccess();
		List<String> ifFail();
		
		static class IfBlockBuilder {
			String testCondition;
			List<String> successCommands;
			List<String> failCommands;
			
			public static IfBlockBuilder create() {
				return new IfBlockBuilder();
			}
			
			public IfBlockBuilder withTestCommand(String testCondition) {
				this.testCondition = testCondition;
				return this;
			}
			
			public IfBlockBuilder withSuccessCommands(String...commands) {
				successCommands = asList(commands);
				return this;
			}
			
			public IfBlockBuilder withFailCommands(String...commands) {
				failCommands = asList(commands);
				return this;
			}
			
			public IfBlock build() {
				return new IfBlock() {
					@Override
					public String test() {
						return testCondition;
					}
					
					@Override
					public List<String> ifSuccess() {
						return successCommands;
					}
					
					@Override
					public List<String> ifFail() {
						return failCommands;
					}
				};
			}
		}
	}
}
