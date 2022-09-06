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
		String value = "`" + prefix + "setmodorole` : D�finit le r�le de mod�ration du bot";
		embed.addField("Commandes Admin :", value, false);
		value = "`" + prefix + "changecolor` : Changer la couleur de la barre\n"
				+ "`" + prefix + "changeprefix` : Changer le pr�fixe\n"
				+ "`" + prefix + "changequestion` : Changer une question en particulier\n"
				+ "`" + prefix + "creategame` : Initialise un questionnaire\n"
				+ "`" + prefix + "downloadresults` : T�l�charge les r�sultats du jeu\n"
				+ "`" + prefix + "play` : Lance le questionnaire\n"
				+ "`" + prefix + "resetgame` : Efface toutes les questions\n"
				+ "`" + prefix + "showquestions` : Affiche toutes les questions\n"
				+ "`" + prefix + "switchanswer` : Inverse la r�ponse des questions choisies\n"
				+ "`" + prefix + "top5` : Affiche le top 5 du jeu\n";
		embed.addField("Commandes Mod�rateurs :", value, false);
		channel.sendMessageEmbeds(embed.build()).queueAfter(20, TimeUnit.MILLISECONDS);
	}
}
