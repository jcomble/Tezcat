package Commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import Classes.CommandParameters;
import Classes.DiscordCommand;
import Classes.EmbedQuestions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;

public class ShowQuestions extends DiscordCommand {
	
	public ShowQuestions(CommandParameters params) {
		super(params);
	}
	
	@Override
	public void execute() {
		if (args.size() != 1) {
			channel.sendMessage("`" + prefix + "showquestion` seulement!").queueAfter(20, TimeUnit.MILLISECONDS);
			return;
		}
		EmbedQuestions embedquestions;
		try {
			String requete = "SELECT * FROM Modos WHERE id_server = " + guild.getId() + ";";
			System.out.println("SQL : " + requete);
			ResultSet res = req.request(requete);
			String id_modo = "";
			if (res.next()) {
				id_modo = res.getString("id_modo");
			}
			Role role_modo = null;
			if (!id_modo.equals("")) {
				role_modo = guild.getRoleById(id_modo);
			}
			Member member = guild.retrieveMemberById(user.getId()).completeAfter(20, TimeUnit.MILLISECONDS);
			if (!member.hasPermission(Permission.ADMINISTRATOR) && (role_modo == null || !member.getRoles().contains(role_modo))) {
				channel.sendMessage("Tu n'es ni modérateur ni administrateur!").queueAfter(20, TimeUnit.MILLISECONDS);
				return;
			}
			embedquestions = new EmbedQuestions(req, guild, 1, color);
			EmbedBuilder embed = embedquestions.getEmbed();
			embed.setColor(color);
			channel.sendMessageEmbeds(embed.build()).queueAfter(20, TimeUnit.MILLISECONDS,
				msg -> {
					msg.addReaction(Emoji.fromFormatted("⬅️")).queueAfter(20, TimeUnit.MILLISECONDS);
					msg.addReaction(Emoji.fromFormatted("➡️")).queueAfter(20, TimeUnit.MILLISECONDS);
					String request = "INSERT INTO EmbedQuestions VALUES(" + guild.getId() + ", " + msg.getId() + ", 1);";
					System.out.println("SQL : " + request);
					try {
						req.update(request);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
}
