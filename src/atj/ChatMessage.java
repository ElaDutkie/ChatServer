package atj;

public class ChatMessage {
private String user;
private String email;
private String message;

public ChatMessage() {}

public ChatMessage(String user, String email, String message) {
	this.user = user;
	this.email = email;
	this.message = message;
}

public String getUser() {
	return user;
}

public void setUser(String user) {
	this.user = user;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public String getMessage() {
	return message;
}

public void setMessage(String message) {
	this.message = message;
}


@Override
public String toString() {
	return toString().format("[%s] %s", getUser(), getMessage());
}


}
