package Classes;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLRequester {

	private static Connection con;
	
	private void getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:src/Tezcat.db");
		initialise();
	}
	
	public SQLRequester() throws ClassNotFoundException, SQLException {
		getConnection();
	}
	
	public ResultSet request(String request) throws SQLException, ClassNotFoundException {
		if (con == null) {
			getConnection();
		}
		Statement state = con.createStatement();
		ResultSet res = state.executeQuery(request);
		return res;
	}
	
	public void update(String request) throws SQLException {
		PreparedStatement prep = con.prepareStatement(request);
		prep.execute();
		prep.close();
	}
	
	private void initialise_prefixes(DatabaseMetaData dbm) throws SQLException {
		ResultSet tables = dbm.getTables(null, null, "Prefixes", null);
		if (!tables.next()) {
		update("CREATE TABLE \"Prefixes\" (\r\n"
				+ "	\"id_server\"	INTEGER NOT NULL,\r\n"
				+ "	\"prefix\"	TEXT NOT NULL\r\n"
				+ ");");
		}
	}
	
	private void initialise_colors(DatabaseMetaData dbm) throws SQLException {
		ResultSet tables = dbm.getTables(null, null, "Colors", null);
		if (!tables.next()) {
		update("CREATE TABLE \"Colors\" (\r\n"
				+ "	\"id_server\"	INTEGER NOT NULL,\r\n"
				+ "	\"color\"	INTEGER NOT NULL DEFAULT 16711680\r\n"
				+ ");");
		}
	}
	
	private void initialise_questions(DatabaseMetaData dbm) throws SQLException {
		ResultSet tables = dbm.getTables(null, null, "Questions", null);
		if (!tables.next()) {
		update("CREATE TABLE \"Questions\" (\r\n"
				+ "	\"id_server\"	INTEGER NOT NULL,\r\n"
				+ "	\"question\"	TEXT NOT NULL,\r\n"
				+ "	\"reponse\"	INTEGER NOT NULL,\r\n"
				+ "	\"numero_question\"	INTEGER NOT NULL\r\n"
				+ ");");
		}
	}
	
	private void initialise_sondage(DatabaseMetaData dbm) throws SQLException {
		ResultSet tables = dbm.getTables(null, null, "Modos", null);
		if (!tables.next()) {
		update("CREATE TABLE \"Sondage\" (\r\n"
				+ "	\"Question\"	TEXT NOT NULL,\r\n"
				+ "	\"id_serveur\"	INTEGER NOT NULL,\r\n"
				+ "	\"rep1\"	TEXT,\r\n"
				+ "	\"rep2\"	TEXT,\r\n"
				+ "	\"rep3\"	TEXT,\r\n"
				+ "	\"rep4\"	TEXT,\r\n"
				+ "	\"rep5\"	TEXT,\r\n"
				+ "	\"rep6\"	TEXT,\r\n"
				+ "	\"rep7\"	TEXT,\r\n"
				+ "	\"rep8\"	TEXT,\r\n"
				+ "	\"rep9\"	TEXT,\r\n"
				+ "	\"rep10\"	TEXT,\r\n"
				+ "	\"rep11\"	TEXT,\r\n"
				+ "	\"rep12\"	TEXT,\r\n"
				+ "	\"rep13\"	TEXT,\r\n"
				+ "	\"rep14\"	TEXT,\r\n"
				+ "	\"rep15\"	TEXT,\r\n"
				+ "	\"rep16\"	TEXT,\r\n"
				+ "	\"rep17\"	TEXT,\r\n"
				+ "	\"rep18\"	TEXT,\r\n"
				+ "	\"rep19\"	TEXT,\r\n"
				+ "	\"rep20\"	TEXT\r\n"
				+ ");");
		}
	}
	
	private void initialise_embedquestions(DatabaseMetaData dbm) throws SQLException {
		ResultSet tables = dbm.getTables(null, null, "EmbedQuestions", null);
		if (!tables.next()) {
		update("CREATE TABLE \"EmbedQuestions\" (\r\n"
				+ "	\"id_server\"	INTEGER NOT NULL,\r\n"
				+ "	\"id_message\"	INTEGER NOT NULL,\r\n"
				+ "	\"page\"	INTEGER NOT NULL\r\n"
				+ ");");
		}
	}
	
	private void initialise_game(DatabaseMetaData dbm) throws SQLException {
		ResultSet tables = dbm.getTables(null, null, "Game", null);
		if (!tables.next()) {
		update("CREATE TABLE \"Game\" (\r\n"
				+ "	\"id_server\"	INTEGER NOT NULL,\r\n"
				+ "	\"id_message\"	INTEGER NOT NULL,\r\n"
				+ "	\"numero_question\"	INTEGER NOT NULL\r\n"
				+ ");");
		}
	}
	
	private void initialise_reponsesdonnees(DatabaseMetaData dbm) throws SQLException {
		ResultSet tables = dbm.getTables(null, null, "ReponsesDonnees", null);
		if (!tables.next()) {
		update("CREATE TABLE \"ReponsesDonnees\" (\r\n"
				+ "	\"id_server\"	INTEGER NOT NULL,\r\n"
				+ "	\"id_membre\"	INTEGER NOT NULL,\r\n"
				+ "	\"reponse\"	INTEGER NOT NULL,\r\n"
				+ "	\"numero_question\"	INTEGER NOT NULL\r\n"
				+ ");");
		}
	}
	
	private void initialise_modos(DatabaseMetaData dbm) throws SQLException {
		ResultSet tables = dbm.getTables(null, null, "Modos", null);
		if (!tables.next()) {
		update("CREATE TABLE \"Modos\" (\r\n"
				+ "	\"id_server\"	INTEGER NOT NULL,\r\n"
				+ "	\"id_modo\"	INTEGER NOT NULL\r\n"
				+ ");");
		}
	}
	
	private void initialise() throws SQLException {
		DatabaseMetaData dbm = con.getMetaData();
		initialise_prefixes(dbm);
		initialise_colors(dbm);
		initialise_questions(dbm);
		initialise_embedquestions(dbm);
		initialise_sondage(dbm);
		initialise_game(dbm);
		initialise_reponsesdonnees(dbm);
		initialise_modos(dbm);
	}
}
