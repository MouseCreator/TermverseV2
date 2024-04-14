package mouse.project.lib.data.orm.init;

import mouse.project.lib.data.orm.desc.ModelDescription;
import mouse.project.lib.data.orm.map.OrmMap;
import mouse.project.lib.data.orm.scan.ModelScanner;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;

import java.util.List;

@Service
public class OrmInitializationImpl implements OrmInitialization {
    private final OrmMap ormMap;
    private final ModelScanner modelScanner;
    @Auto
    public OrmInitializationImpl(OrmMap ormMap, ModelScanner modelScanner) {
        this.ormMap = ormMap;
        this.modelScanner = modelScanner;
    }
    @Override
    public void initialize(String basePkg) {
        List<ModelDescription<?>> modelDescriptions = modelScanner.scanAndDescribe(basePkg);
        modelDescriptions.forEach(ormMap::put);
    }
}
