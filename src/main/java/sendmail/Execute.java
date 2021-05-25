package sendmail;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author dirceubelem
 */
public class Execute {

    private String message = "";
    private Boolean success = false;

    public Execute() {
    }

    public static String command(String cmd) {
        Execute c = new Execute();
        c.commandShell(cmd, true);
        return c.getMessage();
    }

    public static String command(String cmd, boolean result) {
        Execute c = new Execute();
        c.commandShell(cmd, result);
        if (result) {
            return c.getMessage();
        } else {
            return "";
        }
    }

    public void commandShell(String command, boolean result) {
        Process proc = run(command);

        if (result) {
            try {

                if (proc.waitFor() == 0) {
                    success = true;
                }

                if (success) {
                    message = getResult(proc.getInputStream());
                } else {
                    message = getResult(proc.getErrorStream());
                }

            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
                success = false;
            } catch (NullPointerException ex) {
                System.out.println(ex.getMessage());
                success = false;
            }
        }

    }

    public Process run(String command) {
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec(command);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            success = false;
            proc = null;
        }
        return proc;
    }

    private String getResult(InputStream istream) {
        String line = null;
        StringBuffer result = new StringBuffer();
        BufferedReader dataIstream = new BufferedReader(new InputStreamReader(
                istream));

        try {
            while ((line = dataIstream.readLine()) != null) {
                result.append(line + "\n");
            }
            dataIstream.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            success = false;
        }

        if (result != null) {
            return result.toString();
        } else {
            return null;
        }

    }

    public String getMessage() {
        return message;
    }

    public Boolean getSuccess() {
        return success;
    }
}
