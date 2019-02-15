package model;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/13 17:05
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultField {

    private Map<String, Object> fields = new LinkedHashMap<String, Object>();

    private List<Request> requests = Lists.newArrayList();

    private boolean skip;

}
