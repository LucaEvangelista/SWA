package eva.luca.soccorso_Web.utility;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import eva.luca.soccorso_Web.models.Request;

public class UtilityMethods {
	private static final String emailText = "per poter procedere alla risoluzione della sua richiesta clicchi qui: ";
	private static final String path = "/Users/lucaevangelista/Documents/Università/webengineering/eclipse-work-space/soccorso_Web_SWA/src/tempFile/confermaMail.txt";
	
	public static void sendEmailWithCodes(Request richiesta) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))){
			
            writer.write("[ " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + " ]  " + richiesta.getMailPersona() + ":");
            writer.newLine();
            writer.write("--------------------------------- SoccorsoWeb ----------------------------------");
            writer.newLine();
            writer.write("Gentile " + richiesta.getNomePersona() + ",");
            writer.newLine();
            writer.write(emailText);

            writer.newLine();

            writer.write("http://localhost:8080/soccorso_Web_SWA/viste/NuovaRichiesta.html?uuid=" + richiesta.getUuid());
            writer.newLine();
            writer.write("grazie.");

            writer.newLine();
            writer.write("--------------------------------- SoccorsoWeb ----------------------------------");
            writer.newLine();
            writer.newLine();
            writer.newLine();
            writer.newLine();
            writer.newLine();
            writer.newLine();
            writer.newLine();
            writer.newLine();
			
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
}
