package cn.sanii.earth.process.impl;

import cn.sanii.earth.process.IAfterProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Administrator
 * @create: 2019/2/21
 * Description:
 */
@Slf4j
public class ConsoleAfterProcessor implements IAfterProcessor {
    @Override
    public void process() {
      log.info("让人类永远保持理智，确实是一件奢侈的事。——Moss");
      log.info("stop");
    }
}
