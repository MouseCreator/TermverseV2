package mouse.project.termverseweb.mouselib.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
@Component
public class DataSourceInit {
    @Autowired
    public DataSourceInit(DataSource dataSource) {
        DataSourceTransfer.dataSource = dataSource;
    }

}
