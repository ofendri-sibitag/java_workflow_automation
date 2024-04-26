import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CounterHandler {

    private static final String JOB_DESCRIPTION = System.getenv("JOB_DESCRIPTION");
    private static final String JOB = System.getenv("JOB");
    private static final String INSTANCE = System.getenv("INSTANCE");
    private static final String METRIC_NAME = "my_metric_count";
    private static final String SRV_URL = System.getenv("URL");

    public static void sendEvent() {
        updateFile();
        try {
            BufferedReader br = new BufferedReader(new FileReader("/app/exec_count.txt"));
            String counter = br.readLine().trim();
            br.close();

            String fetchWorkTimeUp = "# HELP " + METRIC_NAME + " " + JOB_DESCRIPTION +
                    "\n# TYPE " + METRIC_NAME + " counter" +
                    "\n" + METRIC_NAME + " " + counter;

            String URI = SRV_URL + "/metrics/job/" + JOB + "/instance/" + INSTANCE;
            String metrics = fetchWorkTimeUp + "\n";

            System.out.println("Sending counter request...");
            URL url = new URL(URI);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setDoOutput(true);
            conn.getOutputStream().write(metrics.getBytes());
            conn.getInputStream();
            System.out.println("Counter request sent!");
        } catch (IOException e) {
            System.out.println("Counter request failed : " + e.getMessage());
        }
        System.out.println("--------------");
    }

    public static void updateFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("/app/exec_count.txt"));
            String firstLine = br.readLine().trim();
            br.close();

            if (firstLine.matches("\\d+")) {
                int counter = Integer.parseInt(firstLine);
                counter++;
                FileWriter fw = new FileWriter("/app/exec_count.txt");
                fw.write((Integer.toString(counter)));
                fw.close();
                System.out.println("File updated!");
            } else {
                System.out.println("Failed to update file!");
            }
        } catch (IOException e) {
            System.out.println("Can't find counter file : " + e.getMessage());
        }
    }
}
