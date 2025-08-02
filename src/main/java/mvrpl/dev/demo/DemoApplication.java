package mvrpl.dev.demo;

import com.datastax.oss.driver.api.core.CqlSession;
import mvrpl.dev.model.User;
import mvrpl.dev.model.Pessoas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;


@SpringBootApplication
@EnableCassandraRepositories(basePackages="mvrpl.dev.model")
@ComponentScan({"mvrpl.dev.controller"})
@EnableAutoConfiguration
public class DemoApplication {

	@Autowired(required = true)
	private Pessoas pessoas;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
