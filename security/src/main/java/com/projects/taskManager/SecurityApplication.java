package com.projects.taskManager;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.projects.taskManager.auth.AuthenticationService;
import com.projects.taskManager.auth.RegisterRequest;
import com.projects.taskManager.user.Role;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAwareBean")
public class SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

		@Bean
		public CommandLineRunner commandLineRunner(AuthenticationService service) {
			return args -> {
				var admin=RegisterRequest.builder()
						.firstName("Admin")
						.lastName("Admin")
						.email("admin@mail.com")
						.password("adminpassword")
						.role(Role.ADMIN)
						.build();
				System.out.println("Admin token: " + service.register(admin).getAccess_token());

				

				var manager=RegisterRequest.builder()
						.firstName("Manager")
						.lastName("Manager")
						.email("manager@mail.com")
						.password("managerpassword")
						.role(Role.MANAGER)
						.build();
				System.out.println("Manager token: " + service.register(manager).getAccess_token());
			};
		}
}
