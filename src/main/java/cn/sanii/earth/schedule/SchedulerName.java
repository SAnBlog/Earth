package cn.sanii.earth.schedule;

import lombok.Data;

/**
 * @author: Administrator
 * @create: 2019/2/19
 * Description: 扩展Scheduler，增加任务id标识
 */
@Data
public abstract class SchedulerName {
    protected String fieldName;
}
