package database;
import java.sql.*;

public class Database {
	
	private String name;
	Connection c;
	
	public Database(String DBName) {
		this.name = DBName;
		this.c = null;
	}
	
	public boolean connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			this.c = DriverManager.getConnection("jdbc:sqlite:"+this.name);
			this.c.setAutoCommit(false);
		} catch(Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return false;
		}
		return true;
	}
	
	public boolean isConnected() {
		return this.c!=null;
	}
	
	public void disconnect() {
		if(this.c!=null)
		{
			try {
				this.c.close();
			} catch (Exception e) {
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			}
			this.c=null;
		}
	}
	
	public void UpdateDB(String sql) {
		/*
		 * Insert, Delete, Update
		 * */
		if(this.c!=null)
		{
			Statement stmt = null;
			try
			{
				stmt = this.c.createStatement();
				stmt.executeUpdate(sql);
				stmt.close();
				this.c.commit();
			}
			catch( Exception e)
			{
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );		
			}
			//System.out.println("Update was successful");	
		}
	}
	
	public int getLigatureID(String text,int textID) {
		return 0;
	}
	
	
	//Okay I know, that this is a lot of unneccesary code copies, but you cann't just create a getString(sql) method, because it would go against data security
	//If you can make it work, while keep using prepared Statements just let me know.
	public String getText(int LigatureID) {
		String out = "";
		if(this.c!=null) {
			PreparedStatement stmt = null;
			try {				
				stmt = this.c.prepareStatement("SELECT TEXT FROM LIGATURES WHERE LIGATUREID=?;");
				stmt.setInt(1, LigatureID);
				ResultSet rs = stmt.executeQuery();
				
				while(rs.next()) {
					out = rs.getString("TEXT");
					System.out.println("Text: "+rs.getString("TEXT"));
				}
				rs.close();
				stmt.close();
			} catch ( Exception e ) {
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			}
		}
		return out;
	}
	
	public int getTextID(int LigatureID) {
		int out = -1;
		if(this.c!=null) {
			PreparedStatement stmt = null;
			try {				
				stmt = this.c.prepareStatement("SELECT TEXTID FROM LIGATURES WHERE LIGATUREID=?;");
				stmt.setInt(1, LigatureID);
				ResultSet rs = stmt.executeQuery();
				
				while(rs.next()) {
					out = rs.getInt("TEXTID");
					System.out.println("TextID: "+rs.getString("TEXTID"));
				}
				rs.close();
				stmt.close();
			} catch ( Exception e ) {
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			}
		}
		return out;
	}
	
	public String getFileURI(int LigatureID) {
		String out = "";
		if(this.c!=null) {
			PreparedStatement stmt = null;
			try {				
				stmt = this.c.prepareStatement("SELECT FILEURI FROM FILES WHERE LIGATUREID=?;");
				stmt.setInt(1, LigatureID);
				ResultSet rs = stmt.executeQuery();
				
				while(rs.next()) {
					out = rs.getString("FILEURI");
					System.out.println("FileURI: "+rs.getString("FILEURI"));
				}
				rs.close();
				stmt.close();
			} catch ( Exception e ) {
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			}
		}
		return out;
	}
	
	public int getLigatureID(int TextID,String text) {
		int out = -1;
		if(this.c!=null) {
			PreparedStatement stmt = null;
			try {				
				stmt = this.c.prepareStatement("SELECT LIGATUREID FROM LIGATURES WHERE TEXTID=? AND TEXT=?;");
				stmt.setInt(1, TextID);
				stmt.setString(2, text);
				ResultSet rs = stmt.executeQuery();
				
				while(rs.next()) {
					out = rs.getInt("LIGATUREID");
					System.out.println("LigatureID: "+rs.getString("LIGATUREID"));
				}
				rs.close();
				stmt.close();
			} catch ( Exception e ) {
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			}
		}
		return out;
	}
}
