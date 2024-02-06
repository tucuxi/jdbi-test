package kds

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.core.statement.Slf4JSqlLogger
import org.jdbi.v3.spring5.EnableJdbiRepositories
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@SpringBootApplication
@EnableJdbiRepositories
@EnableTransactionManagement
class JdbiApplication

fun main(args: Array<String>) {
    runApplication<JdbiApplication>(*args)
}

@Configuration
class Configuration {

    @Bean
    fun jdbi(dataSource: DataSource): Jdbi {
        return Jdbi
            .create(TransactionAwareDataSourceProxy(dataSource))
            .installPlugin(KotlinPlugin())
            .installPlugin(KotlinSqlObjectPlugin())
            .setSqlLogger(Slf4JSqlLogger())
    }
}