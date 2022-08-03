package Commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import Classes.CommandParameters;
import Classes.DiscordCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class Changeprefix extends DiscordCommand {

	public Changeprefix(CommandParameters params) {
		super(params);
	}
	
	@Override
	public void execute() {
		if (args.size() != 2) {
			channel.sendMessage("`" + prefix + "changeprefix prefix` seulement!").queueAfter(20, TimeUnit.MILLISECONDS);
			return;
		}
		String new_prefix = args.get(1);
		if (new_prefix.length() != 1) {
			channel.sendMessage("Le préfixe doit être de longueur 1!").queueAfter(20, TimeUnit.MILLISECONDS);
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
			requete = "UPDATE Prefixes SET prefix = '" + new_prefix + "' WHERE id_server = " + guild.getId() + ";";
			System.out.println("SQL : " + requete);
			req.update(requete);
			channel.sendMessage("Le préfixe a été changé en `" + new_prefix + "`").queueAfter(20, TimeUnit.MILLISECONDS);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
