package club.calabashbrothers.zkadmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

/**
 * Created by liaojiacan on 2017/6/27.
 */
@Configuration
public class MybatisConfigruation implements TransactionManagementConfigurer{

    @Autowired
    DataSource dataSource;

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return null;
    }
}
