package Commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import Classes.CommandParameters;
import Classes.DiscordCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.RestAction;

public class DownloadResults extends DiscordCommand {

	public DownloadResults(CommandParameters params) {
		super(params);
	}

	@Override
	public void execute() {
		if (args.size() != 1) {
			channel.sendMessage("`" + prefix + "donwloadresults` seulement!").queueAfter(20, TimeUnit.MILLISECONDS);
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
			String result = "Classement (id_membre, surnom, score):\n";
			ArrayList<String> liste_ids = new ArrayList<String>();
			ArrayList<String> liste_noms = new ArrayList<String>();
			requete = "SELECT id_membre, COUNT(*) AS bonnes_reponses\n" +
					"FROM Questions, ReponsesDonnees\n" + 
					"WHERE ReponsesDonnees.numero_question = Questions.numero_question\n" +
					"AND ReponsesDonnees.reponse = Questions.reponse\n" +
					"AND ReponsesDonnees.id_server = " + guild.getId() + "\n" +
					"GROUP BY id_membre\n" + 
					"ORDER BY COUNT(*) DESC;";
			System.out.println("SQL : " + requete);
			res = req.request(requete);
			while (res.next()) {
				String id_membre = res.getString("id_membre");
				String nom;
				if (liste_ids.contains(id_membre)) {
					int index = liste_ids.indexOf(id_membre);
					nom = liste_noms.get(index);
				} else {
					liste_ids.add(id_membre);
					RestAction<Member> retrouve_membre = guild.retrieveMemberById(id_membre);
					Member mbr = retrouve_membre.completeAfter(20, TimeUnit.MILLISECONDS);
					nom = mbr.getEffectiveName();
					liste_noms.add(nom);
				}
				result += id_membre + ", " + nom + ", " + res.getString("bonnes_reponses") + "\n";
			}
			res.close();
			result += "\n\nBonnes réponses (id_membre, surnom, numero_question, reponse):\n";
			requete = "SELECT id_membre, ReponsesDonnees.numero_question AS num, ReponsesDonnees.reponse AS rep\n" +
					"FROM Questions, ReponsesDonnees\n" + 
					"WHERE ReponsesDonnees.numero_question = Questions.numero_question\n" +
					"AND ReponsesDonnees.reponse = Questions.reponse\n" +
					"AND ReponsesDonnees.id_server = " + guild.getId() + "\n" +
					"ORDER BY id_membre ASC, num ASC;";
			System.out.println("SQL : " + requete);
			res = req.request(requete);
			while (res.next()) {
				String id_membre = res.getString("id_membre");
				String nom;
				if (liste_ids.contains(id_membre)) {
					int index = liste_ids.indexOf(id_membre);
					nom = liste_noms.get(index);
				} else {
					liste_ids.add(id_membre);
					RestAction<Member> retrouve_membre = guild.retrieveMemberById(id_membre);
					Member mbr = retrouve_membre.completeAfter(20, TimeUnit.MILLISECONDS);
					nom = mbr.getEffectiveName();
					liste_noms.add(nom);
				}
				String booleen = res.getString("num") == "1" ? "Vrai" : "Faux";  
				result += id_membre + ", " + nom + ", " + res.getString("num") + ", " + booleen + "\n";
			}
			res.close();
			result += "\n\nToutes les réponses données (id_membre, surnom, numero_question, reponse):\n";
			requete = "SELECT id_membre, numero_question AS num, reponse AS rep\n" +
					"FROM ReponsesDonnees\n" +
					"WHERE ReponsesDonnees.id_server = " + guild.getId() + "\n" +
					"ORDER BY id_membre ASC, numero_question ASC;";
			System.out.println("SQL : " + requete);
			res = req.request(requete);
			while (res.next()) {
				String id_membre = res.getString("id_membre");
				String nom;
				if (liste_ids.contains(id_membre)) {
					int index = liste_ids.indexOf(id_membre);
					nom = liste_noms.get(index);
				} else {
					liste_ids.add(id_membre);
					RestAction<Member> retrouve_membre = guild.retrieveMemberById(id_membre);
					Member mbr = retrouve_membre.completeAfter(20, TimeUnit.MILLISECONDS);
					nom = mbr.getEffectiveName();
					liste_noms.add(nom);
				}
				String booleen = res.getString("num") == "1" ? "Vrai" : "Faux";  
				result += id_membre + ", " + nom + ", " + res.getString("num") + ", " + booleen + "\n";
			}
			File myfile = new File("Resultats.txt");
			myfile.setWritable(true);
			FileWriter myWriter = new FileWriter("Resultats.txt");
			myWriter.write(result);
			myWriter.close();
			channel.sendFile(myfile).queueAfter(20, TimeUnit.MILLISECONDS);
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
	}
}