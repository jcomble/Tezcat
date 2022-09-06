package Commands;

import java.util.concurrent.TimeUnit;

import Classes.CommandParameters;
import Classes.DiscordCommand;
import net.dv8tion.jda.api.EmbedBuilder;

public class Help extends DiscordCommand {
	
	public Help(CommandParameters params) {
		super(params);
	}
	
	@Override
	public void execute() {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Aide:");
		String value = "`" + prefix + "setmodorole` : Définit le rôle de modération du bot";
		embed.addField("Commandes Admin :", value, false);
		value = "`" + prefix + "changecolor` : Changer la couleur de la barre\n"
				+ "`" + prefix + "changeprefix` : Changer le préfixe\n"
				+ "`" + prefix + "changequestion` : Changer une question en particulier\n"
				+ "`" + prefix + "creategame` : Initialise un questionnaire\n"
				+ "`" + prefix + "downloadresults` : Télécharge les résultats du jeu\n"
				+ "`" + prefix + "play` : Lance le questionnaire\n"
				+ "`" + prefix + "resetgame` : Efface toutes les questions\n"
				+ "`" + prefix + "showquestions` : Affiche toutes les questions\n"
				+ "`" + prefix + "switchanswer` : Inverse la réponse des questions choisies\n"
				+ "`" + prefix + "top5` : Affiche le top 5 du jeu\n";
		embed.addField("Commandes Modérateurs :", value, false);
		channel.sendMessageEmbeds(embed.build()).queueAfter(20, TimeUnit.MILLISECONDS);
	}
}
