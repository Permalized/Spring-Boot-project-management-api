package com.projects.ProjectManagementAPI;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.projects.ProjectManagementAPI.auth.AuthenticationService;
import com.projects.ProjectManagementAPI.auth.RegisterRequest;
import com.projects.ProjectManagementAPI.user.Role;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAwareBean")
public class ProjectManagementAPIApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectManagementAPIApplication.class, args);
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
