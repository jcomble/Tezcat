package Commands;

import java.util.concurrent.TimeUnit;

import Classes.CommandParameters;
import Classes.DiscordCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;

public class Test extends DiscordCommand {
	
	public Test(CommandParameters params) {
		super(params);
	}
	
	@Override
	public void execute() {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Question n°X");
		embed.setDescription("Est-ce qu'il s'agit d'une musique officielle de Pokémon?");
		embed.setColor(color);
		channel.sendMessageEmbeds(embed.build()).queueAfter(20, TimeUnit.MILLISECONDS,
			msg -> {
				msg.addReaction(Emoji.fromFormatted("✅")).queueAfter(20, TimeUnit.MILLISECONDS);
				msg.addReaction(Emoji.fromFormatted("❌")).queueAfter(20, TimeUnit.MILLISECONDS);
				msg.addReaction(Emoji.fromFormatted("↘️")).queueAfter(20, TimeUnit.MILLISECONDS);
			}
		);
		channel.sendMessage("he").queueAfter(20, TimeUnit.MILLISECONDS,
			msg -> {
			}
		);
	}
}
