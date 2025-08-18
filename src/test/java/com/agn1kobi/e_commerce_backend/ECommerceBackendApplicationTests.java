package com.agn1kobi.e_commerce_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Value;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ECommerceBackendApplicationTests {

    @Value("${spring.application.name}")
    private String appName;

	@Test
	void contextLoads() {
		// context loads and props available
		assertThat(appName).isNotBlank();
	}

}
