package tech.pacifici.patronus.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tech.pacifici.patronus.logger.ErrorLogger

@Configuration
class LoggerConfig {
    @Bean
    fun errorLogger(): ErrorLogger = ErrorLogger()
}
