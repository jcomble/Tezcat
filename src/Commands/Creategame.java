package Commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import Classes.CommandParameters;
import Classes.DiscordCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class Creategame extends DiscordCommand {
	
	public Creategame(CommandParameters params) {
		super(params);
	}
	
	private String correct_question(String question) {
		String tmp_result = "";
		int length = question.length();
		for (int iterator = 0; iterator < length; iterator++) {
			char tmp_character = question.charAt(iterator);
			if (tmp_character == '\'') {
				tmp_result = tmp_result + '\'';
			}
			tmp_result = tmp_result + tmp_character;
		}
		return tmp_result;
	}
	
	@Override
	public void execute() {
		if (args.size() != 3) {
			channel.sendMessage("`" + prefix + "creategame \"Question de base\" nombre_questions` seulement!").queueAfter(20, TimeUnit.MILLISECONDS);
			return;
		}
		ResultSet res;
		String nombre_string = "";
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
			nombre_string = correct_question(args.get(2));
			int nombre = 0;
			nombre = Integer.parseInt(nombre_string);
			if (nombre > 30 || nombre < 1) {
				channel.sendMessage("Il faut qu'il y ait entre 1 et 30 questions").queueAfter(20, TimeUnit.MILLISECONDS);
				return;
			}
			String question_base = correct_question(args.get(1));
			requete = "DELETE FROM Questions WHERE id_server = " + guild.getId() + ";";
			System.out.println("SQL : " + requete);
			req.update(requete);
			for (int iterator = 0; iterator < nombre; iterator++) {
				requete = "INSERT INTO Questions VALUES (" + guild.getId() + ", '" + question_base + "', 1," + String.valueOf(iterator + 1) + ");";
				System.out.println("SQL : " + requete);
				req.update(requete);
			}
			channel.sendMessage("Le questionnaire a été créé").queueAfter(20, TimeUnit.MILLISECONDS,
				msg -> {
					msg.delete().queueAfter(5, TimeUnit.SECONDS);
				}
			);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e){
			channel.sendMessage(nombre_string + " n'est pas un nombre entier!").queueAfter(20, TimeUnit.MILLISECONDS);
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
