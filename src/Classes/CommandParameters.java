package Classes;

import java.util.ArrayList;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class CommandParameters {
	private MessageChannel channel;
	private ArrayList<String> args;
	private SQLRequester req;
	private User user;
	private Guild guild;
	private Message message;
	private char prefix;
	
	public CommandParameters(MessageChannel channel, ArrayList<String> args, SQLRequester req, User user, Guild guild,
			Message message, char prefix) {
		this.channel = channel;
		this.args = args;
		this.req = req;
		this.user = user;
		this.guild = guild;
		this.message = message;
		this.prefix = prefix;
	}
	
	public MessageChannel getChannel() {
		return channel;
	}
	public void setChannel(MessageChannel channel) {
		this.channel = channel;
	}
	public ArrayList<String> getArgs() {
		return args;
	}
	public void setArgs(ArrayList<String> args) {
		this.args = args;
	}
	public SQLRequester getReq() {
		return req;
	}
	public void setReq(SQLRequester req) {
		this.req = req;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Guild getGuild() {
		return guild;
	}
	public void setGuild(Guild guild) {
		this.guild = guild;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public char getPrefix() {
		return prefix;
	}
	public void setPrefix(char prefix) {
		this.prefix = prefix;
	}
}
