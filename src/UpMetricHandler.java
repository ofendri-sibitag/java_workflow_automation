import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpMetricHandler {
    private static final String JOB_DESCRIPTION = System.getenv("JOB_DESCRIPTION");
    private static final String JOB = System.getenv("JOB");
    private static final String INSTANCE = System.getenv("INSTANCE");
    private static final String SRV_URL = System.getenv("URL");

    public static void sendUp() {
        String fetchWorkTimeUp = "# HELP up " + JOB_DESCRIPTION +
                "\n# TYPE up gauge" +
                "\nup 1";

        String URI = SRV_URL + "/metrics/job/" + JOB + "/instance/" + INSTANCE;
        String metrics = fetchWorkTimeUp + "\n";

        System.out.println("Sending up request...");

        try {
            URL url = new URL(URI);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setDoOutput(true);
            conn.getOutputStream().write(metrics.getBytes());
            conn.getInputStream();
            System.out.println("Up request sent!");
        } catch (IOException e) {
            System.out.println("Up request failed: " + e.getMessage());
        }
        System.out.println("--------------");
    }

    public static void main (String[] args) {
        sendUp();
    }
}
