package trunk;
import ru.ifmo.enf.micelius.core.Bolet;
import ru.ifmo.enf.micelius.core.InnerRequest;
import ru.ifmo.enf.micelius.core.InnerResponse;
import java.util.Collection;

/**
 * User: Bogdanov Kirill
 * Date: 11.03.12
 * Time: 14:01
 */
public class WakeMeUpBolet implements Bolet {
    public void process(final InnerRequest request, final InnerResponse response) {
        try {
            SettingsManager sm = new SettingsManager();
            if (request.getParameter("action").equals("rings")) {
                long userId = Long.parseLong(request.getParameter("userId"));
                String day = request.getParameter("day");
                String parity = request.getParameter("parity");
                response.addObject("rings", sm.getRings(userId, day, parity));
            }
            if (request.getParameter("action").equals("schedule")) {
                long userId = Long.parseLong(request.getParameter("userId"));
                String day = request.getParameter("day");
                String parity = request.getParameter("parity");
                Collection<Collection<String>> schedule = sm.getSchedule(userId, day, parity);
                int cnt = 0;
                for (Collection<String> note : schedule) {
                    response.addObject(cnt + "", note);
                    cnt++;
                }
            }
            if (request.getParameter("action").equals("getSettings")) {
                long userId = Long.parseLong(request.getParameter("userId"));
                long id = Long.parseLong(request.getParameter("id"));
                response.addObject("settings", sm.getSettings(userId, id));
            }
            if (request.getParameter("action").equals("setSettings")) {
                long userId = Long.parseLong(request.getParameter("userId"));
                long id = Long.parseLong(request.getParameter("id"));
                String skip = request.getParameter("skip");
                String wakeTime = request.getParameter("wakeTime");
                String message = request.getParameter("message");
                int turn = Integer.parseInt(request.getParameter("turn"));
                sm.updateSettings(userId, id, skip, wakeTime, message, turn);
            }
        } catch (Exception ex) {
            response.addError("Exception", 9001);
            ex.printStackTrace();
        }
    }
}
