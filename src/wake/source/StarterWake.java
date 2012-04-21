package wake.source;

import ru.ifmo.enf.micelius.core.BoletsContainer;
import ru.ifmo.enf.micelius.server.BoletsRequestHandler;
import ru.ifmo.enf.micelius.server.ConfigKeys;
import ru.ifmo.enf.micelius.server.Server;

import java.util.Properties;

/**
 * User: Bogdanov Kirill
 * Date: 14.03.12
 * Time: 17:14
 */
public class StarterWake {
    public static void main(final String[] args) {

        final Properties configs = new Properties();
        configs.put(ConfigKeys.PORT, "8075");
        configs.put(ConfigKeys.MAX_THREADS, "10");

        final BoletsContainer boletsContainer = new BoletsContainer();
        boletsContainer.add("wakeBolet", new WakeMeUpBolet());
        final BoletsRequestHandler boletsRequestHandler = new BoletsRequestHandler(boletsContainer);

        new Server(configs, boletsRequestHandler);
    }
}
