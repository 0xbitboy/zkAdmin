package club.calabashbrothers.zkadmin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@EnableWebMvc
@MapperScan(basePackages = "club.calabashbrothers.zkadmin.domain.mapper")
@ComponentScan(basePackages="club.calabashbrothers.zkadmin")
@SpringBootApplication
public class ZkAdminApplication extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(ZkAdminApplication.class, args);
	}
}
