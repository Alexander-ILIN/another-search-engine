package main.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Класс, использующийся для добавления префикса к таблицам
 */
@Component
public class PrefixPhysicalNamingStrategy extends PhysicalNamingStrategyStandardImpl
{
    @Autowired
    private Config config;

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context)
    {
        String tablesPrefix = config.getTablesPrefix();
        Identifier newIdentifier = new Identifier(tablesPrefix + name.getText(), name.isQuoted());
        return super.toPhysicalTableName(newIdentifier, context);
    }


}
