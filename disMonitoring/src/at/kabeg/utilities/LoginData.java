package at.kabeg.utilities;

public class LoginData {
	
	private static String PASSWORD = "f2bae23f32fd19c67df857ed29a5dac3";
	private static String USERNAME = "svc_dis";
	private static String ADMIN = "admin";
	private static String ADMIN_PASSWORD = "04d856522da0e60965e147aaa94ff4cb";
	private static LoginData instance = null;
	
	private LoginData(){}
	
	public static LoginData getInstance(){
		if(instance==null){
			instance = new LoginData();
		}
		return instance;
	}
	
	public boolean checkLogin(String username, String password){
		if(username.equals(USERNAME) && password.equals(PASSWORD)){
			return true;
		} else if(username.equals(ADMIN) && password.equals(ADMIN_PASSWORD)) {
			return true;
		}
		return false;
	}
	
}
