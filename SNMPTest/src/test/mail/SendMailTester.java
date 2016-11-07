/**
 * 
 */
package test.mail;
import org.apache.commons.mail.*;
/**
 * @author sung053
 *
 */
public class SendMailTester {

	public void sendMail() throws EmailException{
	  // Create the attachment
	  EmailAttachment attachment = new EmailAttachment();
	  attachment.setPath("mypictures/john.jpg");
	  attachment.setDisposition(EmailAttachment.ATTACHMENT);
	  attachment.setDescription("Picture of John");
	  attachment.setName("John");

	  // Create the email message
//	  MultiPartEmail email = new MultiPartEmail();
//	  email.setHostName("mail.myserver.com");
//	  email.addTo("jdoe@somewhere.org", "John Doe");
//	  email.setFrom("me@apache.org", "Me");
//	  email.setSubject("The picture");
//	  email.setMsg("Here is the picture you wanted");
	  
	  MultiPartEmail email = new MultiPartEmail();
	  
	  email.setHostName("smtp.naver.com");
	  email.setSmtpPort(465);
	  email.setSSL(true);
	  //email.setStartTLSEnabled(true);
	  email.setAuthentication("darklune", "vksXKwl053");
	  
	  email.addTo("sung053@takis.co.kr", "LSJ");
	  email.setFrom("darklune@naver.com", "Me");
	  email.setSubject("The Test");
	  email.setMsg("Here is the picture you wanted");

	  // add the attachment
	  //email.attach(attachment);

	  // send the email
	  email.send();
	}
	public static void main(String args[]){
		SendMailTester testMailer = new SendMailTester();
		try {
			testMailer.sendMail();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print("send success!");
	}
}
