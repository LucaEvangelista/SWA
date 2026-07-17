package eva.luca.soccorso_Web.data.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public final class ConnectionFactory {

	//rappresenta il pool delle connseesioni
    private static final HikariDataSource DATA_SOURCE;

    private ConnectionFactory() {
        // Impedisce di creare oggetti ConnectionFactory.
    }

    static {
        try {
            HikariConfig config = new HikariConfig();

            
             //Dati già presenti nella tua classe DBConfig.
            config.setDriverClassName(DBConfig.DRIVER);
            config.setJdbcUrl(DBConfig.URL);
            config.setUsername(DBConfig.USER);
            config.setPassword(DBConfig.PASSWORD);

            //potrà aprire al massimo 10 connessioni
            config.setMaximumPoolSize(10);

            //cerca di mantenere almeno due connessioni pronte
            config.setMinimumIdle(2);

            //tempo massimo di attesa
            config.setConnectionTimeout(30_000);

            //dopo 10 min una connessione libera viene rimossa
            config.setIdleTimeout(600_000);

            //dopo 30 min una connessione viene sostituita con una nuova
            config.setMaxLifetime(1_800_000);

            config.setPoolName("SoccorsoWebPool");
            config.setAutoCommit(true);

            //qui viene creata la pool
            DATA_SOURCE = new HikariDataSource(config);

            System.out.println(
                    "Pool JDBC HikariCP inizializzato correttamente");
        } catch (Exception e) {
            throw new ExceptionInInitializerError(
                    "Impossibile inizializzare il pool JDBC: "
                    + e.getMessage()
            );
        }
    }

    //restituisce una connessione tra quelle disponibili nel pool
    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }

    //chiude definitivamente il pool utilizzando poolshutdownlistener quando chiudo tomcat o ripubblico il progetto 
    public static void closePool() {
        if (!DATA_SOURCE.isClosed()) {
            DATA_SOURCE.close();

            System.out.println(
                    "Pool JDBC HikariCP chiuso correttamente");
        }
    }
}