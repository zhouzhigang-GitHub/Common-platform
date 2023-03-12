package com.common.platform.sys.beetl;

import com.common.platform.base.utils.CoreUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.util.Properties;

/**
 * beetl配置(如果需要配置别的配置可参照这个形式自己添加)
 */

@Data
public class BeetlProperties {

    public static final String BEETLCONF_PREFIX = "beetl";

    private String delimiterStatementStart;

    private String delimiterStatementEnd;

    private String resourceTagroot;

    private String resourceTagsuffix;

    private String resourceAutoCheck;

    @Value("${spring.mvc.view.prefix}")
    private String prefix;

    public Properties getProperties() {
        Properties properties = new Properties();
        if (CoreUtil.isNotEmpty(delimiterStatementStart)) {
            if (delimiterStatementStart.startsWith("\\")) {
                delimiterStatementStart = delimiterStatementStart.substring(1);
            }
            properties.setProperty("DELIMITER_STATEMENT_START", delimiterStatementStart);
        }
        if (CoreUtil.isNotEmpty(delimiterStatementEnd)) {
            properties.setProperty("DELIMITER_STATEMENT_END", delimiterStatementEnd);
        } else {
            properties.setProperty("DELIMITER_STATEMENT_END", "null");
        }
        if (CoreUtil.isNotEmpty(resourceTagroot)) {
            properties.setProperty("RESOURCE.tagRoot", resourceTagroot);
        }
        if (CoreUtil.isNotEmpty(resourceTagsuffix)) {
            properties.setProperty("RESOURCE.tagSuffix", resourceTagsuffix);
        }
        if (CoreUtil.isNotEmpty(resourceAutoCheck)) {
            properties.setProperty("RESOURCE.autoCheck", resourceAutoCheck);
        }
        return properties;
    }
}
