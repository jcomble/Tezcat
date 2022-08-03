package Classes;

import java.util.ArrayList;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public abstract class DiscordCommand {
	
	protected ArrayList<String> args;
	protected MessageChannel channel;
	protected Guild guild;
	protected Message message;
	protected char prefix;
	protected SQLRequester req;
	protected User user;

	public DiscordCommand(CommandParameters params) {
		this.args = params.getArgs();
		this.channel = params.getChannel();
		this.guild = params.getGuild();
		this.message = params.getMessage();
		this.prefix = params.getPrefix();
		this.req = params.getReq();
		this.user = params.getUser();
	}
	
	public abstract void execute();
}
