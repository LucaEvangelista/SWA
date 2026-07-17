package eva.luca.soccorso_Web.data.db;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class PoolShutdownListener implements ServletContextListener {

    @Override
    public void contextDestroyed(
            ServletContextEvent event) {

        ConnectionFactory.closePool();
    }
}