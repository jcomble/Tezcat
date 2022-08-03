package Classes;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

public class EmbedGame {
	private EmbedBuilder embed;
	
	public EmbedGame(Guild guild, int numero_question, SQLRequester req) {
		try {
			ResultSet res = req.request("SELECT * FROM Questions WHERE id_server = " + guild.getId() + " AND numero_question = " + String.valueOf(numero_question) + ";");
			if (res.next()) {
				String question = res.getString("question");
				EmbedBuilder embed = new EmbedBuilder();
				embed.setTitle("Question n°" + String.valueOf(numero_question));
				embed.setDescription(question);
				this.embed = embed;
			} else {
				EmbedBuilder embed = new EmbedBuilder();
				embed.setTitle("Le questionnaire est terminé!");
				embed.setDescription("Laisse les animateurs vérifier les résultats du jeu!");
				this.embed = embed;
			}
			res.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public EmbedBuilder getEmbed() {
		return embed;
	}
	
}
