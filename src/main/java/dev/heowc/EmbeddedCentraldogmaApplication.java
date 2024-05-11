package dev.heowc;

import com.linecorp.centraldogma.server.CentralDogma;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class EmbeddedCentraldogmaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmbeddedCentraldogmaApplication.class, args);
    }

    @Configuration
    static class CentraldogmaConfiguration {

        @Bean
        CentralDogma centralDogma(@Value("${dogma.path:classpath:dogma.json}") File dogmaConfigFile) throws IOException {
            return CentralDogma.forConfig(dogmaConfigFile);
        }
    }

    @Component
    static class CentralDogmaLifecycle implements SmartLifecycle {

        private final CentralDogma centralDogma;
        private volatile boolean running;

        CentralDogmaLifecycle(CentralDogma centralDogma) {
            this.centralDogma = centralDogma;
        }

        @Override
        public void start() {
            centralDogma.start().join();
            running = true;
        }

        @Override
        public void stop() {
            centralDogma.stop().join();
            running = false;
        }

        @Override
        public boolean isRunning() {
            return running;
        }
    }
}
