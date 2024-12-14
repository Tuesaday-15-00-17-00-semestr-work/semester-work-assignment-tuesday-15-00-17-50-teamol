package Server.library;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.StandardBasicTypes;

public class SQLiteDialect extends Dialect {
    public SQLiteDialect() {
        super();
    }

    public void initializeFunctionRegistry(
            FunctionContributions functionContributions,
            SessionFactoryImplementor sessionFactory) {
        functionContributions.getFunctionRegistry().register("concat", new StandardSQLFunction("concat", StandardBasicTypes.STRING));
        functionContributions.getFunctionRegistry().register("mod", new StandardSQLFunction("mod", StandardBasicTypes.INTEGER));
        functionContributions.getFunctionRegistry().register("substr", new StandardSQLFunction("substr", StandardBasicTypes.STRING));
        functionContributions.getFunctionRegistry().register("length", new StandardSQLFunction("length", StandardBasicTypes.INTEGER));
    }

    @Override
    public boolean dropConstraints() {
        // SQLite does not support dropping constraints
        return false;
    }

    @Override
    public boolean qualifyIndexName() {
        // Index names are unqualified in SQLite
        return false;
    }



    @Override
    public String getAddColumnString() {
        return "add column";
    }

    @Override
    public String getForUpdateString() {
        return "";
    }


}