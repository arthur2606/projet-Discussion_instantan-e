package clientserveur2;

public class Message  {
	
	private String msg;
	private String loginD; //login de celui qui envoie le message
	
	public Message(String loginD, String  msg)  {
		
		this.msg = msg;
		this.loginD = loginD;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getLoginD() {
		return loginD;
	}

	public void setLoginD(String loginD) {
		this.loginD = loginD;
	}

	
	
	

}
