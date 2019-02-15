package pipeline;

import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import model.Response;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/14 14:07
 * @Description: 持久化到文件
 */
@Slf4j
public class SaveFilePipeline implements Pipeline {

    @Override
    public void process(Response response) {
        final File newFile = new File("D:/" + response.getName() + ".txt");
        response.getResultField().getFields().forEach((key, val) -> {
            try {
                String line = System.getProperty("line.separator");
                Files.append((key + " : " + val + line), newFile, Charset.defaultCharset());
            } catch (IOException e) {
                log.error("SaveFilePipeline eroror", e);
            }
        });
    }
}
