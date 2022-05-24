package com.synectiks.procurement;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.graylog2.gelfclient.GelfConfiguration;
import org.graylog2.gelfclient.GelfTransports;
import org.graylog2.gelfclient.transport.GelfTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import com.synectiks.procurement.business.service.XformAwsS3Config;
import com.synectiks.procurement.config.ApplicationProperties;
import com.synectiks.procurement.config.Constants;

import io.github.jhipster.config.DefaultProfileUtil;
import io.github.jhipster.config.JHipsterConstants;

@SpringBootApplication
@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationProperties.class})
public class ProcurementApp {
	
    private static final Logger log = LoggerFactory.getLogger(ProcurementApp.class);

    private static ConfigurableApplicationContext ctx = null;
    private final Environment env;
    private static String gelfHost;
    
    public ProcurementApp(Environment env) {
        this.env = env;
    }

    /**
     * Initializes procurement.
     * <p>
     * Spring profiles can be configured with a program argument --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>.
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run " +
                "with both the 'dev' and 'prod' profiles at the same time.");
        }
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)) {
            log.error("You have misconfigured your application! It should not " +
                "run with both the 'dev' and 'cloud' profiles at the same time.");
        }
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ProcurementApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
//        Environment env = app.run(args).getEnvironment();
        ctx  = app.run(args);
        Environment env = ctx.getEnvironment();
        gelfHost = env.getProperty("gelf.server");
        updateConstants(env);
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Application '{}' is running! Access URLs:\n\t" +
                "Local: \t\t{}://localhost:{}{}\n\t" +
                "External: \t{}://{}:{}{}\n\t" +
                "Profile(s): \t{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles());
    }
    
    @Bean
    @Autowired
    public XformAwsS3Config getAwsS3Configurations() {
    	return XformAwsS3Config.builder()
        		.apiAccessKey(env.getProperty("aws.access-key"))
        		.apiSecretKey(env.getProperty("aws.secret-key"))
        		.region(env.getProperty("aws.region"))
        		.build();
    }
    
    @Bean
    @Autowired
	public GelfConfiguration getGelfConfiguration() {
		String host = env.getProperty("gelf.server");
		int port = Integer.parseInt(env.getProperty("gelf.port"));
		
		return new GelfConfiguration(new InetSocketAddress(host, port))
        .transport(GelfTransports.TCP)
        .queueSize(Integer.parseInt(env.getProperty("gelf.queue-size")))
        .connectTimeout(Integer.parseInt(env.getProperty("gelf.connect-timeout")))
        .reconnectDelay(Integer.parseInt(env.getProperty("gelf.reconnect-delay")))
        .tcpNoDelay(true)
        .sendBufferSize(Integer.parseInt(env.getProperty("gelf.send-buffer-size")));
	}
    
    @Bean
    @Autowired
	public GelfTransport getGelfTransport() {
		return GelfTransports.create(getGelfConfiguration());
	}
    
//    
//    @Bean
//    @Autowired
//    public AWSCredentials getAwsCredentials() {
//    	return new BasicAWSCredentials(env.getProperty("aws.access-key"),env.getProperty("aws.secret-key")); 
//    }
    
    public static void updateConstants(Environment env) {
    	Constants.REQUISITION_BUCKET = env.getProperty("requisition.aws.bucket");
    	Constants.REQUISITION_DIRECTORY = env.getProperty("requisition.aws.directory");
    	Constants.IS_LOCAL_FILE_STORE = env.getProperty("file-storage.local-storage");
    	Constants.LOCAL_FILE_PATH = env.getProperty("file-storage.local-file-path");
    	Constants.IS_AWS_FIEL_STORE = env.getProperty("file-storage.aws-storage");
    }
    
    public static <T> T getBean(Class<T> cls) {
		return ctx.getBean(cls);
	}
    
    public static String getGelfHost() {
    	return gelfHost;
    }
}
