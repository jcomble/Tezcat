package Commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import Classes.CommandParameters;
import Classes.DiscordCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class Top5 extends DiscordCommand{

	public Top5(CommandParameters params) {
		super(params);
	}

	@Override
	public void execute() {
		if (args.size() != 1) {
			channel.sendMessage("`" + prefix + "top5` seulement!").queueAfter(20, TimeUnit.MILLISECONDS);
			return;
		}
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
			requete = "SELECT * FROM Game WHERE id_server = " + guild.getId() + ";";
			System.out.println("SQL : " + requete);
			res = req.request(requete);
			if (res.next()) {
				channel.sendMessage("Il y a un questionnaire en cours!").queueAfter(20, TimeUnit.MILLISECONDS);
				return;
			}
			res.close();
			requete = "SELECT id_membre, COUNT(*) AS bonnes_reponses\n" +
					"FROM Questions, ReponsesDonnees\n" + 
					"WHERE ReponsesDonnees.numero_question = Questions.numero_question\n" +
					"AND Questions.id_server = ReponsesDonnees.id_server\n" + 
					"AND ReponsesDonnees.reponse = Questions.reponse\n" +
					"AND ReponsesDonnees.id_server = " + guild.getId() + "\n" +
					"GROUP BY id_membre\n" + 
					"ORDER BY COUNT(*) DESC;";
			System.out.println("SQL : " + requete);
			res = req.request(requete);
			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle("Classement TOP 5:");
			for (int iterator = 1; iterator <= 5; iterator ++) {
				if(res.next()) {
					String id_membre = res.getString("id_membre");
					embed.addField("#" + String.valueOf(iterator) + " :", " <@" + id_membre + "> Score: " + res.getString("bonnes_reponses"), false);
				} else {
					break;
				}
			}
			embed.setColor(color);
			res.close();
			channel.sendMessageEmbeds(embed.build()).queueAfter(20, TimeUnit.MILLISECONDS);
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
	}

}
