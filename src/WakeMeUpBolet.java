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
            if (request.getParameter("action").equals("rings")) {
                String group = request.getParameter("group");
                String day = request.getParameter("day");
                String parity = request.getParameter("parity");
                response.addObject("rings", new Schedule().getRings(group, day, parity));
            }
            if (request.getParameter("action").equals("schedule")) {
                String group = request.getParameter("group");
                String day = request.getParameter("day");
                String parity = request.getParameter("parity");
                Collection<Collection<String>> schedule = new Schedule().getSchedule(group, day, parity);
                int cnt = 0;
                for (Collection<String> note : schedule) {
                    response.addObject(cnt + "", note);
                    cnt++;
                }
            }
        } catch (Exception ex) {
            response.addError("Exception", 9001);
            ex.printStackTrace();
        }
    }
}
