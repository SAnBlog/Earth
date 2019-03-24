package cn.sanii.earth.pipeline;

import java.io.File;

/**
 * @author: Administrator
 * @create: 2019/3/24
 * Description: 持久化抽象管道 路径信息
 */
public abstract class BaseMemoryPipeline implements IPipeline {

    protected String prifix;

    protected void mkDir(String name) {
        prifix = new StringBuilder(FILE_PATH).append(name).append("/").toString();
        File file = new File(prifix);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
