package hello;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * A simple configuration for your Spring application.<p/> 
 * [N]:config]:client - The {@code @ConfigurationProperties} annotation selects all the {@code example}-prefixed properties from the <em>Spring Cloud Configuration Server</em> and injects them into the name-matching attributes of the annoted class. Specifically, it'll fetch {@code example.username} from the <em>Vault</em> and map it to the {@code MyConfiguration#username}.<p/>
 * @author Mark Paluch
 */
@ConfigurationProperties("example")
public class MyConfiguration {

	private String username;

	private String password;

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
}
