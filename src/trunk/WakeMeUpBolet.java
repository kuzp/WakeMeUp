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
            if (request.getParameter("action").equals("saveRing")) {
                long userId = Long.parseLong(request.getParameter("userId"));
                String day = request.getParameter("day");
                String wakeTime = request.getParameter("wakeTime");
                String message = request.getParameter("message");
                int turn = Integer.parseInt(request.getParameter("turn"));
                int generateId = 0;
                final Collection<Integer> ids = sm.getIds(userId);
                for (int id1 : ids) {
                    if (id1 > generateId) {
                        generateId = id1;
                    }
                }
                sm.saveRing(userId, generateId + 1, day, wakeTime, message, turn);
            }
            if (request.getParameter("action").equals("saveSchedule")) {
                long userId = Long.parseLong(request.getParameter("userId"));
                String day = request.getParameter("day");
                String start_time = request.getParameter("startTime");
                String end_time = request.getParameter("endTime");
                String parity = request.getParameter("parity");
                String place = request.getParameter("place");
                String subject = request.getParameter("subject");
                String type = request.getParameter("type");
                String teacher = request.getParameter("teacher");
                int generateId = 0;
                final Collection<Integer> ids = sm.getIds(userId);
                for (int id1 : ids) {
                    if (id1 > generateId) {
                        generateId = id1;
                    }
                }
                sm.saveSchedule(userId ,generateId + 1, day, start_time, end_time, parity, place, subject, type,
                        teacher);
            }
        } catch (Exception ex) {
            response.addError("Exception", 9001);
            ex.printStackTrace();
        }
    }
}
