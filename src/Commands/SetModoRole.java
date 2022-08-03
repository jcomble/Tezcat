package Commands;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import Classes.CommandParameters;
import Classes.DiscordCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.RestAction;

public class SetModoRole extends DiscordCommand {

	public SetModoRole(CommandParameters params) {
		super(params);
	}
	
	private boolean is_role(String arg) {
		if (arg.length() > 4 && arg.charAt(0) == '<' && arg.charAt(1) == '@' && arg.charAt(2) == '&' && arg.charAt(arg.length() - 1) == '>') {
			String id = arg.substring(3, arg.length() - 1);
			int length = id.length();
			for (int iterator = 0; iterator < length; iterator++) {
				char character = id.charAt(iterator);
				if ('0' > character || '9' < character) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void execute() {
		if (args.size() != 2) {
			channel.sendMessage("`" + prefix + "setmodorole @role` seulement!").queueAfter(20, TimeUnit.MILLISECONDS);
			return;
		}
		RestAction<Member> recherche_membre = guild.retrieveMemberById(user.getId());
		Member member = recherche_membre.completeAfter(20, TimeUnit.MILLISECONDS); 
		if (!member.hasPermission(Permission.ADMINISTRATOR)) {
			channel.sendMessage("Tu n'es pas administrateur!").queueAfter(20, TimeUnit.MILLISECONDS);
			return;
		}
		String role = args.get(1);
		if (!is_role(role)) {
			channel.sendMessage(role + " n'est pas un rôle !").queueAfter(20, TimeUnit.MILLISECONDS);
			return;
		}
		String id_role = role.substring(3, role.length() - 1);
		Role modo_role = guild.getRoleById(id_role);
		if (modo_role == null) {
			channel.sendMessage(role + " n'est pas un rôle de ce serveur").queueAfter(20, TimeUnit.MILLISECONDS);
			return;
		}
		try {
			String requete = "DELETE FROM Modos WHERE id_server = " + guild.getId() + ";";
			System.out.println("SQL : " + requete);
			req.update(requete);
			requete = "INSERT INTO Modos VALUES (" + guild.getId() + ", " + id_role + ");";
			System.out.println("SQL : " + requete);
			req.update(requete);
			channel.sendMessage("Désormais le rôle " + role + " sera le rôle pour modérer le questionnaire!").queueAfter(20, TimeUnit.MILLISECONDS);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
