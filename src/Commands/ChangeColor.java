package Commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import Classes.CommandParameters;
import Classes.DiscordCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class ChangeColor extends DiscordCommand {

	private boolean check_hex(String text) {
		int length = text.length();
		for (int iterator = 0; iterator < length; iterator++) {
			char letter = text.charAt(iterator);
			if (letter < '0' || letter > 'f') {
				return false;
			}
		}
		return true;
	}
	
	private int value_hex(String text) {
		int length = text.length();
		int sum = 0;
		for (int iterator = 0; iterator < length; iterator++) {
			sum *= 16;
			sum += text.charAt(length - iterator - 1) - '0';
		}
		return sum;
	}
	
	public ChangeColor(CommandParameters params) {
		super(params);
	}

	@Override
	public void execute() {
		if (args.size() != 2) {
			channel.sendMessage("`" + prefix + "changecolor #HEX` seulement!");
			return;
		}
		String code = args.get(1).toLowerCase();
		if (code.charAt(0) == '#') {
			code = code.substring(1, code.length() - 1);
		}
		if (!check_hex(code)) {
			channel.sendMessage("Mauvais code couleur");
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
			requete = "UPDATE Colors SET color = '" + value_hex(code) + "' WHERE id_server = " + guild.getId() + ";";
			System.out.println("SQL : " + requete);
			req.update(requete);
			channel.sendMessage("Le préfixe a été changé en `" + value_hex(code) + "`").queueAfter(20, TimeUnit.MILLISECONDS);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
}
