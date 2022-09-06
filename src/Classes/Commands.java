package Classes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import Commands.ChangeColor;
import Commands.Changeprefix;
import Commands.Changequestion;
import Commands.Creategame;
import Commands.DownloadResults;
import Commands.Help;
import Commands.Play;
import Commands.Resetgame;
import Commands.SetModoRole;
import Commands.ShowQuestions;
import Commands.SwitchAnswer;
import Commands.Test;
import Commands.Top5;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.Emoji.Type;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {
	private SQLRequester req;
	
	private ArrayList<String> getargs(String content) {
		ArrayList<String> args = new ArrayList<String>();
		String tmp_string = "";
		int length = content.length();
		char last_char= ' ', actual_char = ' ';
		boolean guillemet_ouvert = false;
		for (int iterator = 0; iterator < length; iterator++) {
			last_char = actual_char;
			actual_char = content.charAt(iterator);
			if (!guillemet_ouvert && (actual_char == ' ' || actual_char == '\n')) {
				if (!tmp_string.equals("")) {
					args.add(tmp_string);
					tmp_string = "";
				}
			} else if (last_char != '\\' && actual_char == '\"' && !guillemet_ouvert) {
				guillemet_ouvert = true;
			} else if (last_char != '\\' && actual_char == '\"' && guillemet_ouvert) {
				guillemet_ouvert = false;
				if (!tmp_string.equals("")) {
					args.add(tmp_string);
					tmp_string = "";
				}
			} else {
				tmp_string = tmp_string + actual_char;
			}
		}
		if (!tmp_string.equals("")) {
			args.add(tmp_string);
			tmp_string = "";
		}
		return args;
	}
	
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
		Message message = event.getMessage();
        User user = message.getAuthor();
        if (user.isBot()) {
			return;
		}
		MessageChannel channel = event.getChannel();
		Guild guild = event.getGuild();
        String content = message.getContentRaw();
        ArrayList<String> args = getargs(content);
        char prefix = '%';
        int color = 16711680;
        try {
        	String requete = "SELECT * FROM Prefixes WHERE id_server = " + guild.getId() + ";";
        	System.out.println("SQL : " + requete);
			ResultSet res = req.request(requete);
			if (res.next()) {
				prefix = res.getString("prefix").charAt(0);
			}
			res.close();
			requete = "SELECT * FROM Colors WHERE id_server = " + guild.getId() + ";";
        	System.out.println("SQL : " + requete);
			res = req.request(requete);
			if (res.next()) {
				color = res.getString("color").charAt(0);
			}
			res.close();
		} catch (ClassNotFoundException | SQLException e1) {
		}
        
		if (args.size() == 0 || args.get(0).charAt(0) != prefix) {
			return;
		}
		String suffixe = args.get(0).substring(1).toLowerCase();
		CommandParameters params = new CommandParameters(channel, args, req, user, guild, message, prefix, color);
		Class<?>[] list = {
			Test.class,
			Help.class,
			Changeprefix.class,
			Creategame.class,
			Resetgame.class,
			ShowQuestions.class,
			Changequestion.class,
			SwitchAnswer.class,
			Play.class,
			DownloadResults.class,
			Top5.class,
			SetModoRole.class,
			ChangeColor.class
		};
		ArrayList<String> list_name = new ArrayList<String>();
		for (Class<?> classe : list) {
			list_name.add(classe.getSimpleName().toLowerCase());
		}
		if (list_name.contains(suffixe)) {
			try {
				Constructor<?> constructeur = list[list_name.indexOf(suffixe)].getConstructor(CommandParameters.class);
				Object me_command = constructeur.newInstance(params);
				Method method = me_command.getClass().getMethod("execute", (Class<?>[]) null);
				method.invoke(me_command);
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
			return;
		}
    }
	
	public Commands(SQLRequester req) {
		this.req = req;
	}
	
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		User user = event.getUser();
		if (user.isBot()) {
			return;
		}
		Guild guild = event.getGuild();
		Message message = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
		MessageReaction reaction = event.getReaction();
		Emoji emoji = reaction.getEmoji();
		String requete = "SELECT * FROM Colors WHERE id_server = " + guild.getId() + ";";
    	System.out.println("SQL : " + requete);
    	try {
			ResultSet res = req.request(requete);
			int color = 16711680;
			if (res.next()) {
				color = res.getString("color").charAt(0);
			}
			if (emoji.getType().equals(Type.UNICODE) && (emoji.getFormatted().equals("⬅️") || emoji.getFormatted().equals("➡️"))) {
				requete = "SELECT * FROM EmbedQuestions WHERE id_message = " + message.getId() + ";";
				System.out.println("SQL : " + requete);
				res = req.request(requete);
				int page = 1;
				if (res.next()) {
					page = res.getInt("page");
					res.close();
				} else {
					res.close();
					return;
				}
				requete = "SELECT * FROM Questions WHERE id_server = " + guild.getId() + ";";
				System.out.println("SQL : " + requete);
				res = req.request(requete);
				int nombre_questions = 0;
				while (res.next()) {
					nombre_questions += 1;
				}
				res.close();
				int max_page = Math.max(1, (nombre_questions + 4) / 5);
				if (emoji.getFormatted().equals("⬅️")) {
					page = Math.max(1 , page - 1);
				} else {
					page = Math.min(page + 1 , max_page);
				}
				
				res.close();
				EmbedQuestions embedquestions = new EmbedQuestions(req, guild, page, color);
				EmbedBuilder embed = embedquestions.getEmbed();
				message.editMessageEmbeds(embed.build()).completeAfter(20, TimeUnit.MILLISECONDS);
				requete = "UPDATE EmbedQuestions SET page = " + String.valueOf(page) + " WHERE id_message = " + message.getId() + ";";
				System.out.println("SQL : " + requete);
				req.update(requete);
				reaction.removeReaction(user).completeAfter(20, TimeUnit.MILLISECONDS);
			}
			if (emoji.getType().equals(Type.UNICODE) && (emoji.getFormatted().equals("✅") || emoji.getFormatted().equals("❌") || emoji.getFormatted().equals("↘️"))) {
				requete = "SELECT * FROM Game WHERE id_server = " + guild.getId() + " AND id_message = " + message.getId() + ";";
				System.out.println("SQL : " + requete);
				res = req.request(requete);
				if (!res.next()) {
					res.close();
					return;
				}
				int numero_question = res.getInt("numero_question"); 
				if (emoji.getFormatted().equals("✅")) {
					res.close();
					requete = "SELECT * FROM ReponsesDonnees WHERE id_server = " + guild.getId() + " AND id_membre = " + user.getId() + " AND numero_question = " + String.valueOf(numero_question) + ";";
					System.out.println("SQL : " + requete);
					res = req.request(requete);
					if (!res.next()) {
						res.close();
						requete = "INSERT INTO ReponsesDonnees VALUES (" + guild.getId() + ", " + user.getId() + ", 1, " + String.valueOf(numero_question) + ");";
						System.out.println("SQL : " + requete);
						req.update(requete);
						EmbedGame game = new EmbedGame(guild, numero_question, req, color);
						EmbedBuilder embed = game.getEmbed();
						requete = "SELECT Count(*) AS compt FROM ReponsesDonnees WHERE id_server = " + guild.getId() + " AND numero_question = " + String.valueOf(numero_question) + ";";
						res = req.request(requete);
						embed.setFooter(res.getString("compt") + " personnes ont répondu!");
						message.editMessageEmbeds(embed.build()).queueAfter(20, TimeUnit.MILLISECONDS);
					} else {
						res.close();
					}
					message.removeReaction(Emoji.fromFormatted("✅"), user).completeAfter(20, TimeUnit.MILLISECONDS);
				} else if (emoji.getFormatted().equals("❌")) {
					res.close();
					requete = "SELECT * FROM ReponsesDonnees WHERE id_server = " + guild.getId() + " AND id_membre = " + user.getId() + " AND numero_question = " + String.valueOf(numero_question) + ";";
					System.out.println("SQL : " + requete);
					res = req.request(requete);
					if (!res.next()) {
						res.close();
						requete = "INSERT INTO ReponsesDonnees VALUES (" + guild.getId() + ", " + user.getId() + ", 0, " + String.valueOf(numero_question) + ");";
						System.out.println("SQL : " + requete);
						req.update(requete);
						EmbedGame game = new EmbedGame(guild, numero_question, req, color);
						EmbedBuilder embed = game.getEmbed();
						requete = "SELECT Count(*) AS compt FROM ReponsesDonnees WHERE id_server = " + guild.getId() + " AND numero_question = " + String.valueOf(numero_question) + ";";
						res = req.request(requete);
						embed.setFooter(res.getString("compt") + " personnes ont répondu!");
						message.editMessageEmbeds(embed.build()).queueAfter(20, TimeUnit.MILLISECONDS);
					} else {
						res.close();
					}
					message.removeReaction(Emoji.fromFormatted("❌"), user).completeAfter(20, TimeUnit.MILLISECONDS);
				} else {
					res.close();
					requete = "SELECT * FROM Modos WHERE id_server = " + guild.getId() + ";";
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
					if (role_modo == null || !member.getRoles().contains(role_modo)) {
						return;
					}
					requete = "UPDATE Game SET numero_question = " + String.valueOf(numero_question + 1) + " WHERE id_server = " + guild.getId() + ";";
					System.out.println("SQL : " + requete);
					req.update(requete);
					requete = "SELECT * FROM Questions WHERE id_server = " + guild.getId() + " AND numero_question = " + String.valueOf(numero_question + 1) + ";";
					System.out.println("SQL : " + requete);
					res = req.request(requete);
					message.removeReaction(Emoji.fromFormatted("↘️"), user).completeAfter(20, TimeUnit.MILLISECONDS);;
					if (!res.next()) {
						message.clearReactions().completeAfter(20, TimeUnit.MILLISECONDS);
						requete = "DELETE FROM Game WHERE id_server = " + guild.getId() + ";";
						System.out.println("SQL : " + requete);
						req.update(requete);
					}
					res.close();
					EmbedGame embedgame = new EmbedGame(guild, numero_question + 1, req, color);
					EmbedBuilder embed = embedgame.getEmbed();
					message.editMessageEmbeds(embed.build()).completeAfter(20, TimeUnit.MILLISECONDS);
				}
			}
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public void onGuildJoin(@Nonnull GuildJoinEvent event) {
		try {
			String requete = "INSERT INTO Prefixes VALUES (" + event.getGuild().getId() + ", '%');";
			System.out.println("SQL : " + requete);
			req.update(requete);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onGuildLeave(@Nonnull GuildLeaveEvent event) {
		try {
			String requete = "DELETE FROM Prefixes WHERE id_server = " + event.getGuild().getId() + ";";
			System.out.println("SQL : " + requete);
			req.update(requete);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}