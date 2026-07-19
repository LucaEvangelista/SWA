package eva.luca.soccorso_Web.base;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;

import eva.luca.soccorso_Web.resources.AdminRes;
import eva.luca.soccorso_Web.resources.AuthRes;
import eva.luca.soccorso_Web.resources.CompetenzeRes;
import eva.luca.soccorso_Web.resources.ConvalidaRes;
import eva.luca.soccorso_Web.resources.MaterialiRes;
import eva.luca.soccorso_Web.resources.MezziRes;
import eva.luca.soccorso_Web.resources.MissioniRes;
import eva.luca.soccorso_Web.resources.OperatoriRes;
import eva.luca.soccorso_Web.resources.PatentiRes;
import eva.luca.soccorso_Web.resources.RichiesteRes;
import eva.luca.soccorso_Web.resources.SquadreRes;
import eva.luca.soccorso_Web.security.AuthLoggedFilter;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("rest")
@OpenAPIDefinition(
	    info = @Info(
	        title = "Soccorso Web REST API",
	        version = "1.0.0",
	        description = "API REST per la gestione del sistema di soccorso"
	    ),
	    servers = {
	        @Server(
	            url = "/soccorso_Web_SWA",
	            description = "Server locale Tomcat"
	        )
	    }
	)

//configurazione JWT per swagger
	@SecurityScheme(
	    name = "bearerAuth",
	    type = SecuritySchemeType.HTTP,
	    scheme = "bearer",
	    bearerFormat = "JWT"
	)
public class RestApplication extends Application{
	
	private final Set<Class<?>> classes;
	private final Set<Object> singletons;
	
	public RestApplication() {
		HashSet<Class<?>> c = new HashSet<Class<?>>();
		
		
//        aggiungiamo tutte le *root resurces* (cioè quelle
//        con l'annotazione Path) che vogliamo pubblicare
		
		c.add(MezziRes.class);
		c.add(MaterialiRes.class);
		c.add(OperatoriRes.class);
		c.add(RichiesteRes.class);
		c.add(MissioniRes.class);
		c.add(PatentiRes.class);
		c.add(SquadreRes.class);
		c.add(ConvalidaRes.class);
		c.add(AuthRes.class);
		c.add(AuthLoggedFilter.class);
		c.add(CompetenzeRes.class);
		c.add(AdminRes.class);
		
		c.add(OpenApiResource.class);
		
		
//        aggiungiamo il provider Jackson per poter
//        usare i suoi servizi di serializzazione e 
//        deserializzazione JSON
		
        classes = Collections.unmodifiableSet(c);
        
//         ObjectMapper personalizzato per gestire LocalDate, LocalDateTime
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        
//         Fa stampare le date come stringhe, es: "2000-05-12"
        
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        
        
        JacksonJsonProvider provider = new JacksonJsonProvider();
        provider.setMapper(mapper);

        HashSet<Object> s = new HashSet<>();
        s.add(provider);

        singletons = Collections.unmodifiableSet(s);
        
	}
	
    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
    
    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

}
