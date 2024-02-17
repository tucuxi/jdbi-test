package kds

import org.jdbi.v3.spring5.JdbiRepository
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.kotlin.BindKotlin
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

@JdbiRepository
@UseClasspathSqlLocator
interface EventDao{
    @SqlUpdate
    fun insert(@BindKotlin event: Event, @Bind data: String)
}
