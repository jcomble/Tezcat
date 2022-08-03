package Commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import Classes.CommandParameters;
import Classes.DiscordCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class SwitchAnswer extends DiscordCommand {

	public SwitchAnswer(CommandParameters params) {
		super(params);
	}

	@Override
	public void execute() {
		if (args.size() < 2) {
			channel.sendMessage("`" + prefix + "switchanswer numero1 numero2 ....`").queueAfter(20, TimeUnit.MILLISECONDS);
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
			for (int iterator = 1; iterator < args.size(); iterator++) {
				requete = "SELECT * FROM Questions WHERE id_server = " + guild.getId() + " AND numero_question = " + args.get(iterator) + ";";
				System.out.println("SQL : " + requete);
				res = req.request(requete);
				if (!res.next()) {
					res.close();
					continue;
				}
				int reponse = res.getInt("reponse");
				res.close();
				reponse = 1 - reponse;
				requete = "UPDATE Questions SET reponse = " + String.valueOf(reponse) + " WHERE id_server = " + guild.getId() + " AND numero_question = " + args.get(iterator) + ";";
				System.out.println("SQL : " + requete);
				req.update(requete);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		channel.sendMessage("Les réponses ont été inversées!").queueAfter(20, TimeUnit.MILLISECONDS,
			msg -> {
				msg.delete().queueAfter(5, TimeUnit.SECONDS);
			}
		);
	}

}
