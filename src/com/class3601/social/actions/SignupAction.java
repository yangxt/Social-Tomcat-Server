package com.class3601.social.actions;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;
import com.class3601.social.common.MessageStore;
import com.class3601.social.models.Krypto;
import com.class3601.social.models.User;
import com.class3601.social.persistence.HibernateUserManager;
import com.opensymphony.xwork2.ActionSupport;

public class SignupAction extends ActionSupport implements ServletRequestAware {

	private static final long serialVersionUID = 1L;
	private static String PARAMETER_1 = "id";
	private static String PARAMETER_2 = "email";
	private static String PARAMETER_3 = "password";
	private static String XML_1 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n\n" + "<signup>\n" + "<result>";
	private static String XML_2 = "</result>\n";
	private static String XML_3 = "<username>";
	private static String XML_4 = "</username>\n" + "<email>";
	private static String XML_5 = "</email>\n" + "<password>";
	private static String XML_6 = "</password>\n" + "<token>";
	private static String XML_7 = "</token>\n" + "<counter>";
	private static String XML_8 = "</counter>\n";
	private static String XML_9 = "</signup>\n";

	private MessageStore messageStore;
	private HttpServletRequest request;
	private Krypto krypto = new Krypto();
	HibernateUserManager uManager = new HibernateUserManager();
	long counter = 0;

	public String execute() throws Exception {
		// HttpServletRequest request = ServletActionContext.getRequest();
		// preferred method is to implement ServletRequestAware interface
		// http://struts.apache.org/2.0.14/docs/how-can-we-access-the-httpservletrequest.html

		//http://localhost:8080/social/initial?parameter1=dog&parameter2=cat
		//http://localhost:8080/social/initial?parameter1=dog&parameter2=error
		String id = getServletRequest().getParameter(PARAMETER_1);
		String email = getServletRequest().getParameter(PARAMETER_2);
		String password = getServletRequest().getParameter(PARAMETER_3);

		User existingUser = uManager.getUserById(id);
		User newUser = new User();

		messageStore = new MessageStore();

		// Error checking parameters are set properly
		if (id == null || email == null || password == null || existingUser != null) {
			//addActionError("Danger! Danger Will Robinson! ID or Password field not set correctly.");

			messageStore.appendToMessage(XML_1);
			messageStore.appendToMessage("fail");
			messageStore.appendToMessage(XML_2);
			messageStore.appendToMessage(XML_9);

			System.out.println("========================- Signup -========================");
			System.out.println(messageStore.getMessage());
			System.out.println("==========================================================");
			return "success";
		}
		
		newUser.setId(id);
		newUser.setEmail(email);
		String encryptedPassword = krypto.encrypt(password);
		newUser.setPassword(encryptedPassword);
		newUser.setCounter(0);
		uManager.add(newUser);

		messageStore.appendToMessage(XML_1);
		messageStore.appendToMessage("success");
		messageStore.appendToMessage(XML_2);
		messageStore.appendToMessage(XML_3);
		messageStore.appendToMessage(id);
		messageStore.appendToMessage(XML_4);
		messageStore.appendToMessage(email);
		messageStore.appendToMessage(XML_5);
		messageStore.appendToMessage("SECRET");
//		messageStore.appendToMessage(user.getToken());
		messageStore.appendToMessage(XML_6);
//		messageStore.appendToMessage("" + user.getCounter());
		messageStore.appendToMessage(XML_7);
		messageStore.appendToMessage(XML_8);
		messageStore.appendToMessage(XML_9);

		System.out.println("========================- Signup -========================");
		System.out.println(messageStore.getMessage());
		System.out.println("==========================================================");
		return "success";
	}

	public MessageStore getMessageStore() {
		return messageStore;
	}

	public void setMessageStore(MessageStore messageStore) {
		this.messageStore = messageStore;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	private HttpServletRequest getServletRequest() {
		return request;
	}

}
