package com.tiendita.tienditainventario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;

@SpringBootApplication
public class TienditainventarioApplication {

	public static void main(String[] args) {
		// CONFIGURACIÓN RADICAL: Silenciar Hibernate ANTES de que Spring inicie
		silenciarHibernate();
		
		SpringApplication.run(TienditainventarioApplication.class, args);
	}
	
	private static void silenciarHibernate() {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		
		// Silenciar TODOS los loggers de Hibernate
		Logger hibernateLogger = loggerContext.getLogger("org.hibernate");
		hibernateLogger.setLevel(Level.OFF);
		
		Logger sqlLogger = loggerContext.getLogger("org.hibernate.SQL");
		sqlLogger.setLevel(Level.OFF);
		
		Logger typeLogger = loggerContext.getLogger("org.hibernate.type");
		typeLogger.setLevel(Level.OFF);
		
		Logger ormLogger = loggerContext.getLogger("org.hibernate.orm");
		ormLogger.setLevel(Level.OFF);
		
		Logger engineLogger = loggerContext.getLogger("org.hibernate.engine");
		engineLogger.setLevel(Level.OFF);
		
		Logger resourceLogger = loggerContext.getLogger("org.hibernate.resource");
		resourceLogger.setLevel(Level.OFF);
		
		Logger jdbcLogger = loggerContext.getLogger("org.hibernate.jdbc");
		jdbcLogger.setLevel(Level.OFF);
		
		Logger queryLogger = loggerContext.getLogger("org.hibernate.query");
		queryLogger.setLevel(Level.OFF);
		
		Logger internalLogger = loggerContext.getLogger("org.hibernate.internal");
		internalLogger.setLevel(Level.OFF);
		
		Logger hqlLogger = loggerContext.getLogger("org.hibernate.hql");
		hqlLogger.setLevel(Level.OFF);
		
		// Silenciar Kafka también
		Logger kafkaLogger = loggerContext.getLogger("org.apache.kafka");
		kafkaLogger.setLevel(Level.OFF);
		
		System.out.println("HIBERNATE LOGGING SILENCIADO PROGRAMÁTICAMENTE");
	}

}
