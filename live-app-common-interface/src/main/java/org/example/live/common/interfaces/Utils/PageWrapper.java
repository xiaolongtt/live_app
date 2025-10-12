package org.example.live.common.interfaces.Utils;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/12 20:07
 * @注释
 */
@Data
public class PageWrapper<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -8740440596304624521L;
    private List<T> data;
    private boolean hasNext;
}
