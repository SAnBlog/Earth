package cn.sanii.earth.model;

import cn.sanii.earth.model.enums.FieldEnum;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private Map<FieldEnum, Object> fields = Maps.newLinkedHashMap();

    private List<Request> requests = Lists.newArrayList();

    private boolean skip;

}
