package cn.sanii.earth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: shouliang.wang
 * @Date: 2018/10/12 15:02
 * @Description: ip实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IP {
    private String host;
    private Integer port;
}
