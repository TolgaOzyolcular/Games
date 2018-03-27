package JavaFXGame;

public class User {
	private String username;
	private String password;
	private double overall_score;
	private double credit;
	
	
	
	public User() {}
	
	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	public User(String username, String password, double overall_score, double credit) {
		super();
		this.username = username;
		this.password = password;
		this.overall_score = overall_score;
		this.credit = credit;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public double getOverall_score() {
		return overall_score;
	}
	public void setOverall_score(double overall_score) {
		this.overall_score = overall_score;
	}
	public double getCredit() {
		return credit;
	}
	public void setCredit(double credit) {
		this.credit = credit;
	}
	
	
	

}
