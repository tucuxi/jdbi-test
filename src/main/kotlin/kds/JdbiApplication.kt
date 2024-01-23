package kds

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@SpringBootApplication
class JdbiApplication

fun main(args: Array<String>) {
    runApplication<JdbiApplication>(*args)
}

@Configuration
class Configuration {

    @Bean
    fun jdbi(dataSource: DataSource): Jdbi {
        return Jdbi.create(dataSource)
            .installPlugin(KotlinPlugin())
    }
}