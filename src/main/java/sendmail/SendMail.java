package sendmail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;

public class SendMail {

    public static void sendEmail(String user, String password, String to, String msg, String subject) {

        try {

            String cmd = "curl -v --url smtps://smtp.gmail.com:465 --ssl-reqd --mail-from %s --mail-rcpt %s -T %s --user %s:%s";

            String path = "/tmp/";
            File f = new File(path);

            File temp = File.createTempFile(Guid.getString(), ".txt", f);

            FileWriter fw = new FileWriter(temp);
            BufferedWriter out = new BufferedWriter(fw);
            out.write("Subject: " + subject + "\n");
            out.write("Content-Type: text/html; charset=utf-8\n");
            out.write("Content-Transfer-Encoding: base64\n");
            out.write(base64(msg.getBytes(StandardCharsets.UTF_8)));

            out.flush();
            out.close();

            StringBuilder command = new StringBuilder();
            command.append(String.format(cmd, user, to, temp.getAbsolutePath(), user, password));

            String ret = Execute.command(command.toString());
            System.out.println(ret);

            temp.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String base64(byte[] value) {
        Base64 base64 = new Base64();
        return new String(base64.encode(value));
    }

}
