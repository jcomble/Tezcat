package Commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import Classes.CommandParameters;
import Classes.DiscordCommand;
import Classes.EmbedGame;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

public class NextQuestion extends DiscordCommand {

	public NextQuestion(CommandParameters params) {
		super(params);
	}

	@Override
	public void execute() {
		if (args.size() != 1) {
			channel.sendMessage("`" + prefix + "nextquestion` seulement!").queueAfter(20, TimeUnit.MILLISECONDS);
			return;
		}		
		String requete = "SELECT * FROM Modos WHERE id_server = " + guild.getId() + ";";
		System.out.println("SQL : " + requete);
		ResultSet res;
		try {
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
			if (role_modo == null || !member.getRoles().contains(role_modo)) {
				channel.sendMessage("Tu n'es pas modérateur toi!").queueAfter(20, TimeUnit.MILLISECONDS);
				return;
			}
			requete = "SELECT * FROM Game WHERE id_server = " + guild.getId() + ";";
			System.out.println("SQL : " + requete);
			res = req.request(requete);
			if (!res.next()) {
				res.close();
				channel.sendMessage("Il n'y a pas de jeu lancé pour le moment!").queueAfter(20, TimeUnit.MILLISECONDS);
				return;
			}
			int numero_question = res.getInt("numero_question");
			res.close();
			EmbedGame embedgame = new EmbedGame(guild, numero_question + 1, req, color);
			EmbedBuilder embed = embedgame.getEmbed();
			embed.setColor(color);
			Message msg = channel.sendMessageEmbeds(embed.build()).completeAfter(20, TimeUnit.MILLISECONDS);
			requete = "SELECT * FROM Questions WHERE id_server = " + guild.getId() + " AND numero_question = " + String.valueOf(numero_question + 1) + ";";
			System.out.println("SQL : " + requete);
			res = req.request(requete);
			if (!res.next()) {
				requete = "DELETE FROM Game WHERE id_server = " + guild.getId() + ";";
				System.out.println("SQL : " + requete);
				req.update(requete);
			} else {
				requete = "UPDATE Game SET numero_question = " + String.valueOf(numero_question + 1) + ", id_message = " + msg.getId() + " WHERE id_server = " + guild.getId() + ";";
				System.out.println("SQL : " + requete);
				req.update(requete);
			}
			res.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
}
