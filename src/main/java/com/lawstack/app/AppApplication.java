// `package com.lawstack.app;` is declaring the package name for the Java class `AppApplication`. This
// package name is used to organize and group related classes together. In this case, the package name
// is `com.lawstack.app`, which suggests that this class is part of an application developed by the
// Lawstack company.
package com.lawstack.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

}
