package com.egen.weight.tracker.easyRules;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="metricRuleUnderWeight", ignoreUnknownFields=false)
public class MetricProperties {

}
