package Commands;

import java.util.concurrent.TimeUnit;

import Classes.CommandParameters;
import Classes.DiscordCommand;

public class Survey extends DiscordCommand{

	public Survey(CommandParameters params) {
		super(params);
	}

	@Override
	public void execute() {
		if (args.size() != 2) {
			channel.sendMessage("`" + prefix + "survey \"Question\"` seulement!").queueAfter(20, TimeUnit.MILLISECONDS);
			return;
		}
		
	}

}
