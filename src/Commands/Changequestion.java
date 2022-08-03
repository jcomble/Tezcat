package Commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import Classes.CommandParameters;
import Classes.DiscordCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class Changequestion extends DiscordCommand {

	public Changequestion(CommandParameters params) {
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
			channel.sendMessage("`" + prefix + "changequestion numero_question \"nouvelle question\"` seulement!").queueAfter(20, TimeUnit.MILLISECONDS);
			return;
		}
		ResultSet res;
		String string_numero = "";
		int numero = 0;
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
			string_numero = args.get(1);
			numero = Integer.parseInt(string_numero);
			if (numero < 1) {
				channel.sendMessage(string_numero + "n'est pas un nombre entier supérieur à 1!").queueAfter(20, TimeUnit.MILLISECONDS);
				return;
			}
			requete = "UPDATE Questions SET question = '" + correct_question(args.get(2)) + "' WHERE id_server = " + guild.getId() + " AND numero_question = " + String.valueOf(numero) + ";";
			System.out.println("SQL : " + requete);
			req.update(requete);
			channel.sendMessage("La question a bien été changée!").queueAfter(20, TimeUnit.MILLISECONDS,
				msg -> {
					msg.delete().queueAfter(5, TimeUnit.SECONDS);
				}
			);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();	
		} catch (NumberFormatException e) {
			channel.sendMessage(string_numero + "n'est pas un nombre entier!").queueAfter(20, TimeUnit.MILLISECONDS);
			return;
		} catch (SQLException e) {
			channel.sendMessage("Erreur numéro question").queueAfter(20, TimeUnit.MILLISECONDS);
			e.printStackTrace();
		}
	}
}