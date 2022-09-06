package Commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import Classes.CommandParameters;
import Classes.DiscordCommand;
import Classes.EmbedGame;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;

public class Play extends DiscordCommand{

	public Play(CommandParameters params) {
		super(params);
	}

	@Override
	public void execute() {
		if (args.size() != 1) {
			channel.sendMessage("`" + prefix + "play` seulement!").queueAfter(20, TimeUnit.MILLISECONDS);
			return;
		}
		ResultSet res;
		try {
			String requete = "SELECT * FROM Modos WHERE id_server = " + guild.getId() + ";";
			System.out.println("SQL : " + requete);
			res = req.request(requete);
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
			requete = "SELECT * FROM Game WHERE id_server = " + guild.getId() + ";";
			System.out.println("SQL : " + requete);
			res = req.request(requete);
			if (res.next()) {
				channel.sendMessage("Il y a un questionnaire en cours!").queueAfter(20, TimeUnit.MILLISECONDS);
				res.close();
				return;
			}
			res.close();
			requete = "DELETE FROM ReponsesDonnees WHERE id_server = " + guild.getId() + ";";
			System.out.println("SQL : " + requete);
			req.update(requete);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		EmbedGame embedgame = new EmbedGame(guild, 1, req, color);
		channel.sendMessageEmbeds(embedgame.getEmbed().build()).queueAfter(20, TimeUnit.MILLISECONDS,
			msg -> {
				try {
					String requete = "DELETE FROM ReponsesDonnees WHERE id_server = " + guild.getId() + ";";
					System.out.println("SQL : " + requete);
					req.update(requete);
					requete = "INSERT INTO Game VALUES (" + guild.getId() + ", " + msg.getId() + ", 1)";
					System.out.println("SQL : " + requete);
					req.update(requete);
					msg.addReaction(Emoji.fromFormatted("✅")).queueAfter(20, TimeUnit.MILLISECONDS);
					msg.addReaction(Emoji.fromFormatted("❌")).queueAfter(20, TimeUnit.MILLISECONDS);
					msg.addReaction(Emoji.fromFormatted("↘️")).queueAfter(20, TimeUnit.MILLISECONDS);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		);
	}

}
