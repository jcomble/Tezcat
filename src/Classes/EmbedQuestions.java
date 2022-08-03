package Classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

public class EmbedQuestions {
	EmbedBuilder embed;
	
	public EmbedQuestions(SQLRequester req, Guild guild, int page) throws ClassNotFoundException, SQLException {
		ResultSet res = req.request("SELECT * FROM Questions WHERE id_server = " + guild.getId() + " ORDER BY numero_question;");
		ArrayList<String> questions = new ArrayList<String>();
		ArrayList<String> reponses = new ArrayList<String>();
		while (res.next()) {
			questions.add(res.getString("question"));
			if (res.getInt("reponse") == 1) {
				reponses.add("oui");
			} else {
				reponses.add("non");
			}
		}
		int tmp_page = Math.max(1, Math.min(page, (questions.size() + 4) / 5));
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Questions");
		System.out.println(tmp_page);
		System.out.println(questions.size());
		for (int i = 0; i < 5; i++) {
			int index = 5 * (tmp_page - 1) + i; 
			if (index < questions.size()) {
				String name = "Question n°" + String.valueOf(index + 1);
				String value = questions.get(index) + "\n" + reponses.get(index);
				embed.addField(name, value, false);
			} else {
				break;
			}
		}
		this.embed = embed;
	}

	public EmbedBuilder getEmbed() {
		return embed;
	}
	
}
