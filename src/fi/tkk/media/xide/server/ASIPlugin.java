package fi.tkk.media.xide.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.auth.InvalidCredentialsException;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.json.JSONObject;

public class ASIPlugin {

	private static final String cosAddr = "http://cos.sizl.org";

	/**
	 * Method to Authenticate user in ASI
	 * 
	 * @param appName
	 *            Name of the application in ASI
	 * @param appPassword
	 *            Password of the specified application in ASI
	 * @param username
	 *            Username of the user wish to login
	 * @param password
	 *            Password of the user wish to login
	 * @return Map containing the Cookie to identify current ASI session and the
	 *         id of the user in ASI. Keys are named cookie and id. Returns null
	 *         if the authentication fails.
	 */
	static public Map<String, String> login(String appName, String appPassword,
			String username, String password) {
		try {
//			System.out.println("!!going to test " + username + password);
			String line = cosAddr + "/session";
			HttpClient client = new HttpClient();
			PostMethod method = new PostMethod(line);
			NameValuePair[] postParams = new NameValuePair[4];
			postParams[0] = new NameValuePair("session[app_name]", appName);
			postParams[1] = new NameValuePair("session[app_password]",
					appPassword);
			postParams[2] = new NameValuePair("session[username]", username);
			postParams[3] = new NameValuePair("session[password]", password);
			method.setRequestBody(postParams);
			client.executeMethod(method);
			int responseCode = method.getStatusCode();
			if (responseCode == 201) {
				String answer = method.getResponseBodyAsString();
				String cookie = method.getResponseHeader("Set-Cookie")
						.getValue();
				JSONObject obj = new JSONObject(answer);
				String userid = obj.getJSONObject("entry").getString("user_id");
				Map<String, String> result = new HashMap<String, String>();
				result.put("cookie", cookie);
				result.put("id", userid);
				return result;
			} else {
				System.out.println("ASI-error: "
						+ method.getResponseBodyAsString());
			}
			method.releaseConnection();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * Logout from ASI
	 * 
	 * @param cookie
	 *            Cookie which identifies the session
	 * @return true/false
	 */
	static public boolean logout(String cookie) {
		try {
			String line = cosAddr + "/session";
			HttpClient client = new HttpClient();
			DeleteMethod method = new DeleteMethod(line);
			method.addRequestHeader("Cookie", cookie);
			client.executeMethod(method);
			method.releaseConnection();
			if (method.getStatusCode() != 200) {
				System.out.println("ASI-error: "
						+ method.getResponseBodyAsString());
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Change users password in ASI
	 * 
	 * @param cookie
	 *            Cookie which identifies the session
	 * @param newPass
	 *            New password to replace the old one
	 * @return true/false
	 */
	static public boolean changePassword(String cookie, String newPass) {
		try {
			String url = cosAddr + "/people/@me/@self?person%5Bpassword%5D="
					+ newPass;
			HttpClient client = new HttpClient();
			PutMethod method = new PutMethod(url);
			method.addRequestHeader("Cookie", cookie);
			client.executeMethod(method);
			int responseCode = method.getStatusCode();
			method.releaseConnection();
			if (responseCode == 200) {
				return true;
			} else {
				System.out.println("ASI-error: "
						+ method.getResponseBodyAsString());
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Request password recover email for the user with the given email
	 * 
	 * @param appName
	 *            Name of the application in ASI
	 * @param appPassword
	 *            Password of the application in ASI
	 * @param email
	 *            Users email
	 * @return true/false
	 */
	static public boolean recoverPassword(String appName, String appPassword,
			String email) {
		try {
			String sessionAddr = cosAddr + "/session";
			HttpClient client = new HttpClient();
			PostMethod method = new PostMethod(sessionAddr);
			NameValuePair[] sessionParams = new NameValuePair[2];
			sessionParams[0] = new NameValuePair("session[app_name]", appName);
			sessionParams[1] = new NameValuePair("session[app_password]",
					appPassword);
			method.setRequestBody(sessionParams);
			client.executeMethod(method);
			if (method.getStatusCode() != 201) {
				System.out.println("ASI-error: "
						+ method.getResponseBodyAsString());
				method.releaseConnection();
				return false;
			} else {
				String cookie = method.getResponseHeader("Set-Cookie")
						.getValue();
				String line = cosAddr + "/people/recover_password";
				PostMethod post = new PostMethod(line);
				NameValuePair[] postParams = new NameValuePair[1];
				postParams[0] = new NameValuePair("email", email);
				post.addRequestHeader("Cookie", cookie);
				post.setRequestBody(postParams);
				client.executeMethod(post);
				if (post.getStatusCode() == 404) {
					System.out.println("ASI-error: "
							+ post.getResponseBodyAsString());
					post.releaseConnection();
					return false;
				} else if (post.getStatusCode() == 200) {
					post.releaseConnection();
					return true;
				} else {
					System.out.println("ASI-error: "
							+ post.getResponseBodyAsString());
					post.releaseConnection();
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @param appName
	 *            Name of the application in ASI
	 * @param appPassword
	 *            Password of the specified application in ASI
	 * @param username
	 *            Username of the new user
	 * @param password
	 *            Password of the new user
	 * @param email
	 *            Email of the new user
	 * @return true/false
	 * @throws InvalidCredentialsException 
	 */
	public static boolean register(String appName, String appPassword,
			String username, String password, String email) throws InvalidCredentialsException {
		try {
			String sessionAddr = cosAddr + "/session";
			HttpClient client = new HttpClient();
			PostMethod method = new PostMethod(sessionAddr);
			NameValuePair[] sessionParams = new NameValuePair[2];
			sessionParams[0] = new NameValuePair("session[app_name]", appName);
			sessionParams[1] = new NameValuePair("session[app_password]",
					appPassword);
			method.setRequestBody(sessionParams);
			client.executeMethod(method);
			if (method.getStatusCode() != 201) {
				System.out.println("ASI-error: "
						+ method.getResponseBodyAsString());
				method.releaseConnection();
				return false;
			} else {
				String cookie = method.getResponseHeader("Set-Cookie")
						.getValue();
				String line = cosAddr + "/people";
				PostMethod post = new PostMethod(line);
				NameValuePair[] postParams = new NameValuePair[4];
				postParams[0] = new NameValuePair("person[consent]", "FI1");
				postParams[1] = new NameValuePair("person[email]", email);
				postParams[2] = new NameValuePair("person[username]", username);
				postParams[3] = new NameValuePair("person[password]", password);
				post.addRequestHeader("Cookie", cookie);
				post.setRequestBody(postParams);
				client.executeMethod(post);
				if (post.getStatusCode() != 201) {
					System.out.println("ASI-error: "
							+ post.getResponseBodyAsString());
					post.releaseConnection();
					if (post.getResponseBodyAsString().contains("Username")) {
						throw new InvalidCredentialsException(
								"Username already in use");
					}
					if (post.getResponseBodyAsString().contains("Email")) {
						throw new InvalidCredentialsException(
								"Email already in use");
					}
					return false;
				} else {
					post.releaseConnection();
					return true;
				}
			}
		} catch (InvalidCredentialsException e) {
			throw e; 
		}
		  catch (Exception e) {
		  e.printStackTrace();
		  return false;
		}
	}

	/**
	 * Gets details of the user from ASI. Tries to get name (combined together).
	 * If these are empty, gets username value.
	 * 
	 * @param appName
	 *            Name of the application in ASI
	 * @param appPassword
	 *            Password of the specified application in ASI
	 * @param userID
	 *            user_id of the user in ASI
	 * @return Map containing user details (pairs name-value)
	 */
	public static Map<String, String> getUserFullName(String userID,
			String cookie) {
		try {
			HttpClient client = new HttpClient();

			String url = cosAddr + "/people/" + userID + "/@self";
			GetMethod get = new GetMethod(url);
			get.addRequestHeader("Cookie", cookie);
			client.executeMethod(get);

			int responseCode = get.getStatusCode();
			if (responseCode == 200) {
				String answer = get.getResponseBodyAsString();
				JSONObject obj = new JSONObject(answer);
				String name = obj.getJSONObject("entry").optString("name");

				if (!name.equals("null")) {
					name = obj.getJSONObject("entry").getJSONObject("name").optString("unstructured");
					if (name.equals("null")) {
						name = obj.getJSONObject("entry").getJSONObject("name").optString("given_name");
					}
					if (name.equals("null")) {
						name = obj.getJSONObject("entry").getJSONObject("name").optString("family_name");
					}
					if (name.equals("null")) {
						name = obj.getJSONObject("entry").optString("username");
					}
				}
				else {
					name = obj.getJSONObject("entry").optString("username");
				}
				
				Map<String, String> result = new HashMap<String, String>();
				result.put("name", name);
				get.releaseConnection();
				return result;
			} else {
				get.releaseConnection();
				System.out.println("ASI-error: "
						+ get.getResponseBodyAsString());
				return null;
			}
			// }
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
